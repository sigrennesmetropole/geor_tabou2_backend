package rm.tabou2.service.sig;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.factory.CommonFactoryFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;

import io.netty.channel.ChannelOption;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import rm.tabou2.service.exception.GeoserverProxyException;
import rm.tabou2.service.helper.operation.OperationReadAccess;
import rm.tabou2.storage.tabou.item.OperationVisibilityFilter;

/**
 * Proxy cartographique vers GeoServer (US-323). Seules les couches autorisées (tabou2.geoserver.authorized-layers)
 * sont exposées : la règle de visibilité de l'utilisateur est imposée dans le filtre de chaque requête et l'appel est
 * fait avec le compte technique. Ces couches doivent exposer les colonnes mode_etape, prospectif et code_insee.
 */
@Service
public class GeoserverProxyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoserverProxyService.class);

    private static final String SERVICE = "SERVICE";
    private static final String REQUEST = "REQUEST";
    private static final String CQL_FILTER = "CQL_FILTER";
    private static final String BBOX = "BBOX";
    private static final String WMS = "WMS";
    private static final String WFS = "WFS";
    private static final String GET_MAP = "GETMAP";
    private static final String GET_FEATURE_INFO = "GETFEATUREINFO";
    private static final String GET_LEGEND_GRAPHIC = "GETLEGENDGRAPHIC";
    private static final String GET_FEATURE = "GETFEATURE";
    private static final String DESCRIBE_FEATURE_TYPE = "DESCRIBEFEATURETYPE";

    private static final Map<String, Set<String>> ALLOWED_REQUESTS = Map.of(
            WMS, Set.of(GET_MAP, GET_FEATURE_INFO, GET_LEGEND_GRAPHIC),
            WFS, Set.of(GET_FEATURE, DESCRIBE_FEATURE_TYPE));

    /** Requêtes dont les données sont filtrées par la règle de visibilité. */
    private static final Set<String> FILTERED_REQUESTS = Set.of(GET_MAP, GET_FEATURE_INFO, GET_FEATURE);

    /** Paramètres refusés à tous les profils. */
    private static final Set<String> FORBIDDEN_PARAMS = Set.of("AUTHKEY");

    /** Paramètres permettant de contourner le CQL_FILTER, refusés aux profils non globaux. */
    private static final Set<String> RESTRICTED_PARAMS = Set.of("FEATUREID", "RESOURCEID", "FILTER", "SLD", "SLD_BODY",
            "VIEWPARAMS", "STOREDQUERY_ID", "REMOTE_OWS_URL", "REMOTE_OWS_TYPE");

    private static final Set<String> FORBIDDEN_CQL_TOKENS = Set.of("querycollection", "querysingle", ";");

    private static final FilterFactory FILTER_FACTORY = CommonFactoryFinder.getFilterFactory();

    private final OperationReadAccess operationReadAccess;

    private final String geoserverUrl;

    private final String username;

    private final String password;

    private final Set<String> authorizedLayers;

    private final WebClient webClient;

    @Autowired
    public GeoserverProxyService(OperationReadAccess operationReadAccess,
                                 @Value("${tabou2.geoserver.url:}") String geoserverUrl,
                                 @Value("${tabou2.geoserver.username:}") String username,
                                 @Value("${tabou2.geoserver.password:}") String password,
                                 @Value("${tabou2.geoserver.authorized-layers:tabou:v_oa_operation}")
                                 String[] authorizedLayers,
                                 @Value("${tabou2.geoserver.pool.max-connections:50}") int maxConnections,
                                 @Value("${tabou2.geoserver.pool.acquire-timeout-ms:5000}") int acquireTimeoutMs,
                                 @Value("${tabou2.geoserver.connect-timeout-ms:5000}") int connectTimeoutMs,
                                 @Value("${tabou2.geoserver.read-timeout-ms:60000}") int readTimeoutMs) {
        this(operationReadAccess, geoserverUrl, username, password, new HashSet<>(Arrays.asList(authorizedLayers)),
                buildWebClient(maxConnections, acquireTimeoutMs, connectTimeoutMs, readTimeoutMs));
    }

    GeoserverProxyService(OperationReadAccess operationReadAccess, String geoserverUrl, String username,
                          String password, Set<String> authorizedLayers, WebClient webClient) {
        this.operationReadAccess = operationReadAccess;
        this.geoserverUrl = geoserverUrl;
        this.username = username;
        this.password = password;
        this.authorizedLayers = authorizedLayers.stream().map(String::trim).filter(name -> !name.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
        this.webClient = webClient;
    }

    // Redirections non suivies (comportement par défaut de Reactor Netty)
    private static WebClient buildWebClient(int maxConnections, int acquireTimeoutMs, int connectTimeoutMs,
                                            int readTimeoutMs) {
        ConnectionProvider pool = ConnectionProvider.builder("geoserver-proxy")
                .maxConnections(maxConnections)
                .pendingAcquireTimeout(Duration.ofMillis(acquireTimeoutMs))
                .build();
        HttpClient httpClient = HttpClient.create(pool)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(readTimeoutMs));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    /**
     * Relaie une requête OGC vers GeoServer après contrôle et application de la règle de visibilité.
     *
     * @param post       true si la requête reçue est un POST x-www-form-urlencoded
     * @param parameters paramètres de la requête (query string et corps formulaire)
     * @return la réponse GeoServer, à fermer par l'appelant
     * @throws GeoserverProxyException requête refusée (400) ou GeoServer indisponible (502, 503)
     */
    public GeoserverStream proxy(boolean post, Map<String, String[]> parameters) throws GeoserverProxyException {
        checkConfigured();
        OperationVisibilityFilter visibility = operationReadAccess.getVisibilityFilter();
        Map<String, String> upstream = buildUpstreamParameters(visibility, parameters);
        return execute(post, upstream);
    }

    /**
     * Relaie une requête WFS GetFeature envoyée en XML (table attributaire de MapStore) après contrôle et application
     * de la règle de visibilité.
     *
     * @param body         corps XML reçu
     * @param outputFormat paramètre outputFormat de l'URL (MapStore y place le format), ou null
     * @return la réponse GeoServer, à fermer par l'appelant
     * @throws GeoserverProxyException requête refusée (400) ou GeoServer indisponible (502, 503)
     */
    public GeoserverStream proxyXml(byte[] body, String outputFormat) throws GeoserverProxyException {
        checkConfigured();
        byte[] upstream = WfsGetFeatureXml.secure(body, authorizedLayers, operationReadAccess.getVisibilityFilter(),
                outputFormat);
        return send(webClient.post().uri(baseUri()).contentType(MediaType.APPLICATION_XML).bodyValue(upstream));
    }

    private void checkConfigured() throws GeoserverProxyException {
        if (geoserverUrl == null || geoserverUrl.isBlank()) {
            throw new GeoserverProxyException(503, "Proxy cartographique non configuré");
        }
    }

    Map<String, String> buildUpstreamParameters(OperationVisibilityFilter visibility, Map<String, String[]> parameters)
            throws GeoserverProxyException {
        // Les noms de paramètres OGC sont insensibles à la casse : un doublon est ambigu, donc refusé
        Map<String, String> byName = new LinkedHashMap<>();
        Map<String, String> originalNames = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String key = entry.getKey().toUpperCase(Locale.ROOT);
            String[] values = entry.getValue();
            if (values == null || values.length != 1 || byName.containsKey(key)) {
                throw badRequest("Paramètre répété : " + entry.getKey());
            }
            byName.put(key, values[0]);
            originalNames.put(key, entry.getKey());
        }

        String service = byName.getOrDefault(SERVICE, "").toUpperCase(Locale.ROOT);
        String request = byName.getOrDefault(REQUEST, "").toUpperCase(Locale.ROOT);
        Set<String> allowed = ALLOWED_REQUESTS.get(service);
        if (allowed == null || !allowed.contains(request)) {
            throw badRequest("Requête OGC non autorisée");
        }

        checkLayers(service, request, byName);

        for (String key : byName.keySet()) {
            if (FORBIDDEN_PARAMS.contains(key) || (!visibility.isGlobal() && RESTRICTED_PARAMS.contains(key))) {
                throw badRequest("Paramètre non autorisé : " + originalNames.get(key));
            }
        }
        // Un BBOX WFS n'est pas combiné de façon fiable avec CQL_FILTER ; le client doit exprimer l'emprise en CQL
        if (!visibility.isGlobal() && WFS.equals(service) && GET_FEATURE.equals(request) && byName.containsKey(BBOX)) {
            throw badRequest("Paramètre non autorisé : " + originalNames.get(BBOX));
        }

        String userCql = byName.get(CQL_FILTER);
        checkCql(userCql);

        if (FILTERED_REQUESTS.contains(request) && !visibility.isGlobal()) {
            byName.put(CQL_FILTER, combineFilters(userCql, visibility.toCql()));
            originalNames.putIfAbsent(CQL_FILTER, CQL_FILTER);
        }

        Map<String, String> result = new LinkedHashMap<>();
        byName.forEach((key, value) -> result.put(originalNames.get(key), value));
        return result;
    }

    private void checkLayers(String service, String request, Map<String, String> params) throws GeoserverProxyException {
        List<String> names = new ArrayList<>();
        if (WMS.equals(service)) {
            if (GET_LEGEND_GRAPHIC.equals(request)) {
                names.add(params.get("LAYER"));
            } else {
                names.add(params.get("LAYERS"));
                if (GET_FEATURE_INFO.equals(request) && params.containsKey("QUERY_LAYERS")) {
                    names.add(params.get("QUERY_LAYERS"));
                }
            }
        } else {
            names.add(params.get("TYPENAME"));
            names.add(params.get("TYPENAMES"));
        }
        boolean found = false;
        for (String name : names) {
            if (name == null) {
                continue;
            }
            found = true;
            if (!authorizedLayers.contains(name.trim())) {
                throw badRequest("Couche non autorisée");
            }
        }
        // Sans nom de couche, GeoServer s'appliquerait à toutes les couches
        if (!found) {
            throw badRequest("Couche non autorisée");
        }
    }

    private void checkCql(String cql) throws GeoserverProxyException {
        if (cql == null) {
            return;
        }
        String lower = cql.toLowerCase(Locale.ROOT);
        for (String token : FORBIDDEN_CQL_TOKENS) {
            if (lower.contains(token)) {
                throw badRequest("CQL_FILTER non autorisé");
            }
        }
    }

    private String combineFilters(String userCql, String securityCql) throws GeoserverProxyException {
        try {
            Filter security = ECQL.toFilter(securityCql);
            if (userCql == null || userCql.isBlank()) {
                return ECQL.toCQL(security);
            }
            // Chaque filtre est parsé séparément : une injection (1=1) OR (1=1 ne peut pas sortir de sa branche
            return ECQL.toCQL(FILTER_FACTORY.and(ECQL.toFilter(userCql), security));
        } catch (CQLException | RuntimeException e) {
            throw new GeoserverProxyException(400, "CQL_FILTER invalide", e);
        }
    }

    private GeoserverStream execute(boolean post, Map<String, String> parameters) throws GeoserverProxyException {
        if (post) {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            parameters.forEach(form::add);
            return send(webClient.post().uri(baseUri()).body(BodyInserters.fromFormData(form)));
        }
        // Noms et valeurs passent par des variables pour être encodés strictement (le "+" ne doit pas rester tel quel)
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(geoserverUrl);
        Map<String, String> variables = new HashMap<>();
        int index = 0;
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            builder.queryParam("{n" + index + "}", "{v" + index + "}");
            variables.put("n" + index, parameter.getKey());
            variables.put("v" + index, parameter.getValue());
            index++;
        }
        return send(webClient.get().uri(builder.encode().buildAndExpand(variables).toUri()));
    }

    private URI baseUri() throws GeoserverProxyException {
        try {
            return URI.create(geoserverUrl);
        } catch (IllegalArgumentException e) {
            throw new GeoserverProxyException(503, "URL GeoServer invalide", e);
        }
    }

    private GeoserverStream send(WebClient.RequestHeadersSpec<?> request) throws GeoserverProxyException {
        ResponseEntity<Flux<DataBuffer>> response;
        try {
            response = request.headers(headers -> headers.setBasicAuth(username, password, StandardCharsets.UTF_8))
                    .retrieve()
                    // Les erreurs HTTP de GeoServer (exceptions OGC) sont relayées telles quelles
                    .onStatus(HttpStatusCode::isError, clientResponse -> Mono.empty())
                    .toEntityFlux(DataBuffer.class)
                    .block();
        } catch (WebClientException e) {
            throw new GeoserverProxyException(502, "Erreur lors de l'appel à GeoServer", e);
        }
        int status = response.getStatusCode().value();
        GeoserverStream stream = new GeoserverStream(status, response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE),
                response.getBody());
        if (status == 401 || status == 403) {
            // Problème du compte technique : ne pas le faire passer pour un refus de l'utilisateur
            LOGGER.error("GeoServer a refusé le compte technique du proxy (HTTP {})", status);
            stream.close();
            throw new GeoserverProxyException(502, "GeoServer indisponible");
        }
        return stream;
    }

    private static GeoserverProxyException badRequest(String message) {
        return new GeoserverProxyException(400, message);
    }
}

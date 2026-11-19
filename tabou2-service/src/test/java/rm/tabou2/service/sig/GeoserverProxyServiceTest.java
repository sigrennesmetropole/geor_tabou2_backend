package rm.tabou2.service.sig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import rm.tabou2.service.exception.GeoserverProxyException;
import rm.tabou2.service.helper.operation.OperationReadAccess;
import rm.tabou2.storage.tabou.item.OperationVisibilityFilter;

class GeoserverProxyServiceTest {

    private static final String LAYER = "tabou:v_oa_operation";

    private static final String SECOND_LAYER = "tabou:v_oa_operation_2";

    private static final OperationVisibilityFilter BRUZ = OperationVisibilityFilter.commune(new BigDecimal(35047));

    private OperationReadAccess readAccess;

    private ClientRequest sentRequest;

    private ClientResponse geoserverResponse;

    private GeoserverProxyService service;

    @BeforeEach
    void setUp() {
        readAccess = mock(OperationReadAccess.class);
        WebClient webClient = WebClient.builder().exchangeFunction(request -> {
            sentRequest = request;
            return Mono.just(geoserverResponse);
        }).build();
        service = new GeoserverProxyService(readAccess, "http://geoserver/ows", "carto", "secret",
                Set.of(LAYER, SECOND_LAYER), webClient);
    }

    private static Map<String, String[]> params(String... keyValues) {
        Map<String, String[]> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put(keyValues[i], new String[] { keyValues[i + 1] });
        }
        return map;
    }

    private static Map<String, String[]> getMap(String... extra) {
        Map<String, String[]> map = params("SERVICE", "WMS", "REQUEST", "GetMap", "LAYERS", LAYER);
        for (int i = 0; i < extra.length; i += 2) {
            map.put(extra[i], new String[] { extra[i + 1] });
        }
        return map;
    }

    private static String value(Map<String, String> parameters, String name) {
        return parameters.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(name)).map(Map.Entry::getValue)
                .findFirst().orElse(null);
    }

    private void assertRefused(OperationVisibilityFilter filter, Map<String, String[]> parameters) {
        GeoserverProxyException e = assertThrows(GeoserverProxyException.class,
                () -> service.buildUpstreamParameters(filter, parameters));
        assertEquals(400, e.getHttpStatus());
    }

    @Test
    void commune_profile_gets_security_filter() throws Exception {
        Map<String, String> result = service.buildUpstreamParameters(BRUZ, getMap());

        String cql = value(result, "CQL_FILTER");
        assertTrue(cql.contains("35047"));
        assertTrue(cql.contains("mode_etape"));
    }

    @Test
    void user_filter_is_combined_with_security_filter() throws Exception {
        Map<String, String> result = service.buildUpstreamParameters(BRUZ, getMap("cql_filter", "id_tabou = 12"));

        String cql = value(result, "CQL_FILTER");
        assertEquals(1, result.keySet().stream().filter(name -> name.equalsIgnoreCase("CQL_FILTER")).count());
        assertTrue(cql.contains("id_tabou = 12"));
        assertTrue(cql.contains("35047"));
        assertTrue(cql.contains(" AND "));
    }

    @Test
    void public_only_profile_has_no_off_branch() throws Exception {
        String cql = value(service.buildUpstreamParameters(OperationVisibilityFilter.publicOnly(), getMap()),
                "CQL_FILTER");

        assertFalse(cql.contains("OFF"));
        assertTrue(cql.contains("PUBLIC"));
    }

    @Test
    void global_profile_filter_is_untouched() throws Exception {
        Map<String, String> result = service.buildUpstreamParameters(OperationVisibilityFilter.global(),
                getMap("CQL_FILTER", "id_tabou = 12"));

        assertEquals("id_tabou = 12", value(result, "CQL_FILTER"));
        assertNull(value(service.buildUpstreamParameters(OperationVisibilityFilter.global(), getMap()), "CQL_FILTER"));
    }

    @Test
    void injection_is_refused() {
        assertRefused(BRUZ, getMap("CQL_FILTER", "1=1) OR (1=1"));
        assertRefused(BRUZ, getMap("CQL_FILTER", "id_tabou = 1 OR 1=1) --"));
    }

    @Test
    void every_configured_operation_layer_is_served_with_the_security_filter() throws Exception {
        for (String layer : List.of(LAYER, SECOND_LAYER)) {
            Map<String, String> result = service.buildUpstreamParameters(BRUZ,
                    params("SERVICE", "WMS", "REQUEST", "GetMap", "LAYERS", layer));

            assertTrue(value(result, "CQL_FILTER").contains("35047"));
        }
    }

    @Test
    void other_layer_is_refused() {
        assertRefused(BRUZ, params("SERVICE", "WMS", "REQUEST", "GetMap", "LAYERS", "tabou:autre"));
        assertRefused(BRUZ, params("SERVICE", "WMS", "REQUEST", "GetMap", "LAYERS", LAYER + ",tabou:autre"));
        assertRefused(BRUZ, params("SERVICE", "WMS", "REQUEST", "GetMap"));
        assertRefused(BRUZ, params("SERVICE", "WMS", "REQUEST", "GetFeatureInfo", "LAYERS", LAYER, "QUERY_LAYERS",
                "tabou:autre"));
        assertRefused(BRUZ, params("SERVICE", "WFS", "REQUEST", "DescribeFeatureType"));
        assertRefused(BRUZ, params("SERVICE", "WFS", "REQUEST", "GetFeature", "TYPENAMES", "tabou:autre"));
    }

    @Test
    void forbidden_cql_functions_are_refused() {
        assertRefused(BRUZ, getMap("CQL_FILTER", "queryCollection('a','b','INCLUDE')"));
        assertRefused(OperationVisibilityFilter.global(), getMap("CQL_FILTER", "QUERYSINGLE('a','b','INCLUDE')"));
        assertRefused(BRUZ, getMap("CQL_FILTER", "id_tabou = 1;INCLUDE"));
    }

    @Test
    void bypass_parameters_are_refused_for_non_global_only() throws Exception {
        assertRefused(BRUZ, params("SERVICE", "WFS", "REQUEST", "GetFeature", "TYPENAMES", LAYER, "FEATUREID", "a.1"));
        assertRefused(BRUZ, params("SERVICE", "WFS", "REQUEST", "GetFeature", "TYPENAMES", LAYER, "filter", "<xml/>"));
        assertRefused(BRUZ, getMap("sld_body", "<xml/>"));
        assertRefused(BRUZ, params("SERVICE", "WFS", "REQUEST", "GetFeature", "TYPENAMES", LAYER, "BBOX", "0,0,1,1"));
        assertRefused(OperationVisibilityFilter.global(), getMap("authkey", "x"));

        service.buildUpstreamParameters(OperationVisibilityFilter.global(), getMap("VIEWPARAMS", "a:b"));
    }

    @Test
    void ambiguous_or_unlisted_requests_are_refused() {
        assertRefused(BRUZ, params("SERVICE", "WMS", "REQUEST", "GetCapabilities"));
        assertRefused(BRUZ, params("SERVICE", "WPS", "REQUEST", "Execute"));
        assertRefused(BRUZ, params("REQUEST", "GetMap", "LAYERS", LAYER));
        Map<String, String[]> duplicated = getMap("layers", "tabou:autre");
        assertRefused(BRUZ, duplicated);
        Map<String, String[]> multiple = getMap();
        multiple.put("LAYERS", new String[] { LAYER, "tabou:autre" });
        assertRefused(BRUZ, multiple);
    }

    @Test
    void describe_feature_type_and_legend_are_not_filtered() throws Exception {
        Map<String, String> describe = service.buildUpstreamParameters(BRUZ,
                params("SERVICE", "WFS", "REQUEST", "DescribeFeatureType", "TYPENAME", LAYER));
        Map<String, String> legend = service.buildUpstreamParameters(BRUZ,
                params("SERVICE", "WMS", "REQUEST", "GetLegendGraphic", "LAYER", LAYER));

        assertNull(value(describe, "CQL_FILTER"));
        assertNull(value(legend, "CQL_FILTER"));
    }

    @Test
    void wfs_get_feature_is_filtered() throws Exception {
        Map<String, String> result = service.buildUpstreamParameters(BRUZ,
                params("service", "wfs", "request", "GetFeature", "typeNames", LAYER, "outputFormat", "application/json"));

        assertTrue(value(result, "CQL_FILTER").contains("35047"));
        assertEquals("application/json", value(result, "outputFormat"));
    }

    @Test
    void request_is_forwarded_with_technical_account_and_without_cache_headers() throws Exception {
        when(readAccess.getVisibilityFilter()).thenReturn(BRUZ);
        geoserverResponse = ClientResponse.create(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body("png").build();

        try (GeoserverStream stream = service.proxy(false, getMap())) {
            assertEquals(200, stream.getStatus());
            assertEquals("image/png", stream.getContentType());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            stream.writeTo(out);
            assertEquals("png", out.toString(StandardCharsets.UTF_8));
        }

        assertEquals(HttpMethod.GET, sentRequest.method());
        assertEquals("Basic Y2FydG86c2VjcmV0", sentRequest.headers().getFirst(HttpHeaders.AUTHORIZATION));
        assertTrue(sentRequest.url().toString().startsWith("http://geoserver/ows?"));
        assertTrue(sentRequest.url().getRawQuery().contains("CQL_FILTER="));
    }

    @Test
    void query_values_are_strictly_encoded() throws Exception {
        when(readAccess.getVisibilityFilter()).thenReturn(OperationVisibilityFilter.global());
        geoserverResponse = ClientResponse.create(HttpStatus.OK).build();

        service.proxy(false, getMap("CQL_FILTER", "a = 'x+y' AND b = 'é'")).close();

        String query = sentRequest.url().getRawQuery();
        assertTrue(query.contains("%2B"));
        assertFalse(query.contains("+"));
        assertTrue(query.contains("%C3%A9"));
    }

    @Test
    void geoserver_error_status_is_relayed() throws Exception {
        when(readAccess.getVisibilityFilter()).thenReturn(BRUZ);
        geoserverResponse = ClientResponse.create(HttpStatus.BAD_REQUEST).body("<ServiceException/>").build();

        try (GeoserverStream stream = service.proxy(false, getMap())) {
            assertEquals(400, stream.getStatus());
        }
    }

    @Test
    void post_request_is_sent_as_form() throws Exception {
        when(readAccess.getVisibilityFilter()).thenReturn(BRUZ);
        geoserverResponse = ClientResponse.create(HttpStatus.OK).build();

        service.proxy(true, getMap()).close();

        assertEquals(HttpMethod.POST, sentRequest.method());
        assertEquals("http://geoserver/ows", sentRequest.url().toString());
        assertTrue(sentRequest.headers().getFirst(HttpHeaders.AUTHORIZATION).startsWith("Basic "));
    }

    @Test
    void geoserver_credentials_refusal_is_not_relayed_as_user_refusal() {
        when(readAccess.getVisibilityFilter()).thenReturn(BRUZ);
        geoserverResponse = ClientResponse.create(HttpStatus.UNAUTHORIZED).build();

        GeoserverProxyException e = assertThrows(GeoserverProxyException.class, () -> service.proxy(false, getMap()));

        assertEquals(502, e.getHttpStatus());
    }

    @Test
    void unconfigured_proxy_is_unavailable() {
        GeoserverProxyService unconfigured = new GeoserverProxyService(readAccess, "", "", "", Set.of(LAYER),
                WebClient.create());

        GeoserverProxyException e = assertThrows(GeoserverProxyException.class,
                () -> unconfigured.proxy(false, getMap()));

        assertEquals(503, e.getHttpStatus());
        assertNull(sentRequest);
    }
}

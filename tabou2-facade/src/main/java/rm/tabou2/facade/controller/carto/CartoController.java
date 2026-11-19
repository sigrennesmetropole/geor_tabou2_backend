package rm.tabou2.facade.controller.carto;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import rm.tabou2.service.exception.GeoserverProxyException;
import rm.tabou2.service.sig.GeoserverProxyService;
import rm.tabou2.service.sig.GeoserverStream;

/**
 * Proxy OGC de la couche des opérations : la visibilité des opérations en Off dépend de l'utilisateur (US-323).
 */
@RestController
@RequestMapping("/carto/ows")
@RequiredArgsConstructor
public class CartoController {

    private static final int MAX_XML_BYTES = 2 * 1024 * 1024;

    private final GeoserverProxyService geoserverProxyService;

    @GetMapping
    public void get(HttpServletRequest request, HttpServletResponse response)
            throws GeoserverProxyException, IOException {
        relay(false, request, response);
    }

    @PostMapping
    public void post(HttpServletRequest request, HttpServletResponse response)
            throws GeoserverProxyException, IOException {
        String contentType = request.getContentType();
        MediaType mediaType = contentType == null ? null : MediaType.parseMediaType(contentType);
        if (mediaType != null && MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            relay(true, request, response);
        } else if (mediaType != null && (MediaType.APPLICATION_XML.isCompatibleWith(mediaType)
                || MediaType.TEXT_XML.isCompatibleWith(mediaType))) {
            relayXml(request, response);
        } else {
            throw new GeoserverProxyException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "Seuls application/x-www-form-urlencoded et application/xml sont acceptés");
        }
    }

    // Requête WFS GetFeature en XML, comme la table attributaire de MapStore
    private void relayXml(HttpServletRequest request, HttpServletResponse response)
            throws GeoserverProxyException, IOException {
        byte[] body = request.getInputStream().readNBytes(MAX_XML_BYTES + 1);
        if (body.length > MAX_XML_BYTES) {
            throw new GeoserverProxyException(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                    "Requête trop volumineuse");
        }
        try (GeoserverStream stream = geoserverProxyService.proxyXml(body, outputFormat(request))) {
            write(stream, response);
        }
    }

    // Les noms de paramètres OGC sont insensibles à la casse
    private static String outputFormat(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .filter(e -> "outputFormat".equalsIgnoreCase(e.getKey()) && e.getValue().length == 1)
                .map(e -> e.getValue()[0]).findFirst().orElse(null);
    }

    // Le flux est relayé tel quel : pas de ré-encodage des images, ni d'ETag ou d'expiration
    private void relay(boolean post, HttpServletRequest request, HttpServletResponse response)
            throws GeoserverProxyException, IOException {
        try (GeoserverStream stream = geoserverProxyService.proxy(post, request.getParameterMap())) {
            write(stream, response);
        }
    }

    private void write(GeoserverStream stream, HttpServletResponse response) throws IOException {
        response.setStatus(stream.getStatus());
        if (stream.getContentType() != null) {
            response.setHeader(HttpHeaders.CONTENT_TYPE, stream.getContentType());
        }
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, no-store");
        stream.writeTo(response.getOutputStream());
    }
}

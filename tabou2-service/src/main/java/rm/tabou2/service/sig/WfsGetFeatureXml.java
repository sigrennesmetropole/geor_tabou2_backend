package rm.tabou2.service.sig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rm.tabou2.service.exception.GeoserverProxyException;
import rm.tabou2.storage.tabou.item.OperationVisibilityFilter;

/**
 * Contrôle et sécurisation d'une requête WFS GetFeature (1.0 / 1.1) envoyée en XML, comme le fait la table attributaire
 * de MapStore (US-323). Seule la couche des opérations est acceptée ; pour un profil non global, la règle de
 * visibilité est ajoutée en ET au filtre de chaque requête.
 */
final class WfsGetFeatureXml {

    private static final String WFS_NS = "http://www.opengis.net/wfs";

    private static final String OGC_NS = "http://www.opengis.net/ogc";

    private static final String OGC_PREFIX = "ogc:";

    /** Filtres de sélection par identifiant : ils contourneraient la règle de visibilité. */
    private static final Set<String> ID_FILTERS = Set.of("FeatureId", "GmlObjectId", "ResourceId");

    private static final List<String> FORBIDDEN_TOKENS = List.of("querycollection", "querysingle");

    private static final String PROPERTY_MODE = "mode_etape";

    private WfsGetFeatureXml() {
    }

    /**
     * Contrôle la requête et, pour un profil non global, restreint chaque requête à la règle de visibilité.
     *
     * @param body         corps XML reçu
     * @param layers       couches autorisées
     * @param visibility   règle de visibilité de l'utilisateur
     * @param outputFormat paramètre outputFormat de l'URL, ou null
     * @return le XML à envoyer à GeoServer
     * @throws GeoserverProxyException requête refusée (400)
     */
    static byte[] secure(byte[] body, Set<String> layers, OperationVisibilityFilter visibility, String outputFormat)
            throws GeoserverProxyException {
        Document document = parse(body);
        Element root = document.getDocumentElement();
        if (!isElement(root, WFS_NS, "GetFeature")) {
            throw badRequest("Requête WFS non autorisée");
        }
        String service = root.getAttribute("service");
        if (!service.isEmpty() && !"WFS".equalsIgnoreCase(service)) {
            throw badRequest("Requête WFS non autorisée");
        }
        // Résolution de liens distants
        if (root.hasAttribute("resolve")) {
            throw badRequest("Paramètre non autorisé : resolve");
        }
        checkContent(root);

        List<Element> queries = children(root);
        if (queries.isEmpty()) {
            throw badRequest("Requête WFS non autorisée");
        }
        for (Element query : queries) {
            if (!isElement(query, WFS_NS, "Query")) {
                throw badRequest("Requête WFS non autorisée");
            }
            if (!layers.contains(query.getAttribute("typeName").trim())) {
                throw badRequest("Couche non autorisée");
            }
        }
        // Comme GeoServer, le format de sortie de la query string l'emporte sur celui du corps
        boolean changed = outputFormat != null && !outputFormat.isBlank();
        if (changed) {
            root.setAttribute("outputFormat", outputFormat.trim());
        }
        if (visibility.isGlobal()) {
            return changed ? serialize(document) : body;
        }
        for (Element query : queries) {
            restrict(document, query, visibility);
        }
        return serialize(document);
    }

    private static Document parse(byte[] body) throws GeoserverProxyException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(body));
        } catch (SAXException | IOException e) {
            throw new GeoserverProxyException(400, "XML invalide", e);
        } catch (ParserConfigurationException e) {
            throw new GeoserverProxyException(503, "Analyse XML indisponible", e);
        }
    }

    /** Refuse les fonctions de croisement de couches (oracle sur le Off) et les liens distants. */
    private static void checkContent(Node node) throws GeoserverProxyException {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            if ("XlinkPropertyName".equals(node.getLocalName())) {
                throw badRequest("Requête WFS non autorisée");
            }
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
                checkToken(node.getAttributes().item(i).getNodeValue());
            }
        } else if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
            checkToken(node.getNodeValue());
        }
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            checkContent(list.item(i));
        }
    }

    private static void checkToken(String value) throws GeoserverProxyException {
        String lower = value.toLowerCase(Locale.ROOT);
        for (String token : FORBIDDEN_TOKENS) {
            if (lower.contains(token)) {
                throw badRequest("Filtre non autorisé");
            }
        }
    }

    private static void restrict(Document document, Element query, OperationVisibilityFilter visibility)
            throws GeoserverProxyException {
        List<Element> filters = new ArrayList<>();
        for (Element child : children(query)) {
            if (isElement(child, OGC_NS, "Filter")) {
                filters.add(child);
            }
        }
        if (filters.size() > 1) {
            throw badRequest("Filtre invalide");
        }
        Element security = securityFilter(document, visibility);
        if (filters.isEmpty()) {
            Element filter = document.createElementNS(OGC_NS, OGC_PREFIX + "Filter");
            filter.appendChild(security);
            // Dans une requête, le filtre précède le tri
            Element sortBy = children(query).stream().filter(c -> isElement(c, OGC_NS, "SortBy")).findFirst()
                    .orElse(null);
            query.insertBefore(filter, sortBy);
            return;
        }
        Element filter = filters.get(0);
        List<Element> content = children(filter);
        if (content.size() > 1) {
            throw badRequest("Filtre invalide");
        }
        if (content.isEmpty()) {
            filter.appendChild(security);
            return;
        }
        Element user = content.get(0);
        if (OGC_NS.equals(user.getNamespaceURI()) && ID_FILTERS.contains(user.getLocalName())) {
            throw badRequest("Filtre non autorisé");
        }
        // Le filtre de l'utilisateur reste dans sa propre branche : il ne peut pas sortir de la conjonction
        filter.removeChild(user);
        Element and = document.createElementNS(OGC_NS, OGC_PREFIX + "And");
        and.appendChild(user);
        and.appendChild(security);
        filter.appendChild(and);
    }

    /** Équivalent XML de OperationVisibilityFilter#toCql. */
    private static Element securityFilter(Document document, OperationVisibilityFilter visibility) {
        Element or = document.createElementNS(OGC_NS, OGC_PREFIX + "Or");
        Element isNull = document.createElementNS(OGC_NS, OGC_PREFIX + "PropertyIsNull");
        isNull.appendChild(property(document, PROPERTY_MODE));
        or.appendChild(isNull);
        or.appendChild(equalTo(document, PROPERTY_MODE, OperationVisibilityFilter.MODE_PUBLIC));
        if (visibility.getCodeInsee() != null) {
            Element and = document.createElementNS(OGC_NS, OGC_PREFIX + "And");
            and.appendChild(equalTo(document, PROPERTY_MODE, OperationVisibilityFilter.MODE_OFF));
            and.appendChild(equalTo(document, "prospectif", "false"));
            and.appendChild(equalTo(document, "code_insee", visibility.getCodeInsee().toBigInteger().toString()));
            or.appendChild(and);
        }
        return or;
    }

    private static Element equalTo(Document document, String property, String literal) {
        Element equal = document.createElementNS(OGC_NS, OGC_PREFIX + "PropertyIsEqualTo");
        equal.appendChild(property(document, property));
        Element value = document.createElementNS(OGC_NS, OGC_PREFIX + "Literal");
        value.setTextContent(literal);
        equal.appendChild(value);
        return equal;
    }

    private static Element property(Document document, String name) {
        Element property = document.createElementNS(OGC_NS, OGC_PREFIX + "PropertyName");
        property.setTextContent(name);
        return property;
    }

    private static byte[] serialize(Document document) throws GeoserverProxyException {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(document), new StreamResult(out));
            return out.toByteArray();
        } catch (TransformerException e) {
            throw new GeoserverProxyException(503, "Sérialisation XML indisponible", e);
        }
    }

    private static List<Element> children(Element parent) {
        List<Element> result = new ArrayList<>();
        NodeList list = parent.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                result.add((Element) list.item(i));
            }
        }
        return result;
    }

    private static boolean isElement(Element element, String namespace, String localName) {
        return namespace.equals(element.getNamespaceURI()) && localName.equals(element.getLocalName());
    }

    private static GeoserverProxyException badRequest(String message) {
        return new GeoserverProxyException(400, message);
    }
}

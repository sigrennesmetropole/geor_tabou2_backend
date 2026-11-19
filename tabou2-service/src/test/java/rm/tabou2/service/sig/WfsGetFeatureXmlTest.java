package rm.tabou2.service.sig;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.junit.jupiter.api.Test;

import rm.tabou2.service.exception.GeoserverProxyException;
import rm.tabou2.storage.tabou.item.OperationVisibilityFilter;

class WfsGetFeatureXmlTest {

    private static final String LAYER = "tabou:v_oa_operation";

    private static final Set<String> LAYERS = Set.of(LAYER, "tabou:v_oa_operation_2");

    private static final OperationVisibilityFilter BRUZ = OperationVisibilityFilter.commune(new BigDecimal(35047));

    private static final OperationVisibilityFilter GLOBAL = OperationVisibilityFilter.global();

    private static String request(String typeName, String queryContent) {
        return "<wfs:GetFeature service=\"WFS\" version=\"1.1.0\" outputFormat=\"application/json\""
                + " xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:ogc=\"http://www.opengis.net/ogc\">"
                + "<wfs:Query typeName=\"" + typeName + "\">" + queryContent + "</wfs:Query></wfs:GetFeature>";
    }

    private static String userFilter() {
        return "<ogc:Filter><ogc:PropertyIsEqualTo><ogc:PropertyName>nature</ogc:PropertyName>"
                + "<ogc:Literal>ZAC</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>";
    }

    private static String secure(String xml, OperationVisibilityFilter visibility) throws GeoserverProxyException {
        return secure(xml, visibility, null);
    }

    private static String secure(String xml, OperationVisibilityFilter visibility, String outputFormat)
            throws GeoserverProxyException {
        return new String(
                WfsGetFeatureXml.secure(xml.getBytes(StandardCharsets.UTF_8), LAYERS, visibility, outputFormat),
                StandardCharsets.UTF_8);
    }

    private static void assertRefused(String xml, OperationVisibilityFilter visibility) {
        GeoserverProxyException e = assertThrows(GeoserverProxyException.class, () -> secure(xml, visibility));
        assertEquals(400, e.getHttpStatus());
    }

    @Test
    void global_profile_request_is_relayed_unchanged() throws Exception {
        byte[] body = request(LAYER, userFilter()).getBytes(StandardCharsets.UTF_8);

        assertArrayEquals(body, WfsGetFeatureXml.secure(body, LAYERS, GLOBAL, null));
    }

    @Test
    void every_configured_layer_is_accepted() throws Exception {
        assertTrue(secure(request("tabou:v_oa_operation_2", userFilter()), BRUZ).contains("35047"));
    }

    @Test
    void output_format_of_the_url_replaces_the_one_of_the_body() throws Exception {
        String xml = request(LAYER, userFilter());

        assertTrue(secure(xml, GLOBAL, "json").contains("outputFormat=\"json\""));
        assertTrue(secure(xml, BRUZ, "json").contains("outputFormat=\"json\""));
        assertTrue(!secure(xml, BRUZ, "json").contains("application/json"));
    }

    @Test
    void commune_profile_keeps_user_filter_in_a_conjunction_with_the_security_filter() throws Exception {
        String result = secure(request(LAYER, userFilter()), BRUZ);

        assertTrue(result.contains("<ogc:And><ogc:PropertyIsEqualTo><ogc:PropertyName>nature"), result);
        assertTrue(result.contains("<ogc:PropertyName>mode_etape</ogc:PropertyName>"), result);
        assertTrue(result.contains("<ogc:Literal>35047</ogc:Literal>"), result);
        assertTrue(result.contains("<ogc:Literal>OFF</ogc:Literal>"), result);
    }

    @Test
    void public_only_profile_has_no_commune_branch() throws Exception {
        String result = secure(request(LAYER, userFilter()), OperationVisibilityFilter.publicOnly());

        assertTrue(result.contains("<ogc:Literal>PUBLIC</ogc:Literal>"), result);
        assertTrue(!result.contains("code_insee"), result);
    }

    @Test
    void missing_filter_is_added_before_sort() throws Exception {
        String sort = "<ogc:SortBy><ogc:SortProperty><ogc:PropertyName>nom</ogc:PropertyName>"
                + "</ogc:SortProperty></ogc:SortBy>";

        String result = secure(request(LAYER, sort), BRUZ);

        assertTrue(result.indexOf("<ogc:Filter>") > 0, result);
        assertTrue(result.indexOf("<ogc:Filter>") < result.indexOf("<ogc:SortBy>"), result);
    }

    @Test
    void empty_filter_is_replaced_by_security_filter() throws Exception {
        String result = secure(request(LAYER, "<ogc:Filter/>"), BRUZ);

        assertTrue(result.contains("<ogc:Or>"), result);
        assertTrue(!result.contains("<ogc:And><ogc:Or>"), result);
    }

    @Test
    void other_layer_is_refused() {
        assertRefused(request("tabou:v_oa_programme", userFilter()), BRUZ);
        assertRefused(request(LAYER + ",tabou:v_oa_programme", userFilter()), GLOBAL);
    }

    @Test
    void feature_id_filter_is_refused_for_commune_profile() {
        String ids = "<ogc:Filter><ogc:FeatureId fid=\"v_oa_operation.1\"/></ogc:Filter>";

        assertRefused(request(LAYER, ids), BRUZ);
    }

    @Test
    void cross_layer_functions_are_refused_for_every_profile() {
        String function = "<ogc:Filter><ogc:Intersects><ogc:PropertyName>shape</ogc:PropertyName>"
                + "<ogc:Function name=\"collectGeometries\"><ogc:Function name=\"queryCollection\">"
                + "<ogc:Literal>tabou:v_oa_secteur</ogc:Literal></ogc:Function></ogc:Function>"
                + "</ogc:Intersects></ogc:Filter>";

        assertRefused(request(LAYER, function), BRUZ);
        assertRefused(request(LAYER, function), GLOBAL);
    }

    @Test
    void doctype_is_refused() {
        String xml = "<!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]>" + request(LAYER, userFilter());

        assertRefused(xml, GLOBAL);
    }

    @Test
    void other_requests_are_refused() {
        assertRefused("<wfs:Transaction xmlns:wfs=\"http://www.opengis.net/wfs\"/>", GLOBAL);
        assertRefused("<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs/2.0\"/>", GLOBAL);
        assertRefused("<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\"/>", GLOBAL);
        assertRefused("not xml", GLOBAL);
    }

    @Test
    void several_filters_in_a_query_are_refused() {
        assertRefused(request(LAYER, userFilter() + userFilter()), BRUZ);
    }
}

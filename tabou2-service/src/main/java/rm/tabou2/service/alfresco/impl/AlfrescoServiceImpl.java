package rm.tabou2.service.alfresco.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.helper.AlfrescoAuthenticationHelper;

@Service
public class AlfrescoServiceImpl implements AlfrescoService {

    private static final String AUTHORIZATION = "Authorization";

    private static final String DOCUMENT_START_URI = "alfresco/versions/1/nodes/";

    @Autowired
    private AlfrescoAuthenticationHelper alfrescoAuthenticationHelper;

    @Override
    public AlfrescoDocument getDocumentMetadata(String documentId) {

        //Construction de l'uri du document
        UriComponentsBuilder documentUri = UriComponentsBuilder
                .fromUriString(DOCUMENT_START_URI)
                .path(documentId);

        return alfrescoAuthenticationHelper.getAlfrescoWebClient().get()
                .uri(documentUri.toUriString())
                .header(AUTHORIZATION, "Basic " + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

    }


}

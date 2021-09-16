package rm.tabou2.service.alfresco.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public AlfrescoDocument getDocument(String documentId) {

        //Construction de l'uri du document
        String documentUri = DOCUMENT_START_URI + documentId;


        return alfrescoAuthenticationHelper.getAlfrescoWebClient().get()
                .uri(documentUri)
                .header(AUTHORIZATION, "Basic " + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

    }


}

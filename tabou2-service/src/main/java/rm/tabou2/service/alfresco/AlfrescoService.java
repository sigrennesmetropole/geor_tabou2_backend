package rm.tabou2.service.alfresco;

import rm.tabou2.service.alfresco.dto.AlfrescoDocument;

public interface AlfrescoService {


    /**
     * Récupération d'un document à partir de son identifiant.
     *
     * @param documentId
     * @return document
     */
    AlfrescoDocument getDocument(String documentId);

}

package rm.tabou2.service.alfresco;

import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.st.generator.model.DocumentContent;

public interface AlfrescoService {


    /**
     * Récupération d'un document à partir de son identifiant.
     *
     * @param documentId
     * @return document
     */
    AlfrescoDocument getDocumentMetadata(String documentId);

    /**
     * Téléchargement du contenu d'un document.
     *
     * @param documentId identifiant du document
     * @return document
     */
    DocumentContent downloadDocument(String documentId);
}

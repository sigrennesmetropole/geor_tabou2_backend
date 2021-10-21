package rm.tabou2.service.alfresco;

import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.exception.AppServiceException;
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
     * @param objectType type de l'objet tabou
     * @param objectId identifiant de l'objet tabou associé au document
     * @param documentId  identifiant du document
     * @return document
     * @throws AppServiceException
     */
    DocumentContent downloadDocument(AlfrescoTabouType objectType, long objectId, String documentId) throws AppServiceException;

    /**
     * Suppresion d'un document dans Alfresco.
     *
     * @param documentId identifiant du document
     */
    void deleteDocument(String documentId);

}

package rm.tabou2.service.alfresco;

import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;

public interface AlfrescoService {


    /**
     * Récupération d'un document à partir de son identifiant.
     *
     * @param documentId identifiant du document
     * @return document alfresco document
     */
    AlfrescoDocument getDocumentMetadata(String documentId);

    /**
     * Téléchargement du contenu d'un document.
     *
     * @param objectType type de l'objet tabou
     * @param objectId identifiant de l'objet tabou associé au document
     * @param documentId  identifiant du document
     * @return document
     * @throws AppServiceException exception
     */
    DocumentContent downloadDocument(AlfrescoTabouType objectType, long objectId, String documentId) throws AppServiceException;

    /**
     * Mise à jour du contenu d'un document.
     *
     * @param documentId identifiant du document
     * @param file fichier
     * @throws AppServiceException
     */
    void updateDocumentContent(AlfrescoTabouType objectType, long objectId, String documentId, MultipartFile file) throws AppServiceException;

    /**
     * Suppresion d'un document dans Alfresco.
     *
     * @param objectType type de l'objet tabou
     * @param objectId identifiant de l'objet tabou
     * @param documentId identifiant du document
     */
    void deleteDocument(AlfrescoTabouType objectType, long objectId, String documentId);

}

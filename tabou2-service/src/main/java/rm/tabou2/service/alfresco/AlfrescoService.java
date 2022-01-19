package rm.tabou2.service.alfresco;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoDocumentRoot;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.dto.DocumentMetadata;
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
     * @throws AppServiceException exception si erreur lors du téléchargement du document
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

    /**
     * Ajout d'un document dans Alfresco.
     *
     * @param nom nom du document
     * @param libelle libellé du type de document
     * @param objectType type de l'objet tabou
     * @param objectId identifiant de l'objet tabou
     * @param file fichier
     * @return document
     * @throws AppServiceException erreur lors de l'ajout d'un document dans tabou
     */
    AlfrescoDocument addDocument(String nom, String libelle, AlfrescoTabouType objectType, long objectId, MultipartFile file) throws AppServiceException;

    /**
     * Mise à jour des métadonnées d'un document.
     *
     * @param objectType type de l'objet tabou
     * @param objectId identifiant de l'objet tabou
     * @param documentId identifiant du document
     * @param documentMetadata metadonnées d'un document
     * @param isNewDocument true si le document est nouveau, false sinon
     * @return document
     */
    AlfrescoDocument updateDocumentMetadata(AlfrescoTabouType objectType, long objectId, String documentId, DocumentMetadata documentMetadata, boolean isNewDocument);



    /**
     * Recherche de documents.
     *
     * @param objectType type de l'objet tabou
     * @param objectId identifant de l'objet tabou
     * @param nom nom du document
     * @param libelle libellé du type de document
     * @param typeMime type MIME du document
     * @param pageable paramètres de pagination
     */
    AlfrescoDocumentRoot searchDocuments(AlfrescoTabouType objectType, long objectId, String nom, String libelle, String typeMime, Pageable pageable);
}

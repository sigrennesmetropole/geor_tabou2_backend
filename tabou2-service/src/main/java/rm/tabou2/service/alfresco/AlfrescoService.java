package rm.tabou2.service.alfresco;

import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouObjet;
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
     * @param objet type de l'objet tabou
     * @param objetId identifiant de l'objet tabou associé au document
     * @param documentId  identifiant du document
     * @return document
     * @throws AppServiceException
     */
    DocumentContent downloadDocument(AlfrescoTabouObjet objet, long objetId, String documentId) throws AppServiceException;
}

package rm.tabou2.service.st.generator;


import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.GenerationModel;

import java.io.File;

public interface DocumentGenerator {

    /**
     * Génération du document
     *
     * @param generationModel modèle d'export
     * @return document
     * @throws AppServiceException erreur lors de l'export
     */
    DocumentContent generateDocument(GenerationModel generationModel) throws AppServiceException;

    /**
     * Génération de l'image du document.
     *
     * @param tabouType type d'objet
     * @param objectId  id de l'objet
     * @return Image
     * @throws AppServiceException erreur lors de la génération
     */
    File generatedImgForTemplate(AlfrescoTabouType tabouType, long objectId) throws AppServiceException;
}

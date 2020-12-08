package rm.tabou2.service.st.generator;


import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.GenerationModel;

public interface DocumentGenerator {

    /**
     * Génération du document
     * @param generationModel       modèle d'export
     * @return                      document
     * @throws AppServiceException  erreur lors de l'export
     */
    DocumentContent generateDocument(GenerationModel generationModel) throws AppServiceException;
}

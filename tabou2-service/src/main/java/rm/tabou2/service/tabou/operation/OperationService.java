package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.validator.evenement.ValidEvenementCreation;
import rm.tabou2.service.validator.evenement.ValidEvenementUpdate;
import rm.tabou2.service.validator.operation.ValidOperationCreation;
import rm.tabou2.service.validator.operation.ValidOperationUpdate;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.List;


public interface OperationService {

    /**
     * Création d'une opération
     *
     * @param operation opération à créer
     * @return opération crée
     */
    Operation createOperation(@ValidOperationCreation Operation operation);

    /**
     * Mise à jour d'une opération
     *
     * @param operation opération à modifier
     * @return opération modifiée
     */
    Operation updateOperation(@ValidOperationUpdate Operation operation);

    /**
     * Récupération d'une opération
     *
     * @param operationId identifiant de l'opération
     * @return opération
     */
    Operation getOperationById(long operationId);

    /**
     * Modification de l'étape d'une opération
     *
     * @param operationId Identifiant de l'opération
     * @param etapeId     identifiant de l'étape
     * @return opération modifiée
     * @throws AppServiceException Erreur lors de la mise à jour d'une opération
     */
    Operation updateEtapeOfOperationId(long operationId, long etapeId) throws AppServiceException;

    /**
     * Recherche des opérations à partir des paramètres.
     *
     * @param operationsCriteria paramètres des opérations
     * @param pageable           paramètre lié à la pagination
     * @return Liste des opérations correspondantes à la recherche
     */
    Page<Operation> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable);

    /**
     * Récupération de la liste des évènements d'une opération
     *
     * @param operationId identifiant de l'opération
     * @return liste des évènements
     * @throws AppServiceException erreur lors de la récupération des évènements
     */
    List<Evenement> getEvenementsByOperationId(Long operationId);

    /**
     * Ajout d'un évènement pour une opération
     *
     * @param operationId identifiant de l'opération
     * @param evenement   evenement
     * @return evenement crée
     * @throws AppServiceException erreur lors de l'enregistrement de l'opération
     */
    Evenement addEvenementByOperationId(Long operationId, @ValidEvenementCreation Evenement evenement) throws AppServiceException;

    /**
     * Modification d'un événement d'une opération
     *
     * @param idOperation identifiant de l'opération
     * @param evenement   événement à modifier
     * @return événement modifié
     * @throws AppServiceException erreur lors de la mise à jour de l'événement
     */
    Evenement updateEvenementByOperationId(long idOperation, @ValidEvenementUpdate Evenement evenement) throws AppServiceException;

    /**
     * Récupération des métadonnées d'un document Alfresco d'une opération.
     *
     * @param operationId identifiant de l'opération
     * @param documentId  identifiant du document
     * @return document
     */
    DocumentMetadata getDocumentMetadata(long operationId, String documentId) throws AppServiceException ;

    /**
     * Télécharge le contenu d'un document d'une opération.
     *
     * @param operationId identifiant de l'opération
     * @param documentId identifiant du document
     * @return contenu du document
     * @throws AppServiceException erreur lors de la récupération du contenu d'un document
     */
    DocumentContent downloadDocument(long operationId, String documentId) throws AppServiceException;

    /**
     * Mise à jour du contenu d'un document.
     *
     * @param operationId identifiant d'une opération
     * @param documentId identifiant du document
     * @param file fichier à mettre à jour
     * @throws AppServiceException erreur lors de la mise à jour du contenu d'un document
     */
    void updateDocumentContent(long operationId, String documentId, MultipartFile file) throws AppServiceException;

    /**
     * Ajout d'un document.
     *
     * @param operationId identifiant de l'opération
     * @param nom nom du document
     * @param libelle libellé du type de document
     * @param file document à ajouter
     * @return métadonnées du document
     * @throws AppServiceException erreur lors de l'ajout d'un document
     */
    DocumentMetadata addDocument(long operationId, String nom, String libelle, MultipartFile file) throws AppServiceException;
}

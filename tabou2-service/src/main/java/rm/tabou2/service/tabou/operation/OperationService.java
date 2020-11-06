package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.validator.ValidEvenementCreation;
import rm.tabou2.service.validator.ValidEvenementUpdate;
import rm.tabou2.service.validator.ValidOperationCreation;
import rm.tabou2.service.validator.ValidOperationUpdate;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.List;


public interface OperationService {

    /**
     * Création d'une opération
     * @param operation opération à créer
     * @return opération crée
     */
    Operation createOperation(@ValidOperationCreation Operation operation);

    /**
     * Mise à jour d'une opération
     * @param operation opération à modifier
     * @return opération modifiée
     */
    Operation updateOperation(@ValidOperationUpdate Operation operation);

    /**
     * Récupération d'une opération
     * @param operationId identifiant de l'opération
     * @return opération
     */
    Operation getOperationById(long operationId);

    /**
     * Modification de l'étape d'une opération
     * @param operationId Identifiant de l'opération
     * @param etapeId identifiant de l'étape
     * @return opération modifiée
     */
    Operation updateEtapeOfOperationId (long operationId, long etapeId);

    /**
     * Recherche des opérations à partir des paramètres.
     *
     * @param operationsCriteria paramètres des opérations
     * @param pageable paramètre lié à la pagination
     * @return Liste des opérations correspondantes à la recherche
     */
    Page<Operation> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable);

    /**
     * Récupération de la liste des évènements d'une opération
     * @param operationId               identifiant de l'opération
     * @return                          liste des évènements
     * @throws AppServiceException      erreur lors de la récupération des évènements
     */
    List<Evenement> getEvenementsByOperationId(Long operationId);

    /**
     * Ajout d'un évènement système pour une opération
     * @param operationId           identifiant de l'opération
     * @param evenement             evenement
     * @return                      evenement crée
     * @throws AppServiceException  erreur lors de l'enregistrement de l'opération
     */
    Evenement addEvenementSystemeByOperationId(Long operationId, @ValidEvenementCreation Evenement evenement) throws AppServiceException;

    /**
     * Ajout d'un évènement non système pour une opération
     * @param operationId           identifiant de l'opération
     * @param evenement             evenement
     * @return                      evenement crée
     * @throws AppServiceException  erreur lors de l'enregistrement de l'opération
     */
    Evenement addEvenementNonSystemeByOperationId(Long operationId, @ValidEvenementCreation Evenement evenement) throws AppServiceException;

    /**
     * Modification d'un événement d'une opération
     * @param idOperation           identifiant de l'opération
     * @param evenement             événement à modifier
     * @return                      événement modifié
     * @throws AppServiceException  erreur lors de la mise à jour de l'événement
     */
    Evenement updateEvenementByOperationId(long idOperation, @ValidEvenementUpdate Evenement evenement) throws AppServiceException;
}

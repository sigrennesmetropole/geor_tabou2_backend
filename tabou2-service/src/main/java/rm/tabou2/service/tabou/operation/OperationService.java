package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.validator.ValidOperationCreation;
import rm.tabou2.service.validator.ValidOperationUpdate;
import rm.tabou2.storage.tabou.item.OperationsCriteria;


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
}

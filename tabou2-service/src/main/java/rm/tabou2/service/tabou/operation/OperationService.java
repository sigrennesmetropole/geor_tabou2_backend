package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.List;


public interface OperationService {

    /**
     * Création d'une opération
     * @param operation opération à créer
     * @return opération crée
     */
    Operation createOperation(Operation operation);

    /**
     * Mise à jour d'une opération
     * @param operation opération à modifier
     * @return opération modifiée
     */
    Operation updateOperation(Operation operation);

    /**
     * Récupération d'une opération
     * @param operationId identifiant de l'opération
     * @return opération
     */
    Operation getOperationById(long operationId);

    /**
     * Récupération de la liste des étapes possibles pour un programme
     * @param operationId identifiant de l'opération
     * @return liste des étapes
     */
    List<Etape> getEtapesForOperation(long operationId);

    /**
     * Recherche des opérations à partir des paramètres.
     *
     * @param operationsCriteria paramètres des opérations
     * @param pageable paramètre lié à la pagination
     * @return Liste des opérations correspondantes à la recherche
     */
    Page<Operation> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable);
}

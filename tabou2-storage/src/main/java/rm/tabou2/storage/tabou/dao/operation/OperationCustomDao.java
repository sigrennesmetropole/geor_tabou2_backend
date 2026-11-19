package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationVisibilityFilter;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

public interface OperationCustomDao {

    /**
     * Recherche paginée. La règle de visibilité est appliquée à la recherche et au comptage.
     *
     * @param operationsCriteria critères de recherche
     * @param visibilityFilter règle de visibilité de l'utilisateur connecté
     * @param pageable pagination
     * @return la page d'opérations visibles
     */
    Page<OperationEntity> searchOperations(OperationsCriteria operationsCriteria,
                                           OperationVisibilityFilter visibilityFilter, Pageable pageable);

    /**
     * @param operationId identifiant de l'opération
     * @param filter règle de visibilité
     * @return true si l'opération existe et n'est pas visible selon la règle ; false si elle est visible ou inexistante
     */
    boolean isHidden(long operationId, OperationVisibilityFilter filter);

}

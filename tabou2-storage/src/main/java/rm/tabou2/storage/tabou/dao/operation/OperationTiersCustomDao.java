package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;

public interface OperationTiersCustomDao {

    /**
     * Recherche les tiers des opération à partir de paramètres.
     *
     * @param operationId identifiant de l'opération
     * @param libelleType libellé du type de tiers
     * @param pageable    paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<OperationTiersEntity> searchOperationTiers(String libelleType, Long operationId, Pageable pageable);

}

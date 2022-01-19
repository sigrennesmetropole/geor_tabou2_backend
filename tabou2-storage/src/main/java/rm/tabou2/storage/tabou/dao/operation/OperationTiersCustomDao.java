package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

public interface OperationTiersCustomDao {

    /**
     * Recherche les tiers des opération à partir de paramètres.
     *
     * @param criteria criteres de recherches
     * @param pageable    paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<OperationTiersEntity> searchOperationTiers(TiersAmenagementCriteria criteria, Pageable pageable);

}

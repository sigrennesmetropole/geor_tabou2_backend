package rm.tabou2.storage.tabou.dao.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.TiersCriteria;

public interface TiersCustomDao {

    /**
     * Recherche de tiers à partir de paramètres.
     * @param tiersCriteria paramètres* de recherche
     * @param pageable paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<TiersEntity> searchTiers(TiersCriteria tiersCriteria, Pageable pageable);

}



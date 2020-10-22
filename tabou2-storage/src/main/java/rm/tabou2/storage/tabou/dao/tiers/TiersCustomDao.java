package rm.tabou2.storage.tabou.dao.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.TiersCriteria;

public interface TiersCustomDao {

    Page<TiersEntity> searchTiers(TiersCriteria tiersCriteria, Pageable pageable);

}



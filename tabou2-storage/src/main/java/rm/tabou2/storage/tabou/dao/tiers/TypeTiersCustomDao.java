package rm.tabou2.storage.tabou.dao.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

public interface TypeTiersCustomDao {

    Page<TypeTiersEntity> searchTypeTiers(String libelle, Boolean inactif, Pageable pageable);

}

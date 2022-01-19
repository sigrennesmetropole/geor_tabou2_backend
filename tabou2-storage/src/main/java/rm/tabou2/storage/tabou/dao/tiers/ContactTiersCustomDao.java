package rm.tabou2.storage.tabou.dao.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.tiers.ContactTiersEntity;
import rm.tabou2.storage.tabou.item.ContactTiersCriteria;

public interface ContactTiersCustomDao {

    /**
     * Recherche de contacts tiers à partir de paramètres.
     * @param contactTiersCriteria paramètres* de recherche
     * @param pageable paramètres de pagination
     * @return ContactTiers correspondants à la recherche
     */
    Page<ContactTiersEntity> searchContactTiers(ContactTiersCriteria contactTiersCriteria, Pageable pageable);
}

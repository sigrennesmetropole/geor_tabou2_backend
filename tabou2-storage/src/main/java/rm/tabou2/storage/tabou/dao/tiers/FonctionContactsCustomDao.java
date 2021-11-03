package rm.tabou2.storage.tabou.dao.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.tiers.FonctionContactsEntity;
import rm.tabou2.storage.tabou.item.FonctionContactsCriteria;

public interface FonctionContactsCustomDao {

    /**
     * Recherche de fonctions de contacts à partir de paramètres.
     * @param fonctionContactsCriteria paramètres* de recherche
     * @param pageable paramètres de pagination
     * @return FonctionContacts correspondants à la recherche
     */
    Page<FonctionContactsEntity> searchTiers(FonctionContactsCriteria fonctionContactsCriteria, Pageable pageable);
}

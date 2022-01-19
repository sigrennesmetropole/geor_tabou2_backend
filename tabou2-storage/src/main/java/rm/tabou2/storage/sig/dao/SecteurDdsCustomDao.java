package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.sig.entity.SecteurDdsEntity;

public interface SecteurDdsCustomDao {

    /**
     * Recherche de secteurs DDS à partir de paramètres.
     *
     * @param secteur  nom de secteur
     * @param pageable paramètre lié à la pagination
     * @return secteurDds
     */
    Page<SecteurDdsEntity> searchSecteursDds(String secteur, Pageable pageable);

}

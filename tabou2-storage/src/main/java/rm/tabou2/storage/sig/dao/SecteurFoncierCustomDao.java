package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.SecteurFoncierEntity;

public interface SecteurFoncierCustomDao {

    /**
     * Recherche de secteurs foncier à partir de paramètres.
     *
     * @param negociateur nom du négociateur foncier
     * @param pageable    paramètre lié à la pagination
     * @return secteur foncier
     */
    Page<SecteurFoncierEntity> searchSecteursFoncier(String negociateur, Pageable pageable);

}

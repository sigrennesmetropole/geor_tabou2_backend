package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.QuartierEntity;
import rm.tabou2.storage.sig.item.QuartierCriteria;

public interface QuartierCustomDao {

    /**
     * Recherche de quartiers à partir des paramètres.
     *
     * @param quartierCriteria paramètres des quartiers
     * @param pageable         paramètre lié à la pagination
     * @return liste de quartiers correspondants à la recherche
     */
    Page<QuartierEntity> searchQuartiers(QuartierCriteria quartierCriteria, Pageable pageable);

}

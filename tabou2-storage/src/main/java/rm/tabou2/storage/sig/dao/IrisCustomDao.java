package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.IrisEntity;
import rm.tabou2.storage.sig.item.IrisCriteria;

public interface IrisCustomDao {

    /**
     * Recherche d'un iris à partir des paramètres.
     *
     * @param irisCriteria paramètres de recherche des iris
     * @param pageable     paramètre lié à la pagination
     * @return liste des iris correspondants à la recherche
     */
    Page<IrisEntity> searchIris(IrisCriteria irisCriteria, Pageable pageable);

}

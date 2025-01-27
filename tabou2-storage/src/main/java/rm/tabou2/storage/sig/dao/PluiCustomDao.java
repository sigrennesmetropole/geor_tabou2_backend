package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.PluiEntity;

public interface PluiCustomDao {

    /**
     * Recherche de zonage Plui.
     *
     * @param libelle  libellé du Plui
     * @param pageable paramètre lié à la pagination
     * @return liste des zonage Plui correspondants à la recherche
     */
    Page<PluiEntity> searchPluis(String libelle, Pageable pageable);

}

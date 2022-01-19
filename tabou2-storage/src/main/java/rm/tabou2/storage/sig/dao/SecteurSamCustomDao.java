package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.SecteurSamEntity;

public interface SecteurSamCustomDao {

    /**
     * Recherche de secteurs Sam à partir de paramètres.
     *
     * @param secteur  nom de secteur
     * @param pageable paramètre lié à la pagination
     * @return liste de secteurs Sam
     */
    Page<SecteurSamEntity> searchSecteursSam(String secteur, Pageable pageable);

}

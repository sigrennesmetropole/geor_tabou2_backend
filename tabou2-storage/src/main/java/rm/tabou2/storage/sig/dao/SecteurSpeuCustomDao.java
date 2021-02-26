package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

public interface SecteurSpeuCustomDao {

    /**
     * Recherche de secteurs Speu à partir de paramètres.
     *
     * @param numSecteur numéro de secteur
     * @param nomSecteur nom de secteur
     * @param pageable
     * @return liste de secteur Speu
     */
    Page<SecteurSpeuEntity> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable);

}

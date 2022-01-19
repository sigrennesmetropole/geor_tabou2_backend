package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurSpeu;

public interface SecteurSpeuService {

    /**
     * Recherche de secteurs Speu à partir de paramètres.
     *
     * @param numSecteur numéro de secteur
     * @param nomSecteur nom de secteur
     * @param pageable
     * @return liste de secteur Speu
     */
    Page<SecteurSpeu> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable);

}

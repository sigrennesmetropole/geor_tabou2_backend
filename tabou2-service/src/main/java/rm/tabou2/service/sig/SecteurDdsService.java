package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurDds;

public interface SecteurDdsService {

    /**
     * Recherche de secteurs DDS à partir de paramètres.
     *
     * @param secteur  nom de secteur
     * @param pageable paramètre lié à la pagination
     * @return secteurDds
     */
    Page<SecteurDds> searchSecteursDds(String secteur, Pageable pageable);

}

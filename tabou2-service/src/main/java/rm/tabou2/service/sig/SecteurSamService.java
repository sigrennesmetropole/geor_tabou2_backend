package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurSam;

public interface SecteurSamService {

    /**
     * Recherche de secteurs Sam à partir de paramètres.
     *
     * @param secteur  nom de secteur
     * @param pageable paramètre lié à la pagination
     * @return liste de secteurs Sam
     */
    Page<SecteurSam> searchSecteursSam(String secteur, Pageable pageable);

}

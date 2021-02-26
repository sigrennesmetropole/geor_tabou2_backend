package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurFoncier;

public interface SecteurFoncierService {

    /**
     * Recherche de secteurs foncier à partir de paramètres.
     *
     * @param negociateur nom du négociateur foncier
     * @param pageable    paramètre lié à la pagination
     * @return secteur foncier
     */
    Page<SecteurFoncier> searchSecteursFonciers(String negociateur, Pageable pageable);


}

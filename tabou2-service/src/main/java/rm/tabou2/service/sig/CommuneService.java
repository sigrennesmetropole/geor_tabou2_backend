package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Commune;


public interface CommuneService {

    /**
     * Récupération d'une commune par son identifiant.
     *
     * @param communeId identifiant de la commune
     * @return commune
     */
    Commune getCommuneById(int communeId);

    /**
     * Recherche de communes.
     * @param nom nom de la commune
     * @param codeInsee code insee de la commune
     * @param pageable paramètre lié à la pagination
     * @return liste des communes correspondants à la recherche
     */
    Page<Commune> searchCommunes(String nom, String codeInsee, Pageable pageable);

}

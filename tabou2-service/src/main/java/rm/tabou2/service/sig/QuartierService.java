package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.storage.sig.item.QuartierCriteria;

public interface QuartierService {

    /**
     * Recherche de quartiers à partir des paramètres.
     *
     * @param quartierCriteria paramètres des quartiers
     * @param pageable         paramètre lié à la pagination
     * @return liste de quartiers correspondants à la recherche
     */
    Page<Quartier> searchQuartiers(QuartierCriteria quartierCriteria, Pageable pageable);

    /**
     * Récupération d'un quartier à partir de son identifiant.
     *
     * @param quartierId identifiant du quartier
     * @return quartier
     */
    Quartier getQuartierById(int quartierId);
}

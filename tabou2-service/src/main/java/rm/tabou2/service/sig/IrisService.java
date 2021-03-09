package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.storage.sig.item.IrisCriteria;

/**
 * Interface du service des IRIS
 */
public interface IrisService {

    /**
     * Recherche d'un iris à partir des paramètres.
     *
     * @param irisCriteria paramètres de recherche des iris
     * @param pageable     paramètre lié à la pagination
     * @return liste des iris correspondants à la recherche
     */
    Page<Iris> searchIris(IrisCriteria irisCriteria, Pageable pageable);

    /**
     * Récupération d'un iris par son identifiant.*
     *
     * @param irisId identifiant de l'iris
     * @return iris
     */
    Iris getIrisById(int irisId);
}

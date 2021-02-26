package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.PluiZonage;

public interface PluiService {

    /**
     * Recherche de zonage Plui.
     *
     * @param libelle  libellé du Plui
     * @param pageable paramètre lié à la pagination
     * @return liste des zonage Plui correspondants à la recherche
     */
    Page<PluiZonage> searchPlui(String libelle, Pageable pageable);

    /**
     * Récupération d'un Zonage Plui à partir de son identifiant.
     *
     * @param pluiId identifiant du zonage Plui
     * @return Zonage Plui
     */
    PluiZonage getPluiById(int pluiId);
}

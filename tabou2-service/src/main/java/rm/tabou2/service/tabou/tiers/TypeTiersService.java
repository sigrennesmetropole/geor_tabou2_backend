package rm.tabou2.service.tabou.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeTiers;

public interface TypeTiersService {

    /**
     * Récupération d'un type tiers.
     *
     * @param typeTiersId identifiant du type tiers
     * @return typeTiers
     */
    TypeTiers getById(long typeTiersId);

    /**
     * Ajout d'un type tiers.
     *
     * @param typeTiers type tiers à ajouter
     * @return nouveau type tiers
     */
    TypeTiers createTypeTiers(TypeTiers typeTiers);


    /**
     * Modification d'un type tiers.
     *
     * @param typeTiers type tiers à modifier
     * @return type tiers modifié
     */
    TypeTiers updateTypeTiers(TypeTiers typeTiers);

    /**
     * Désactivation d'un type tiers.
     *
     * @param typeTiersId identifiant du type tiers à désactiver
     */
    TypeTiers inactivateTypeTiers(Long typeTiersId);

    /**
     * Recherche de type tiers.
     *
     * @param libelle  libelle du type tiers à rechercher
     * @param inactif  type tiers inactivé ou non
     * @param pageable pagination           true si tri ascendant, faux sinon
     * @return liste de tiers
     */
    Page<TypeTiers> searchTypeTiers(String libelle, Boolean inactif, Pageable pageable);
}

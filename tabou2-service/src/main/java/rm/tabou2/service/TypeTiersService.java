package rm.tabou2.service;

import rm.tabou2.service.dto.TypeTiers;

import java.util.List;

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
    TypeTiers addTypeTiers(TypeTiers typeTiers);

    /**
     * Modification d'un type tiers.
     *
     * @param typeTiers type tiers à modifier
     * @return type tiers modifié
     */
    TypeTiers editTypeTiers(TypeTiers typeTiers);

    /**
     * Désactivation d'un type tiers.
     *
     * @param typeTiersId identifiant du type tiers à désactiver
     */
    TypeTiers inactivateTypeTiers(Long typeTiersId);

    /**
     * Recherche de type tiers.
     *
     * @param keyword       mot clé à rechercher
     * @param start         index du début
     * @param resultsNumber nombre de résultats à retourner
     * @param orderBy       nom du champ sur lequel trier
     * @param asc           true si tri ascendant, faux sinon
     * @return
     */
    List<TypeTiers> searchTypeTiers(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc);
}

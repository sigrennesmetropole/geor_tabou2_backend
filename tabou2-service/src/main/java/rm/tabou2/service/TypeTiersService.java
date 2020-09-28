package rm.tabou2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeTiers;

import java.util.Date;

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
     * @param libelle        libelle du type tiers à rechercher
     * @param dateInactivite date de mise en inactivité à rechercher
     * @param pageable       pagination           true si tri ascendant, faux sinon
     * @return
     */
    Page<TypeTiers> searchTypeTiers(String libelle, Date dateInactivite, Pageable pageable);
}

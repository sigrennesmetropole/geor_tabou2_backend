package rm.tabou2.service.tabou.logement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeLogement;

public interface TypeLogementService {

    /**
     * Récupération d'un type de logement.
     *
     * @param typeLogementId identifiant du type de logement
     * @return type de logement
     */
    TypeLogement getById(long typeLogementId);

    /**
     * Ajout d'un type de logement.
     *
     * @param typeLogement type de logement à ajouter
     * @return nouveau type de logement
     */
    TypeLogement createTypeLogement(TypeLogement typeLogement);

    /**
     * Modification d'un type de logement.
     *
     * @param typeLogement type de logement à modifier
     * @return type de logement modifié
     */
    TypeLogement updateTypeLogement(TypeLogement typeLogement);

    /**
     * Désactivation d'un type de logement.
     *
     * @param typeLogementId identifiant du type de logement à désactiver
     * @return type de logement inactivé
     */
    TypeLogement inactivateTypeLogement(Long typeLogementId);

    /**
     * Recherche de types de logement.
     *
     * @param libelle          libellé du type de logement à rechercher
     * @param actifUniquement  si true, ne retourne que les types actifs
     * @param pageable         pagination
     * @return page de types de logement
     */
    Page<TypeLogement> searchTypeLogements(String libelle, Boolean actifUniquement, Pageable pageable);
}


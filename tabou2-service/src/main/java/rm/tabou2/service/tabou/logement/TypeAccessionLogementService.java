package rm.tabou2.service.tabou.logement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeAccessionLogement;

public interface TypeAccessionLogementService {

    /**
     * Récupération d'un type d'accession logement.
     *
     * @param typeAccessionLogementId identifiant du type d'accession logement
     * @return type d'accession logement
     */
    TypeAccessionLogement getById(long typeAccessionLogementId);

    /**
     * Ajout d'un type d'accession logement.
     *
     * @param typeAccessionLogement type d'accession logement à ajouter
     * @return nouveau type d'accession logement
     */
    TypeAccessionLogement createTypeAccessionLogement(TypeAccessionLogement typeAccessionLogement);

    /**
     * Modification d'un type d'accession logement.
     *
     * @param typeAccessionLogement type d'accession logement à modifier
     * @return type d'accession logement modifié
     */
    TypeAccessionLogement updateTypeAccessionLogement(TypeAccessionLogement typeAccessionLogement);

    /**
     * Désactivation d'un type d'accession logement.
     *
     * @param typeAccessionLogementId identifiant du type d'accession logement à désactiver
     * @return type d'accession logement inactivé
     */
    TypeAccessionLogement inactivateTypeAccessionLogement(Long typeAccessionLogementId);

    /**
     * Recherche de types d'accession logement.
     *
     * @param libelle          libellé du type d'accession logement à rechercher
     * @param actifUniquement  si true, ne retourne que les types actifs
     * @param pageable         pagination
     * @return page de types d'accession logement
     */
    Page<TypeAccessionLogement> searchTypeAccessionLogements(String libelle, Boolean actifUniquement, Pageable pageable);
}


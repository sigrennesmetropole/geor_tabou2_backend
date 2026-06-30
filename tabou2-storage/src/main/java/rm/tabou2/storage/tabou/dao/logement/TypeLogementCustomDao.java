package rm.tabou2.storage.tabou.dao.logement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.logement.TypeLogementEntity;

public interface TypeLogementCustomDao {

    /**
     * Recherche des types de logement avec filtres optionnels.
     *
     * @param libelle          filtre sur le libellé (supporte le wildcard *)
     * @param actifUniquement  si true, ne retourne que les types actifs (filtre sur les dates de validité)
     * @param pageable         informations de pagination
     * @return page de résultats
     */
    Page<TypeLogementEntity> searchTypeLogements(String libelle, Boolean actifUniquement, Pageable pageable);


}


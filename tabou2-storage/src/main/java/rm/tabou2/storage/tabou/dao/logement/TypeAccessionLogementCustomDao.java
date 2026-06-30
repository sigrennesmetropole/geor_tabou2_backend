package rm.tabou2.storage.tabou.dao.logement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.logement.TypeAccessionLogementEntity;
import rm.tabou2.storage.tabou.item.TypeAccessionLogementCriteria;

public interface TypeAccessionLogementCustomDao {

    /**
     * Recherche des types d'accession logement avec filtres optionnels.
     *
     * @param criteria critères de recherche (libellé, actif, portée)
     * @param pageable informations de pagination
     * @return page de résultats
     */
    Page<TypeAccessionLogementEntity> searchTypeAccessionLogements(TypeAccessionLogementCriteria criteria, Pageable pageable);

}

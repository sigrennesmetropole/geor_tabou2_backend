package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeOccupationEntity;
import rm.tabou2.storage.tabou.item.TypeOccupationCriteria;


public interface TypeOccupationCustomDao {

    /**
     * Recherche des types d'occupations
     * @param criteria Critères de recherche
     * @param pageable Format de la recherche
     * @return Page de résultats correspondant à la recherche
     */
    Page<TypeOccupationEntity> searchTypeOccupation(TypeOccupationCriteria criteria, Pageable pageable);
}

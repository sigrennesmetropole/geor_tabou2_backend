package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeAmenageurEntity;
import rm.tabou2.storage.tabou.item.TypeAmenageurCriteria;


public interface TypeAmenageurCustomDao {

    /**
     * Recherche les types d'amenageurs
     * @param criteria Critères de recherche
     * @param pageable Critères de pagination
     * @return
     */
    Page<TypeAmenageurEntity> searchTypesAmenageurs(TypeAmenageurCriteria criteria, Pageable pageable);
}

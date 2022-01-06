package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeActeurEntity;
import rm.tabou2.storage.tabou.item.TypeActeurCriteria;

public interface TypeActeurCustomDao {

    Page<TypeActeurEntity> searchTypesActeurs(TypeActeurCriteria criteria, Pageable pageable);
}

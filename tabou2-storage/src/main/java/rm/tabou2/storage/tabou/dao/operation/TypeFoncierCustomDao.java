package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeFoncierEntity;
import rm.tabou2.storage.tabou.item.TypeFoncierCriteria;

public interface TypeFoncierCustomDao {

    Page<TypeFoncierEntity> searchTypesFonciers(TypeFoncierCriteria criteria, Pageable pageable);
}

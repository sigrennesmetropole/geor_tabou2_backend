package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeActionOperationEntity;
import rm.tabou2.storage.tabou.item.TypeActionOperationCriteria;

public interface TypeActionOperationCustomDao {

    Page<TypeActionOperationEntity> searchTypesActionsOperations(TypeActionOperationCriteria criteria, Pageable pageable);
}

package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeActionOperation;
import rm.tabou2.storage.tabou.item.TypeActionOperationCriteria;

public interface TypeActionOperationService {
    Page<TypeActionOperation> searchTypesActionsOperations(TypeActionOperationCriteria criteria, Pageable pageable);
}

package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.storage.tabou.item.OperationsCriteria;


public interface OperationService {

    Operation addOperation(Operation operation);

    Operation getOperationById(long operationId);

    Page<Operation> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable);
}

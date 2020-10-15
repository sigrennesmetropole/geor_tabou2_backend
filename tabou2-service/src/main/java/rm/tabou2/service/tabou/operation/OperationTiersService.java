package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;

import java.util.List;

public interface OperationTiersService {

    Operation associateTiersToOperation(long operationId, long tiersId, long typeTiersId) throws AppServiceException;

    List<OperationTiersEntity> getTiersByOperationId(long operationId);

}

package rm.tabou2.service;

import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;

public interface OperationTiersService {

    Operation associateTiersToOperation(long operationId, long tiersId, long typeTiersId) throws AppServiceException;

}

package rm.tabou2.service;

import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.Programme;

public interface OperationTiersService {

    Programme associateTiersToProgramme(long tiersId, long programmeId, long typeTiersId);

    Operation associateTiersToOperation(long tiersId, long operationId, long typeTiersId);

    void associateTiers(long tiersId, long programmeId, long operationId, long typeTiersId);
}

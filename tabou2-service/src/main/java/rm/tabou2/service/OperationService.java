package rm.tabou2.service;

import rm.tabou2.service.dto.Operation;

import java.util.List;

public interface OperationService {
    Operation addOperation(Operation operation);

    List<Operation> getAllOperations(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc);

    Operation getOperationById(long operationId);
}

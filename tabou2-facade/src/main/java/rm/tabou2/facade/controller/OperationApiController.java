package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.OperationsApi;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.storage.entity.OperationEntity;

import javax.validation.Valid;
import java.util.List;

@Controller
public class OperationApiController implements OperationsApi {


    @Override
    public ResponseEntity<Operation> addOperation(@Valid Operation operation) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Operation> editOperation(@Valid Operation operation) throws Exception {

        OperationEntity op = new OperationEntity();

        return null;
    }

    @Override
    public ResponseEntity<Operation> getOperationById(Long operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Operation>> getOperations(@Valid String keyword, @Valid Long start, @Valid Long end, @Valid Boolean onlyActive, @Valid Long resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }

}

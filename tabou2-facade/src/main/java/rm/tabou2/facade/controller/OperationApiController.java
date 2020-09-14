package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.OperationsApi;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.OperationTiersService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.OperationTiers;

import javax.validation.Valid;
import java.util.List;

@Controller
public class OperationApiController implements OperationsApi {

    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationTiersService operationTiersService;

    @Override
    public ResponseEntity<Operation> addOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.addOperation(operation), HttpStatus.OK);

    }



    @Override
    public ResponseEntity<Operation> editOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.addOperation(operation), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> getOperationById(Long operationId) throws Exception {

        return new ResponseEntity<>(operationService.getOperationById(operationId), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<Operation>> getOperations(@Valid String keyword, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        return new ResponseEntity<>(operationService.getAllOperations(keyword, start, resultsNumber, orderBy, asc), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> associateTiersToOperation(@Valid OperationTiers operationTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.associateTiersToOperation(operationTiers.getOperationId(), operationTiers.getTiersId(), operationTiers.getTypeTiersId()), HttpStatus.OK);
    }




}

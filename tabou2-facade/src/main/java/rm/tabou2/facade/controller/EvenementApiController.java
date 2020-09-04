package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EvenementsApi;
import rm.tabou2.service.EvenementService;
import rm.tabou2.service.dto.Evenement;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EvenementApiController implements EvenementsApi {

    @Autowired
    private EvenementService evenementService;

    @Override
    public ResponseEntity<Evenement> addEvenementByOperationId(@Valid Evenement evenement, String operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByProgrammeId(@Valid Evenement evenement, String programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Evenement> editEvenementByOperationId(@Valid Evenement evenement, String operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Evenement> editEvenementByProgrammeId(@Valid Evenement evenement, String programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Evenement>> getEvenementsByOperationId(Long operationId) throws Exception {
        return new ResponseEntity<>(evenementService.getByOperationId(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Evenement>> getEvenementsByProgrammeId(String programmeId) throws Exception {
        return null;
    }
}

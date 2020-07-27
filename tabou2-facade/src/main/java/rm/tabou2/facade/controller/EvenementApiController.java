package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EvenementsApi;
import rm.tabou2.service.dto.Evenement;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EvenementApiController implements EvenementsApi {

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
    public ResponseEntity<List<Evenement>> getEvenementsByOperationId(String operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Evenement>> getEvenementsByProgrammeId(String programmeId) throws Exception {
        return null;
    }
}

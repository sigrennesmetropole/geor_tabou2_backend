package rm.tabou2.facade.controller.tabou.evenement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EvenementsApi;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.tabou.operation.EvenementOperationService;
import rm.tabou2.service.tabou.programme.EvenementProgrammeService;
import rm.tabou2.service.dto.Evenement;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
public class EvenementApiController implements EvenementsApi {

    @Autowired
    private EvenementOperationService evenementOperationService;

    @Autowired
    private EvenementProgrammeService evenementProgrammeService;

    @Override
    public ResponseEntity<List<Evenement>> getEvenementsByOperationId(Long operationId) throws Exception {
        return new ResponseEntity<>(evenementOperationService.getByOperationId(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByOperationId(@Valid Evenement evenement, Long operationId) throws Exception {
        return new ResponseEntity<>(evenementOperationService.addEvenement(evenement, operationId), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Evenement> updateEvenement(@Valid Evenement evenement, Long operationId) throws Exception {

        //TODO : test des droits
        return new ResponseEntity<>(evenementOperationService.editEvenement(evenement, operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Evenement>> getEvenementsByProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(evenementProgrammeService.getByProgrammeId( programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByProgrammeId(@Valid Evenement evenement, Long programmeId) throws Exception {
        return new ResponseEntity<>(evenementProgrammeService.addByProgrammeId(evenement, programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Operation> deleteEvenementByOperationId(@NotNull @Valid Long evenementId, Long operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Programme> deleteEvenementByProgrammeId(@NotNull @Valid Long evenementId, Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Evenement> updateEvenementByProgrammeId(@Valid Evenement evenement, Long programmeId) throws Exception {
        return new ResponseEntity<>(evenementProgrammeService.editByProgrammeId(evenement, programmeId), HttpStatus.OK);
    }


}

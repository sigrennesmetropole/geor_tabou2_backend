package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EvenementsApi;
import rm.tabou2.service.EvenementOperationService;
import rm.tabou2.service.EvenementProgrammeService;
import rm.tabou2.service.dto.Evenement;

import javax.validation.Valid;
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
    public ResponseEntity<Evenement> editEvenement(@Valid Evenement evenement, Long operationId) throws Exception {

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
    public ResponseEntity<Evenement> editEvenementByProgrammeId(@Valid Evenement evenement, Long programmeId) throws Exception {
        return new ResponseEntity<>(evenementProgrammeService.editByProgrammeId(evenement, programmeId), HttpStatus.OK);
    }


}

package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EtapesApi;
import rm.tabou2.service.EtapeOperationService;
import rm.tabou2.service.EtapeProgrammeService;
import rm.tabou2.service.dto.Etape;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EtapeApiController implements EtapesApi {

    @Autowired
    private EtapeOperationService etapeOperationService;

    @Autowired
    private EtapeProgrammeService etapeProgrammeService;

    @Override
    public ResponseEntity<Etape> addEtapeOperation(@Valid Etape etape) throws Exception {

        return new ResponseEntity<>(etapeOperationService.addEtapeOperation(etape), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Etape> addEtapeProgramme(@Valid Etape etape) throws Exception {

        return new ResponseEntity<>(etapeProgrammeService.addEtapeProgramme(etape), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Etape> editEtapeOperation(@Valid Etape etape) throws Exception {
        return new ResponseEntity<>(etapeOperationService.addEtapeOperation(etape), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Etape> editEtapeProgramme(@Valid Etape etape) throws Exception {
        return new ResponseEntity<>(etapeProgrammeService.addEtapeProgramme(etape), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Etape> getEtapeByOperationId(Long operationId) throws Exception {
        return new ResponseEntity<>(etapeOperationService.getEtapeOperationById(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapeByProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(etapeProgrammeService.getEtapesForProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesOperation() throws Exception {
        return new ResponseEntity<>(etapeOperationService.getEtapesForOperation(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesProgramme() throws Exception {
        return new ResponseEntity<>(etapeProgrammeService.getEtapesForProgrammes(), HttpStatus.OK);
    }
}

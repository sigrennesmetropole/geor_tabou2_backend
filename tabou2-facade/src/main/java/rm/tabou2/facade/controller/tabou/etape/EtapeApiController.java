package rm.tabou2.facade.controller.tabou.etape;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EtapesApi;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.tabou.operation.EtapeOperationService;
import rm.tabou2.service.tabou.programme.EtapeProgrammeService;
import rm.tabou2.service.dto.Etape;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
public class EtapeApiController implements EtapesApi {

    @Autowired
    private EtapeOperationService etapeOperationService;

    @Autowired
    private EtapeProgrammeService etapeProgrammeService;


    @Override
    public ResponseEntity<Operation> updateEtapeByOperationId(Long operationId, @NotNull @Valid Long etapeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Etape> getEtapeByProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(etapeProgrammeService.getEtapeProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesByOperationId(Long operationId) throws Exception {
        //Liste des étapes d'avancement possibles pour
        //return new ResponseEntity<List<Etape>>(etapeOperationService.getEtapeOperationById(operationId), HttpStatus.OK);
        return null;
    }


}

package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EtapesApi;
import rm.tabou2.service.dto.Etape;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EtapeApiController implements EtapesApi {


    @Override
    public ResponseEntity<Etape> addEtapeOperation(@Valid Etape etape) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Etape> addEtapeProgramme(@Valid Etape etape) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Etape> editEtapeOperation(@Valid Etape etape) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Etape> editEtapeProgramme(@Valid Etape etape) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesByOperationId(Long operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesOperation() throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesProgramme() throws Exception {
        return null;
    }
}

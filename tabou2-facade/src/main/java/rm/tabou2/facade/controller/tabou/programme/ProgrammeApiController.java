package rm.tabou2.facade.controller.tabou.programme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ProgrammesApi;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeTiersService;
import rm.tabou2.service.dto.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProgrammeApiController implements ProgrammesApi {


    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private ProgrammeTiersService programmeTiersService;

    @Override
    public ResponseEntity<Programme> createProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.addProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> updateProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.addProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Agapeo>> getAgapeoByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<PermisConstruire>> getPermisByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Programme> getProgrammeById(Long programmeId) throws Exception {
        return new ResponseEntity<>(programmeService.getProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Programme>> searchProgrammes(@Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return  new ResponseEntity<>(programmeService.searchProgrammes(keyword, start, resultsNumber, orderBy, asc), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> associateTiersToProgramme(@Valid ProgrammeTiers programmeTiers) throws Exception {
        return new ResponseEntity<>(programmeTiersService.associateTiersToProgramme(programmeTiers.getProgrammeId(), programmeTiers.getTiersId(), programmeTiers.getTypeTiersId()), HttpStatus.OK);
    }

}

package rm.tabou2.facade.controller.tabou.programme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ProgrammesApi;
import rm.tabou2.service.tabou.programme.EvenementProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeTiersService;
import rm.tabou2.service.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
public class ProgrammeApiController implements ProgrammesApi {


    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private ProgrammeTiersService programmeTiersService;

    @Autowired
    private EvenementProgrammeService evenementProgrammeService;


    @Override
    public ResponseEntity<Programme> createProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.createProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> deleteEvenementByProgrammeId(Long evenementId, Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Programme> updateProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.createProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Agapeo>> getAgapeoByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Etape> getEtapeByProgrammeId(Long programmeId) throws Exception {
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
    public ResponseEntity<List<AssociationTiersTypeTiers>> getTiersByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Programme>> searchProgrammes(@Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return  new ResponseEntity<>(programmeService.searchProgrammes(keyword, start, resultsNumber, orderBy, asc), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> updateEvenementByProgrammeId(@Valid Evenement evenement, Long programmeId) throws Exception {
        return new ResponseEntity<>(evenementProgrammeService.updateByProgrammeId(evenement, programmeId), HttpStatus.OK);
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
    public ResponseEntity<Programme> associateTiersToProgramme(Long programmeId, @NotNull @Valid Long tiersId, @NotNull @Valid Long typeTiersId) throws Exception {
        return new ResponseEntity<>(programmeTiersService.associateTiersToProgramme(programmeId, tiersId, typeTiersId), HttpStatus.OK);
    }

}

package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ProgrammesApi;
import rm.tabou2.service.dto.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProgrammeApiController implements ProgrammesApi {


    @Override
    public ResponseEntity<Programme> addProgramme(@Valid Programme programme) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Programme> editProgramme(@Valid Programme programme) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Agapeo>> getAgapeoByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Dates> getDates(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Logements> getLogements(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<PermisConstruire>> getPermisByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Programme> getProgrammeById(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Programme>> getProgrammes(@Valid String keyword, @Valid Long start, @Valid Long end, @Valid Boolean onlyActive, @Valid Long resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }
}

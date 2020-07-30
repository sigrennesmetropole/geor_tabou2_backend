package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TiersApi;
import rm.tabou2.service.dto.Tiers;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TiersApiController implements TiersApi {


    @Override
    public ResponseEntity<Tiers> addTiers(@Valid Tiers tiers) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Tiers> editTiers(@Valid Tiers tiers) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Tiers>> getTiers(@Valid String keyword, @Valid Long start, @Valid Long end, @Valid Boolean onlyActive, @Valid Long resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }


    @Override
    public ResponseEntity<List<Tiers>> getTiersByOperationId(String operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Tiers>> getTiersByProgrammeId(String programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Tiers>> getTiersByType(String type, @Valid String keyword, @Valid Long start, @Valid Long end, @Valid Boolean onlyActive, @Valid Long resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }


}

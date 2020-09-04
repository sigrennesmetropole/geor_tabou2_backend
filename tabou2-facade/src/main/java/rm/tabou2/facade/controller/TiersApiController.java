package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TiersApi;
import rm.tabou2.service.TiersService;
import rm.tabou2.service.dto.Tiers;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TiersApiController implements TiersApi {

    @Autowired
    private TiersService tiersService;

    @Override
    public ResponseEntity<Tiers> addTiers(@Valid Tiers tiers) throws Exception {

        return new ResponseEntity<>(tiersService.addTiers(tiers), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Tiers> editTiers(@Valid Tiers tiers) throws Exception {

        return new ResponseEntity<>(tiersService.addTiers(tiers), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<Tiers>> getTiers(@Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        return new ResponseEntity(tiersService.searchTiers(keyword, start, onlyActive, resultsNumber, orderBy, asc), HttpStatus.OK);

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
    public ResponseEntity<List<Tiers>> getTiersByType(String type, @Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }


}

package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesTiersApi;
import rm.tabou2.service.TypeTiersService;
import rm.tabou2.service.dto.TypeTiers;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TypeTiersApiController implements TypesTiersApi {

    @Autowired
    private TypeTiersService typeTiersService;

    @Override
    public ResponseEntity<TypeTiers> addTypeTiers(@Valid TypeTiers typeTiers) throws Exception {
        return new ResponseEntity<>(typeTiersService.addTypeTiers(typeTiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeTiers> editTypeTiers(@Valid TypeTiers typeTiers) throws Exception {
        return new ResponseEntity<>(typeTiersService.editTypeTiers(typeTiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TypeTiers>> getTypeTiers(@Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return new ResponseEntity<>(typeTiersService.searchTypeTiers(keyword, start, onlyActive, resultsNumber, orderBy, asc), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<TypeTiers> inactivateTypeTiers(Long typeTiersId) throws Exception {
        return new ResponseEntity<>(typeTiersService.inactivateTypeTiers(typeTiersId), HttpStatus.OK);
    }
}

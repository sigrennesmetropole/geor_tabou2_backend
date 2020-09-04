package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesTiersApi;
import rm.tabou2.service.dto.TypeTiers;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TypeTiersApiController implements TypesTiersApi {


    @Override
    public ResponseEntity<TypeTiers> addTypeTiers(@Valid TypeTiers typeTiers) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<TypeTiers> editTypeTiers(@Valid TypeTiers typeTiers) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<TypeTiers>> getTypeTiers(@Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }


    @Override
    public ResponseEntity<List<TypeTiers>> inactivateTypeTiers(String typeTiersId) throws Exception {
        return null;
    }
}

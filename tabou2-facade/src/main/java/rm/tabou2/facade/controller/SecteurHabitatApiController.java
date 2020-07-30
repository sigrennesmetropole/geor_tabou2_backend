package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursHabitatApi;
import rm.tabou2.service.dto.SecteurHabitat;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SecteurHabitatApiController implements SecteursHabitatApi {


    @Override
    public ResponseEntity<List<SecteurHabitat>> getSecteursHabitat(@Valid String keyword, @Valid Long start, @Valid Long end, @Valid Boolean onlyActive, @Valid Long resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }
}

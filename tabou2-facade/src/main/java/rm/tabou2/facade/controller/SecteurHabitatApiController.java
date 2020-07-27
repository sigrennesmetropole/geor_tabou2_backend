package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursHabitatApi;
import rm.tabou2.service.dto.SearchParams;
import rm.tabou2.service.dto.SecteurHabitat;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SecteurHabitatApiController implements SecteursHabitatApi {

    @Override
    public ResponseEntity<List<SecteurHabitat>> getSecteursHabitat(@Valid SearchParams searchParams) throws Exception {
        return null;
    }

}

package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.CommunesApi;
import rm.tabou2.service.dto.Commune;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CommuneApiController implements CommunesApi {


    @Override
    public ResponseEntity<List<Commune>> getCommunes(@Valid String keyword, @Valid Long start, @Valid Long end, @Valid Boolean onlyActive, @Valid Long resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }
}

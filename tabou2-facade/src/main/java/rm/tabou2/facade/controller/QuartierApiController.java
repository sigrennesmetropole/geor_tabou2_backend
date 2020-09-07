package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.QuartiersApi;
import rm.tabou2.service.dto.Quartier;

import javax.validation.Valid;
import java.util.List;

@Controller
public class QuartierApiController implements QuartiersApi {




    @Override
    public ResponseEntity<List<Quartier>> getQuartier(@Valid String keyword, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }
}

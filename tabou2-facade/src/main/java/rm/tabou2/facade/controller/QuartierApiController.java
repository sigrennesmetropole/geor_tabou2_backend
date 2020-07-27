package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.QuartiersApi;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.service.dto.SearchParams;

import javax.validation.Valid;
import java.util.List;

@Controller
public class QuartierApiController implements QuartiersApi {

    @Override
    public ResponseEntity<List<Quartier>> getQuartier(@Valid SearchParams searchParams) throws Exception {
        return null;
    }
}

package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.CommunesApi;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.dto.SearchParams;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CommuneApiController implements CommunesApi {


    @Override
    public ResponseEntity<List<Commune>> getCommunes(@Valid SearchParams searchParams) throws Exception {
        return null;
    }
}

package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.CommunesApi;
import rm.tabou2.service.CommuneService;
import rm.tabou2.service.dto.Commune;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CommuneApiController implements CommunesApi {


    @Autowired
    private CommuneService communeService;

    @Override
    public ResponseEntity<List<Commune>> getCommunes(@Valid String keyword, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        return new ResponseEntity<>(communeService.searchCommunes(keyword, start, resultsNumber, orderBy, asc), HttpStatus.OK);

    }
}

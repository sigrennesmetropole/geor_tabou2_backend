package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.CommunesApi;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.sig.CommuneService;

import javax.validation.Valid;

@Controller
public class CommuneApiController implements CommunesApi {


    @Autowired
    private CommuneService communeService;

    @Override
    public ResponseEntity<PageResult> getCommunes(@Valid String codeInsee, @Valid String nom, @Valid String codePostal, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Commune> getCommuneById(@Valid Integer objectid) throws Exception {
        return null;
    }
}

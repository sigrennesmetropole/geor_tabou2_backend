package rm.tabou2.facade.controller.sig;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursSpeuApi;
import rm.tabou2.service.dto.SecteurHabitat;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class SecteurHabitatApiController implements SecteursSpeuApi {




    @Override
    public ResponseEntity<List<SecteurHabitat>> searchSecteursSpeu(@Valid BigDecimal id, @Valid String libelle, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }
}

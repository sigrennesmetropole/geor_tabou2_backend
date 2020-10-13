package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.QuartiersApi;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.service.sig.QuartierService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class QuartierApiController implements QuartiersApi {


    @Autowired
    private QuartierService quartierService;

    @Override
    public ResponseEntity<PageResult> getQuartier(@Valid String nom, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Quartier> getQuartierById(Integer quartierId) throws Exception {
        return null;
    }


}

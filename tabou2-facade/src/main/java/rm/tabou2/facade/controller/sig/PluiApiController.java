package rm.tabou2.facade.controller.sig;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.PluiApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.PluiZonage;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class PluiApiController implements PluiApi {

    @Override
    public ResponseEntity<PageResult> searchPlui(@Valid String libelle, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<PluiZonage> getPluiById(Integer pluiId) throws Exception {
        return null;
    }
}

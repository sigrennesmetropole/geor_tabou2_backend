package rm.tabou2.facade.controller.sig;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.PluiApi;
import rm.tabou2.service.dto.PluiZonage;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PluiApiController implements PluiApi {

    @Override
    public ResponseEntity<List<PluiZonage>> getPlui(@Valid String keyword, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }
}

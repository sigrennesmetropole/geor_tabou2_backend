package rm.tabou2.facade.controller.sig;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursSamApi;
import rm.tabou2.service.dto.SecteurSam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SecteurSamApiController implements SecteursSamApi {


    @Override
    public ResponseEntity<List<SecteurSam>> getSecteursSam(@Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return null;
    }
}

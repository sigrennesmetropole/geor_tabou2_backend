package rm.tabou2.facade.controller.tabou.etape;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EtapesApi;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Programme;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EtapesApiController implements EtapesApi {
    @Override
    public ResponseEntity<Programme> editEtapeForProgrammeId(Long programmeId, @Valid Etape etape) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapeByProgrammeId(Long programmeId) throws Exception {
        return null;
    }
}

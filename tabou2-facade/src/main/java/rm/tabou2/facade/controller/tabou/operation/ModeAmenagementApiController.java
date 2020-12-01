package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ModeAmenagementApi;
import rm.tabou2.service.dto.ModeAmenagement;
import rm.tabou2.service.tabou.operation.ModeAmenagementService;

import java.util.List;

@Controller
public class ModeAmenagementApiController implements ModeAmenagementApi {

    @Autowired
    private ModeAmenagementService modeAmenagementService;

    @Override
    public ResponseEntity<List<ModeAmenagement>> getModesAmenagement() throws Exception {
        return new ResponseEntity<>(modeAmenagementService.getAllModesAmenagement(), HttpStatus.OK);
    }
}

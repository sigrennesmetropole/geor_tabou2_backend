package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.VocationsApi;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.tabou.operation.VocationService;

import java.util.List;

@Controller
public class VocationApiController implements VocationsApi {

    @Autowired
    private VocationService vocationService;

    @Override
    public ResponseEntity<List<Vocation>> getVocations() throws Exception {
        return new ResponseEntity<>(vocationService.getAllVocations(), HttpStatus.OK);
    }
}

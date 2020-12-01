package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.DecisionsApi;
import rm.tabou2.service.dto.Decision;
import rm.tabou2.service.tabou.operation.DecisionService;

import java.util.List;

@Controller
public class DecisionApiController implements DecisionsApi {

    @Autowired
    private DecisionService decisionService;

    @Override
    public ResponseEntity<List<Decision>> getDecisions() throws Exception {
        return new ResponseEntity<>(decisionService.getAllDecisions(), HttpStatus.OK);
    }
}

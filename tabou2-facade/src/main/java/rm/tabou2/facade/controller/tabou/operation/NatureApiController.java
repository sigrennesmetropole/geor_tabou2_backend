package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rm.tabou2.facade.api.NaturesApi;
import rm.tabou2.service.tabou.operation.NatureService;
import rm.tabou2.service.dto.Nature;

import java.util.List;

public class NatureApiController implements NaturesApi {

    @Autowired
    private NatureService natureService;

    @Override
    public ResponseEntity<List<Nature>> getNatures() throws Exception {
        return new ResponseEntity<>(natureService.getAllNatures(true), HttpStatus.OK);
    }


}

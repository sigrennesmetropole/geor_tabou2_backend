package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ConsommationEspaceApi;
import rm.tabou2.service.dto.ConsommationEspace;
import rm.tabou2.service.tabou.operation.ConsommationEspaceService;

import java.util.List;

@Controller
public class ConsommationEspaceApiController implements ConsommationEspaceApi {

    @Autowired
    private ConsommationEspaceService consommationEspaceService;

    @Override
    public ResponseEntity<List<ConsommationEspace>> getConsommationsEspace() throws Exception {
        return new ResponseEntity<>(consommationEspaceService.getAllConsommationsEspace(), HttpStatus.OK);
    }
}

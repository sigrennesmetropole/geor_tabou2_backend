package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.MaitriseOuvrageApi;
import rm.tabou2.service.dto.MaitriseOuvrage;
import rm.tabou2.service.tabou.operation.MaitriseOuvrageService;

import java.util.List;

@Controller
public class MaitriseOuvrageApiController implements MaitriseOuvrageApi {

    @Autowired
    private MaitriseOuvrageService maitriseOuvrageService;

    @Override
    public ResponseEntity<List<MaitriseOuvrage>> getMaitrisesOuvrage() throws Exception {
        return new ResponseEntity<>(maitriseOuvrageService.getAllMaitrisesOuvrage(), HttpStatus.OK);
    }
}

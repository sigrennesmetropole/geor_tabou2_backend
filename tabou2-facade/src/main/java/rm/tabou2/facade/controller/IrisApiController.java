package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.IrisApi;
import rm.tabou2.service.IrisService;
import rm.tabou2.service.dto.Iris;

import javax.validation.Valid;
import java.util.List;

@Controller
public class IrisApiController implements IrisApi {

    @Autowired
    private IrisService irisService;

    @Override
    public ResponseEntity<List<Iris>> getIris(@Valid String keyword, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return new ResponseEntity<>(irisService.searchIris(keyword, start, resultsNumber, orderBy, asc), HttpStatus.OK);
    }
}

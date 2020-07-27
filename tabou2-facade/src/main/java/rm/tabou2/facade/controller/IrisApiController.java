package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.IrisApi;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.service.dto.SearchParams;

import javax.validation.Valid;
import java.util.List;

@Controller
public class IrisApiController implements IrisApi {


    @Override
    public ResponseEntity<List<Iris>> getIris(@Valid SearchParams searchParams) throws Exception {
        return null;
    }
}

package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.PermisApi;
import rm.tabou2.service.dto.PermisConstruire;

import java.util.List;

@Controller
public class PermisConstruireApiController implements PermisApi {

    @Override
    public ResponseEntity<List<PermisConstruire>> getPermisById(Double permisId) throws Exception {
        return null;
    }

}
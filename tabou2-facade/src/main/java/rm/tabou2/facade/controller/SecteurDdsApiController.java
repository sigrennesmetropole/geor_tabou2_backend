package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursDdsApi;
import rm.tabou2.service.dto.SearchParams;
import rm.tabou2.service.dto.SecteurDds;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SecteurDdsApiController implements SecteursDdsApi {


    @Override
    public ResponseEntity<List<SecteurDds>> getSecteursDds(@Valid SearchParams searchParams) throws Exception {
        return null;
    }
}

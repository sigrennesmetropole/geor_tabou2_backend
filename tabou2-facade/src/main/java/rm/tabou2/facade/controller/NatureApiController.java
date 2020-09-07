package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rm.tabou2.facade.api.NaturesApi;
import rm.tabou2.service.NatureService;
import rm.tabou2.service.dto.Nature;

import javax.validation.Valid;
import java.util.List;

public class NatureApiController implements NaturesApi {

    @Autowired
    private NatureService natureService;

    @Override
    public ResponseEntity<Nature> addNature(@Valid Nature nature) throws Exception {

        return new ResponseEntity<>(natureService.addNature(nature), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Nature> editNature(@Valid Nature nature) throws Exception {

        return new ResponseEntity<>(natureService.addNature(nature), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<Nature>> getNatures() throws Exception {
        return new ResponseEntity<>(natureService.getAllNatures(true), HttpStatus.OK);
    }

    //TODO : inactivate
}

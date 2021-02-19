package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.CommunesApi;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.sig.CommuneService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.CommuneEntity;

import javax.validation.Valid;

@Controller
public class CommuneApiController implements CommunesApi {


    @Autowired
    private CommuneService communeService;

    @Override
    public ResponseEntity<PageResult> searchCommunes(@Valid String codeInsee, @Valid String nom, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, CommuneEntity.class);

        Page<Commune> page = communeService.searchCommunes(nom, codeInsee, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Commune> getCommuneById(@Valid Integer objectid) throws Exception {
        return new ResponseEntity<>(communeService.getCommuneById(objectid), HttpStatus.OK);
    }
}

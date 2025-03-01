package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.SecteursFoncierApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurFoncier;
import rm.tabou2.service.sig.SecteurFoncierService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.SecteurFoncierEntity;

@RestController
public class SecteurFoncierApiController implements SecteursFoncierApi {

    @Autowired
    private SecteurFoncierService secteurFoncierService;

    @Override
    public ResponseEntity<PageResult> searchSecteursFonciers(String negociateur, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurFoncierEntity.class);

        Page<SecteurFoncier> page = secteurFoncierService.searchSecteursFonciers(negociateur, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }


}

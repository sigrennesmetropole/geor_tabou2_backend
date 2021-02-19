package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rm.tabou2.facade.api.SecteursFoncierApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurDds;
import rm.tabou2.service.dto.SecteurFoncier;
import rm.tabou2.service.sig.SecteurFoncierService;
import rm.tabou2.service.utils.PaginationUtils;

import javax.validation.Valid;

public class SecteurFoncierApiController implements SecteursFoncierApi {

    @Autowired
    private SecteurFoncierService secteurFoncierService;

    public ResponseEntity<PageResult> searchSecteursFonciers(@Valid String negociateur, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurDds.class);

        Page<SecteurFoncier> page = secteurFoncierService.searchSecteursFonciers(negociateur, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }


}

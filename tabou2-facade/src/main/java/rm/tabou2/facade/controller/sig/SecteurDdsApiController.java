package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.SecteursDdsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurDds;
import rm.tabou2.service.sig.SecteurDdsService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.SecteurDdsEntity;

@RestController
public class SecteurDdsApiController implements SecteursDdsApi {

    @Autowired
    private SecteurDdsService secteurDdsService;

    @Override
    public ResponseEntity<PageResult> searchSecteursDds(String secteur, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurDdsEntity.class);

        Page<SecteurDds> page = secteurDdsService.searchSecteursDds(secteur, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

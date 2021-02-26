package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursDdsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurDds;
import rm.tabou2.service.sig.SecteurDdsService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.SecteurDdsEntity;

import javax.validation.Valid;

@Controller
public class SecteurDdsApiController implements SecteursDdsApi {

    @Autowired
    private SecteurDdsService secteurDdsService;

    @Override
    public ResponseEntity<PageResult> searchSecteursDds(@Valid String secteur, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurDdsEntity.class);

        Page<SecteurDds> page = secteurDdsService.searchSecteursDds(secteur, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

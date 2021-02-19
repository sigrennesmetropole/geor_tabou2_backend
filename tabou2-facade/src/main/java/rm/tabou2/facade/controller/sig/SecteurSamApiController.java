package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursSamApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurDds;
import rm.tabou2.service.dto.SecteurSam;
import rm.tabou2.service.sig.SecteurSamService;
import rm.tabou2.service.utils.PaginationUtils;

import javax.validation.Valid;

@Controller
public class SecteurSamApiController implements SecteursSamApi {

    @Autowired
    private SecteurSamService secteurSamService;

    @Override
    public ResponseEntity<PageResult> searchSecteursSam(@Valid String nom, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurDds.class);

        Page<SecteurSam> page = secteurSamService.searchSecteursSam(nom, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

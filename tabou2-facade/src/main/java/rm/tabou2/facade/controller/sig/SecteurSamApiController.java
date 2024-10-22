package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.SecteursSamApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurSam;
import rm.tabou2.service.sig.SecteurSamService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.SecteurSamEntity;

@RestController
public class SecteurSamApiController implements SecteursSamApi {

    @Autowired
    private SecteurSamService secteurSamService;

    @Override
    public ResponseEntity<PageResult> searchSecteursSam(String nom, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurSamEntity.class);

        Page<SecteurSam> page = secteurSamService.searchSecteursSam(nom, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

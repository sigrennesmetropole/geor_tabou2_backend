package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.PluiApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.service.sig.PluiService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.PluiEntity;

import javax.validation.Valid;

@Controller
public class PluiApiController implements PluiApi {

    @Autowired
    private PluiService pluiService;


    @Override
    public ResponseEntity<PageResult> searchPlui(@Valid String libelle, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, PluiEntity.class);

        Page<PluiZonage> page = pluiService.searchPlui(libelle, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PluiZonage> getPluiById(Integer pluiId) throws Exception {
        return null;
    }
}

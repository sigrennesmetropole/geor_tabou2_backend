package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.MaitriseOuvrageApi;
import rm.tabou2.service.dto.MaitriseOuvrage;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.MaitriseOuvrageService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;

import javax.validation.Valid;

@Controller
public class MaitriseOuvrageApiController implements MaitriseOuvrageApi {

    @Autowired
    private MaitriseOuvrageService maitriseOuvrageService;

    @Override
    public ResponseEntity<PageResult> getMaitrisesOuvrage(@Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, MaitriseOuvrageEntity.class);

        Page<MaitriseOuvrage> page = maitriseOuvrageService.searchMaitrisesOuvrage(pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

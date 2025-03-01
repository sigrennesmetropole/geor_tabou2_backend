package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.MaitriseOuvrageApi;
import rm.tabou2.service.dto.MaitriseOuvrage;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.MaitriseOuvrageService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;

@RestController
public class MaitriseOuvrageApiController implements MaitriseOuvrageApi {

    @Autowired
    private MaitriseOuvrageService maitriseOuvrageService;
    private static final String SORT_BY_ORDER = "order";

    @Override
    public ResponseEntity<PageResult> getMaitrisesOuvrage(Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        if(orderBy == null) {
            orderBy = SORT_BY_ORDER;
        }

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, MaitriseOuvrageEntity.class);

        Page<MaitriseOuvrage> page = maitriseOuvrageService.searchMaitrisesOuvrage(pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

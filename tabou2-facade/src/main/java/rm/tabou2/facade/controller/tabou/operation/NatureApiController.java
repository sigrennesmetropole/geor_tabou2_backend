package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.NaturesApi;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.NatureService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;

@RestController
public class NatureApiController implements NaturesApi {

    @Autowired
    private NatureService natureService;


    @Override
    public ResponseEntity<PageResult> getNatures(Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, NatureEntity.class);

        Page<Nature> page = natureService.searchNatures(true, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

}

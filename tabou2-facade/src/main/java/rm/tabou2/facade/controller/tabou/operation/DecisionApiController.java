package rm.tabou2.facade.controller.tabou.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.DecisionsApi;
import rm.tabou2.service.dto.Decision;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.DecisionService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.DecisionEntity;

@RestController
@RequiredArgsConstructor
public class DecisionApiController implements DecisionsApi {

    private final DecisionService decisionService;

    @Override
    public ResponseEntity<PageResult> getDecisions(Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, DecisionEntity.class);

        Page<Decision> page = decisionService.searchDecisions(pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

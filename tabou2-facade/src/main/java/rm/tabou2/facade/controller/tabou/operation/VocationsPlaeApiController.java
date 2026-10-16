package rm.tabou2.facade.controller.tabou.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.VocationsPlaeApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.VocationPlae;
import rm.tabou2.service.tabou.operation.VocationPlaeService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.VocationPlaeEntity;

@RestController
@RequiredArgsConstructor
public class VocationsPlaeApiController implements VocationsPlaeApi {

    private final VocationPlaeService vocationPlaeService;

    @Override
    public ResponseEntity<PageResult> searchVocationsPlae(Boolean actifUniquement, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, VocationPlaeEntity.class);

        Page<VocationPlae> page = vocationPlaeService.searchVocationsPlae(actifUniquement, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

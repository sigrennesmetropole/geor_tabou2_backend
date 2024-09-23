package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.OutilAmenagementApi;
import rm.tabou2.service.dto.OutilAmenagement;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.OutilAmenagementService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.OutilAmenagementEntity;

import javax.validation.Valid;

@Controller
public class OutilAmenagementApiController implements OutilAmenagementApi {

    @Autowired
    private OutilAmenagementService outilAmenagementService;

    @Override
    public ResponseEntity<PageResult> searchOutilsAmenagement(@Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, OutilAmenagementEntity.class);

        Page<OutilAmenagement> page = outilAmenagementService.searchOutilsAmenagement(pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

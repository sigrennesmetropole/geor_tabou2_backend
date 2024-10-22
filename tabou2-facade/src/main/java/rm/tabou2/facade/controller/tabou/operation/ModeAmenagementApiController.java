package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.ModeAmenagementApi;
import rm.tabou2.service.dto.ModeAmenagement;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.ModeAmenagementService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.ModeAmenagementEntity;

@RestController
public class ModeAmenagementApiController implements ModeAmenagementApi {

    @Autowired
    private ModeAmenagementService modeAmenagementService;

    @Override
    public ResponseEntity<PageResult> getModesAmenagement(Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ModeAmenagementEntity.class);

        Page<ModeAmenagement> page = modeAmenagementService.searchModesAmenagement(pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

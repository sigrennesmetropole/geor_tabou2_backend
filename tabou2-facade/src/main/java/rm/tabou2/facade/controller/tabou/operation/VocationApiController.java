package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.VocationsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.tabou.operation.VocationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;

import javax.validation.Valid;

@Controller
public class VocationApiController implements VocationsApi {

    @Autowired
    private VocationService vocationService;

    @Override
    public ResponseEntity<PageResult> getVocations(@Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, VocationEntity.class);

        Page<Vocation> page = vocationService.searchVocations(pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

}

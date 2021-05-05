package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ConsommationEspaceApi;
import rm.tabou2.service.dto.ConsommationEspace;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.ConsommationEspaceService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.ConsommationEspaceEntity;

import javax.validation.Valid;

@Controller
public class ConsommationEspaceApiController implements ConsommationEspaceApi {

    @Autowired
    private ConsommationEspaceService consommationEspaceService;

    @Override
    public ResponseEntity<PageResult> getConsommationsEspace(@Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ConsommationEspaceEntity.class);

        Page<ConsommationEspace> page = consommationEspaceService.searchConsommationsEspace(pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

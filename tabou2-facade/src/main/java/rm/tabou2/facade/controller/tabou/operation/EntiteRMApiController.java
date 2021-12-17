package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EntitesRmApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.EntiteRMService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.EntiteRMEntity;
import rm.tabou2.storage.tabou.item.EntiteRMCriteria;
import rm.tabou2.service.dto.EntiteRM;

@Controller
public class EntiteRMApiController implements EntitesRmApi {

    @Autowired
    EntiteRMService entiteRMService;

    @Override
    public ResponseEntity<PageResult> searchEntitesRM(String code, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        EntiteRMCriteria criteria = new EntiteRMCriteria();
        criteria.setCode(code);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EntiteRMEntity.class);

        Page<EntiteRM> page = entiteRMService.searchEntitesRM(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

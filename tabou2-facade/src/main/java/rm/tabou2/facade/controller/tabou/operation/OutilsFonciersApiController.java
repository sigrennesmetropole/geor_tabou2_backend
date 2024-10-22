package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.OutilsFonciersApi;
import rm.tabou2.service.dto.OutilFoncier;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.OutilsFonciersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.OutilFoncierEntity;
import rm.tabou2.storage.tabou.item.OutilFoncierCriteria;

@RestController
public class OutilsFonciersApiController implements OutilsFonciersApi {

    @Autowired
    OutilsFonciersService outilsFonciersService;

    @Override
    public ResponseEntity<PageResult> searchOutilsFonciers(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        OutilFoncierCriteria criteria = new OutilFoncierCriteria();
        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, OutilFoncierEntity.class);

        Page<OutilFoncier> page = outilsFonciersService.searchOutilsFonciers(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

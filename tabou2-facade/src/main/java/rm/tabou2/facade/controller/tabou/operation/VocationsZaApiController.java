package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.VocationZA;
import rm.tabou2.service.tabou.operation.VocationZAService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.VocationZAEntity;
import rm.tabou2.storage.tabou.item.VocationZACriteria;

@RestController
public class VocationsZaApiController implements rm.tabou2.facade.api.VocationsZaApi {

    @Autowired
    VocationZAService vocationZAService;

    @Override
    public ResponseEntity<PageResult> searchVocationsZA(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        VocationZACriteria criteria = new VocationZACriteria();

        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, VocationZAEntity.class);

        Page<VocationZA> page = vocationZAService.searchVocationsZA(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

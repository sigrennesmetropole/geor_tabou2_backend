package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.EntitesReferentesApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.EntiteReferenteService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.EntiteReferenteEntity;
import rm.tabou2.storage.tabou.item.EntiteReferenteCriteria;
import rm.tabou2.service.dto.EntiteReferente;

@Controller
public class EntiteReferenteApiController implements EntitesReferentesApi {

    @Autowired
    EntiteReferenteService entiteReferenteService;

    @Override
    public ResponseEntity<PageResult> searchEntitesReferentes(String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        EntiteReferenteCriteria criteria = new EntiteReferenteCriteria();
        criteria.setCode(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EntiteReferenteEntity.class);

        Page<EntiteReferente> page = entiteReferenteService.searchEntitesReferentes(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

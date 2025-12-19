package rm.tabou2.facade.controller.tabou.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.EntitesReferentesApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.EntiteReferenteService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.EntiteReferenteEntity;
import rm.tabou2.storage.tabou.item.EntiteReferenteCriteria;
import rm.tabou2.service.dto.EntiteReferente;

@RestController
@RequiredArgsConstructor
public class EntiteReferenteApiController implements EntitesReferentesApi {

    private final EntiteReferenteService entiteReferenteService;

    @Override
    public ResponseEntity<PageResult> searchEntitesReferentes(String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {
        EntiteReferenteCriteria criteria = new EntiteReferenteCriteria();
        criteria.setCode(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EntiteReferenteEntity.class);

        Page<EntiteReferente> page = entiteReferenteService.searchEntitesReferentes(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }
}

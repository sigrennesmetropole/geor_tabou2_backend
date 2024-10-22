package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypesContributionsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeContribution;
import rm.tabou2.service.tabou.operation.TypeContributionService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeContributionEntity;
import rm.tabou2.storage.tabou.item.TypeContributionCriteria;

@RestController
public class TypesContributionsApiController implements TypesContributionsApi {

    @Autowired
    TypeContributionService service;

    @Override
    public ResponseEntity<PageResult> searchTypesContributions(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        TypeContributionCriteria criteria = new TypeContributionCriteria();
        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeContributionEntity.class);

        Page<TypeContribution> page = service.searchTypesContributions(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

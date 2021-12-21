package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypeOccupationApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.TypeOccupationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeOccupationEntity;
import rm.tabou2.storage.tabou.item.TypeOccupationCriteria;

@Controller
public class TypeOccupationApiController implements TypeOccupationApi {

    @Autowired
    TypeOccupationService typeOccupationService;

    @Override
    public ResponseEntity<PageResult> searchTypesOccupations(String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        TypeOccupationCriteria criteria = new TypeOccupationCriteria();
        criteria.setLibelle(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeOccupationEntity.class);

        Page<rm.tabou2.service.dto.TypeOccupation> page = typeOccupationService.searchTypeOccupations(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

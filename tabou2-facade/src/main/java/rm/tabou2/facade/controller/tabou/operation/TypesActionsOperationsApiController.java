package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesActionsOperationsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeActionOperation;
import rm.tabou2.service.tabou.operation.TypeActionOperationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeActionOperationEntity;
import rm.tabou2.storage.tabou.item.TypeActionOperationCriteria;

@Controller
public class TypesActionsOperationsApiController implements TypesActionsOperationsApi {

    @Autowired
    TypeActionOperationService service;

    @Override
    public ResponseEntity<PageResult> searchTypesActionsOperations(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        TypeActionOperationCriteria criteria = new TypeActionOperationCriteria();
        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeActionOperationEntity.class);
        Page<TypeActionOperation> page = service.searchTypesActionsOperations(criteria, pageable);
        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

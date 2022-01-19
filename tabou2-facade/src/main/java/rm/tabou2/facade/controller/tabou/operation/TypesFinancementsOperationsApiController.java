package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.facade.api.TypesFinancementsOperationsApi;
import rm.tabou2.service.tabou.operation.TypeFinancementOperationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeFinancementOperationEntity;
import rm.tabou2.storage.tabou.item.TypeFinancementOperationCriteria;
import rm.tabou2.service.dto.TypeFinancementOperation;

@Controller
public class TypesFinancementsOperationsApiController implements TypesFinancementsOperationsApi {

    @Autowired
    TypeFinancementOperationService typeFinancementOperationService;

    @Override
    public ResponseEntity<PageResult> searchTypesFinancementsOperations(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        TypeFinancementOperationCriteria criteria = new TypeFinancementOperationCriteria();

        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeFinancementOperationEntity.class);

        Page<TypeFinancementOperation> page = typeFinancementOperationService
                .searchTypesFinancementsOperations(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

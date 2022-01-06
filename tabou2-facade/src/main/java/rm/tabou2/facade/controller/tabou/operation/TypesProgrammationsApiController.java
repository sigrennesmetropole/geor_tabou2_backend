package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesProgrammationsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeProgrammation;
import rm.tabou2.service.tabou.operation.TypeProgrammationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeProgrammationEntity;
import rm.tabou2.storage.tabou.item.TypeProgrammationCriteria;

@Controller
public class TypesProgrammationsApiController implements TypesProgrammationsApi {

    @Autowired
    TypeProgrammationService service;

    @Override
    public ResponseEntity<PageResult> searchTypesProgrammations(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        TypeProgrammationCriteria criteria = new TypeProgrammationCriteria();
        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeProgrammationEntity.class);

        Page<TypeProgrammation> page = service.searchTypesProgrammations(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

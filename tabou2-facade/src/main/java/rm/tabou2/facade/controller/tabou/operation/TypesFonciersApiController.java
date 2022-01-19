package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesFonciersApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeFoncier;
import rm.tabou2.service.tabou.operation.TypeFoncierService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeFoncierEntity;
import rm.tabou2.storage.tabou.item.TypeFoncierCriteria;

@Controller
public class TypesFonciersApiController implements TypesFonciersApi {

    @Autowired
    TypeFoncierService service;

    @Override
    public ResponseEntity<PageResult> searchTypesFonciers(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        TypeFoncierCriteria criteria = new TypeFoncierCriteria();
        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeFoncierEntity.class);

        Page<TypeFoncier> page = service.searchTypesFonciers(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

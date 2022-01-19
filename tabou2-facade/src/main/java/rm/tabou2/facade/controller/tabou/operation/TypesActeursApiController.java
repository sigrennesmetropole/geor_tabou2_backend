package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesActeursApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeActeur;
import rm.tabou2.service.tabou.operation.TypeActeurService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeActeurEntity;
import rm.tabou2.storage.tabou.item.TypeActeurCriteria;

@Controller
public class TypesActeursApiController implements TypesActeursApi {

    @Autowired
    TypeActeurService service;

    @Override
    public ResponseEntity<PageResult> searchTypesActeurs(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        TypeActeurCriteria criteria = new TypeActeurCriteria();
        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeActeurEntity.class);

        Page<TypeActeur> page = service.searchTypesActeurs(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

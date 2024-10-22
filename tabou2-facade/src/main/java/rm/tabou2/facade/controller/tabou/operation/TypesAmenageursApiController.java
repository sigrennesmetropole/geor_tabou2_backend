package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypesAmenageursApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.operation.TypeAmenageurService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.TypeAmenageurEntity;
import rm.tabou2.storage.tabou.item.TypeAmenageurCriteria;
import rm.tabou2.service.dto.TypeAmenageur;

@RestController
public class TypesAmenageursApiController implements TypesAmenageursApi {

    @Autowired
    TypeAmenageurService typeAmenageurService;

    @Override
    public ResponseEntity<PageResult> searchTypesAmenageurs(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        TypeAmenageurCriteria criteria = new TypeAmenageurCriteria();

        criteria.setLibelle(libelle);
        criteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeAmenageurEntity.class);

        Page<TypeAmenageur> page = typeAmenageurService.searchTypesAmenageurs(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

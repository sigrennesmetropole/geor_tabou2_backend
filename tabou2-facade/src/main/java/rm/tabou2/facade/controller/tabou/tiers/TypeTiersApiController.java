package rm.tabou2.facade.controller.tabou.tiers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypesTiersApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeTiers;
import rm.tabou2.service.tabou.tiers.TypeTiersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

@RestController
public class TypeTiersApiController implements TypesTiersApi {

    @Autowired
    private TypeTiersService typeTiersService;

    @Override
    public ResponseEntity<TypeTiers> createTypeTiers(TypeTiers typeTiers) throws Exception {
        return new ResponseEntity<>(typeTiersService.createTypeTiers(typeTiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeTiers> updateTypeTiers(TypeTiers typeTiers) throws Exception {
        return new ResponseEntity<>(typeTiersService.updateTypeTiers(typeTiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTypeTiers(String libelle, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeTiersEntity.class);

        Page<TypeTiers> page = typeTiersService.searchTypeTiers(libelle, inactif, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeTiers> getTypeTiersById(Long typeTiersId) throws Exception {
        return new ResponseEntity<>(typeTiersService.getById(typeTiersId), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<TypeTiers> inactivateTypeTiers(Long typeTiersId) throws Exception {
        return new ResponseEntity<>(typeTiersService.inactivateTypeTiers(typeTiersId), HttpStatus.OK);
    }
}

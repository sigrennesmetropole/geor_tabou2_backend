package rm.tabou2.facade.controller.tabou.tiers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TiersApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.TiersCriteria;

import javax.validation.Valid;

@Controller
public class TiersApiController implements TiersApi {

    @Autowired
    private TiersService tiersService;

    @Autowired
    private OperationTiersService operationTiersService;

    @Override
    public ResponseEntity<Tiers> createTiers(@Valid Tiers tiers) throws Exception {

        return new ResponseEntity<>(tiersService.addTiers(tiers), HttpStatus.OK);

    }


    @Override
    public ResponseEntity<Tiers> updateTiers(@Valid Tiers tiers) throws Exception {

        return new ResponseEntity<>(tiersService.addTiers(tiers), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchTiers(@Valid String nom, @Valid Boolean tiersPrive, @Valid String adresseVille, @Valid Boolean inactif, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        TiersCriteria tiersCriteria = new TiersCriteria();
        tiersCriteria.setNom(nom);
        tiersCriteria.setTiersPrive(tiersPrive);
        tiersCriteria.setAdresseVille(adresseVille);
        tiersCriteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TiersEntity.class);

        Page<Tiers> page = tiersService.searchTiers(tiersCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);


    }

    @Override
    public ResponseEntity<Tiers> getTiersById(Long tiersId) throws Exception {
        return new ResponseEntity<>(tiersService.getTiersById(tiersId), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Tiers> inactivateTiers(Long tiersId) throws Exception {
        return new ResponseEntity<>(tiersService.inactivateTiers(tiersId), HttpStatus.OK);
    }


}

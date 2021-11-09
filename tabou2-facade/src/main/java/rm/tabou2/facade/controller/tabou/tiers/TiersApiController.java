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
import rm.tabou2.service.dto.TiersV1;
import rm.tabou2.service.mapper.tabou.tiers.TiersV1Mapper;
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
    private TiersV1Mapper tiersV1Mapper;


    @Override
    public ResponseEntity<TiersV1> createTiers(@Valid TiersV1 tiersv1) throws Exception {
        Tiers tiers = tiersV1Mapper.tiersV1ToTiers(tiersv1);
        tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.createTiers(tiers));
        return new ResponseEntity<>(tiersv1, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Tiers> createTiers2(@Valid Tiers tiers) throws Exception {
        return new ResponseEntity<>(tiersService.createTiers(tiers), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<TiersV1> updateTiers(@Valid TiersV1 tiersv1) throws Exception {
        Tiers tiers = tiersV1Mapper.tiersV1ToTiers(tiersv1);
        tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.updateTiers(tiers));
        return new ResponseEntity<>(tiersv1, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Tiers> updateTiers2(@Valid Tiers tiers) throws Exception {
        return new ResponseEntity<>(tiersService.updateTiers(tiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTiers(@Valid String nom, @Valid Boolean tiersPrive, @Valid String adresseVille, @Valid Boolean inactif, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        TiersCriteria tiersCriteria = new TiersCriteria();
        tiersCriteria.setNom(nom);
        tiersCriteria.setTiersPrive(tiersPrive);
        tiersCriteria.setAdresseVille(adresseVille);
        tiersCriteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TiersEntity.class);

        Page<TiersV1> page = tiersV1Mapper.tiersToTiersV1Page(tiersService.searchTiers(tiersCriteria, pageable), pageable);


        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);


    }

    @Override
    public ResponseEntity<PageResult> searchTiers2(@Valid String nom, @Valid Boolean tiersPrive, @Valid String adresseVille, @Valid Boolean inactif, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
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
    public ResponseEntity<TiersV1> getTiersById(Long tiersId) throws Exception {
        TiersV1 tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.getTiersById(tiersId));
        return new ResponseEntity<>(tiersv1, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Tiers> getTiersById2(Long tiersId) throws Exception {
        Tiers tiers = tiersService.getTiersById(tiersId);
        return new ResponseEntity<>(tiers, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<TiersV1> inactivateTiers(Long tiersId) throws Exception {
        TiersV1 tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.inactivateTiers(tiersId));
        return new ResponseEntity<>(tiersv1, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Tiers> inactivateTiers2(Long tiersId) throws Exception {
        return new ResponseEntity<>(tiersService.inactivateTiers(tiersId), HttpStatus.OK);
    }

}

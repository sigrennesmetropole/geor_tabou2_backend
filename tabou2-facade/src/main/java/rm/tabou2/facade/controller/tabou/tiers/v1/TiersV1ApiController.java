package rm.tabou2.facade.controller.tabou.tiers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import rm.tabou2.facade.api.v1.TiersApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.dto.TiersV1;
import rm.tabou2.service.mapper.tabou.tiers.TiersV1Mapper;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.TiersCriteria;

@RestController
public class TiersV1ApiController implements TiersApi {

	@Autowired
	private TiersService tiersService;

	@Autowired
	private TiersV1Mapper tiersV1Mapper;

	@Override
	public ResponseEntity<TiersV1> createTiersV1(TiersV1 tiersv1) throws Exception {
		Tiers tiers = tiersV1Mapper.tiersV1ToTiers(tiersv1);
		tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.createTiers(tiers));
		return new ResponseEntity<>(tiersv1, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<TiersV1> updateTiersV1(TiersV1 tiersv1) throws Exception {
		Tiers tiers = tiersV1Mapper.tiersV1ToTiers(tiersv1);
		tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.updateTiers(tiers));
		return new ResponseEntity<>(tiersv1, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PageResult> searchTiersV1(String nom, Boolean tiersPrive,
			String adresseVille, Boolean inactif, Integer start, Integer resultsNumber,
			String orderBy, Boolean asc) throws Exception {

		TiersCriteria tiersCriteria = new TiersCriteria();
		tiersCriteria.setNom(nom);
		tiersCriteria.setTiersPrive(tiersPrive);
		tiersCriteria.setAdresseVille(adresseVille);
		tiersCriteria.setInactif(inactif);

		Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TiersEntity.class);

		Page<TiersV1> page = tiersV1Mapper.tiersToTiersV1Page(tiersService.searchTiers(tiersCriteria, pageable),
				pageable);

		return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<TiersV1> getTiersByIdV1(Long tiersId) throws Exception {
		TiersV1 tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.getTiersById(tiersId));
		return new ResponseEntity<>(tiersv1, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<TiersV1> inactivateTiersV1(Long tiersId) throws Exception {
		TiersV1 tiersv1 = tiersV1Mapper.tiersToTiersV1(tiersService.inactivateTiers(tiersId));
		return new ResponseEntity<>(tiersv1, HttpStatus.OK);
	}

}

package rm.tabou2.facade.controller.tabou.tiers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import rm.tabou2.facade.api.v2.TiersApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.dto.ContactTiers;
import rm.tabou2.service.tabou.tiers.ContactTiersService;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.tiers.ContactTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.ContactTiersCriteria;
import rm.tabou2.storage.tabou.item.TiersCriteria;

import javax.validation.Valid;

@RestController
public class TiersV2ApiController implements TiersApi {

	@Autowired
	private TiersService tiersService;

	@Override
	public ResponseEntity<Tiers> createTiers(@Valid Tiers tiers) throws Exception {
		return new ResponseEntity<>(tiersService.createTiers(tiers), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Tiers> updateTiers(@Valid Tiers tiers) throws Exception {
		return new ResponseEntity<>(tiersService.updateTiers(tiers), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PageResult> searchTiers(@Valid String nom, @Valid Boolean tiersPrive,
			@Valid String adresseVille, @Valid Boolean inactif, @Valid Integer start, @Valid Integer resultsNumber,
			@Valid String orderBy, @Valid Boolean asc) throws Exception {
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
		Tiers tiers = tiersService.getTiersById(tiersId);
		return new ResponseEntity<>(tiers, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Tiers> inactivateTiers(Long tiersId) throws Exception {
		return new ResponseEntity<>(tiersService.inactivateTiers(tiersId), HttpStatus.OK);
	}

	@Autowired
	private ContactTiersService contactTiersService;

	@Override
	public ResponseEntity<ContactTiers> createContactTiers(Long tiersId, @Valid ContactTiers contactTiers) throws Exception {
		return new ResponseEntity<>(contactTiersService.createContactTiers(tiersId, contactTiers), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ContactTiers> getContactTiersById(Long tiersId, Long contactTiersId) throws Exception {
		return new ResponseEntity<>(contactTiersService.getContactTiersById(tiersId, contactTiersId), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ContactTiers> inactivateContactTiers(Long tiersId, Long contactTiersId) throws Exception {
		return new ResponseEntity<>(contactTiersService.inactivateContactTiers(tiersId, contactTiersId), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PageResult> searchContactTiers(Long tiersId, String nom, Boolean inactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
		ContactTiersCriteria criteria = new ContactTiersCriteria();
		criteria.setNom(nom);
		criteria.setInactif(inactif);
		criteria.setIdTiers(tiersId);

		Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ContactTiersEntity.class);
		Page<ContactTiers> page = contactTiersService.searchContactTiers(criteria, pageable);

		return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ContactTiers> updateContactTiers(Long tiersId, @Valid ContactTiers contactTiers) throws Exception {
		return new ResponseEntity<>(contactTiersService.updateContactTiers(tiersId, contactTiers), HttpStatus.OK);
	}

}

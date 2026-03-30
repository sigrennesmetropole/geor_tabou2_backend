package rm.tabou2.service.tabou.tiers.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rm.tabou2.service.dto.TypeTiers;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.service.mapper.tabou.tiers.TypeTiersMapper;
import rm.tabou2.service.tabou.tiers.TypeTiersService;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersCustomDao;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

@Service
@RequiredArgsConstructor
public class TypeTiersServiceImpl implements TypeTiersService {

	private final TypeTiersDao typeTiersDao;

	private final TypeTiersCustomDao typeTiersCustomDao;

	private final TypeTiersMapper typeTiersMapper;
	
	private final DateHelper dateHelper;

	@Override
	public TypeTiers createTypeTiers(TypeTiers typeTiers) {

		TypeTiersEntity typeTiersEntity = typeTiersMapper.dtoToEntity(typeTiers);

		typeTiersEntity = typeTiersDao.save(typeTiersEntity);

		return typeTiersMapper.entityToDto(typeTiersEntity);

	}

	@Override
	public TypeTiers getById(long typeTiersId) {

		Optional<TypeTiersEntity> typeTiersOpt = typeTiersDao.findById(typeTiersId);
		if (typeTiersOpt.isEmpty()) {
			throw new NoSuchElementException("Le type tiers  n'existe pas, id=" + typeTiersId);
		}

		return typeTiersMapper.entityToDto(typeTiersOpt.get());

	}

	@Override
	public TypeTiers updateTypeTiers(TypeTiers typeTiers) {

		if (typeTiers.getId() == null) {
			throw new IllegalArgumentException("L'id du type tiers est obligatoire");
		}
		Optional<TypeTiersEntity> typeTiersOpt = typeTiersDao.findById(typeTiers.getId());
		if (typeTiersOpt.isEmpty()) {
			throw new NoSuchElementException("Le type tiers demandé n'existe pas , id=" + typeTiers.getId());
		}

		TypeTiersEntity typeTiersEntity = typeTiersOpt.get();
		typeTiersEntity.setLibelle(typeTiers.getLibelle());

		typeTiersDao.save(typeTiersEntity);

		return typeTiersMapper.entityToDto(typeTiersOpt.get());

	}

	@Override
	public TypeTiers inactivateTypeTiers(Long typeTiersId) {

		Optional<TypeTiersEntity> typeTiersOpt = typeTiersDao.findById(typeTiersId);
		if (typeTiersOpt.isEmpty()) {
			throw new NoSuchElementException("Le type tiers demandé n'existe pas, id=" + typeTiersId);
		}

		TypeTiersEntity typeTiers = typeTiersOpt.get();
		typeTiers.setDateInactif(dateHelper.now());

		typeTiersDao.save(typeTiers);

		return typeTiersMapper.entityToDto(typeTiers);

	}

	@Override
	public Page<TypeTiers> searchTypeTiers(String libelle, Boolean inacatif, Pageable pageable) {

		return typeTiersMapper.entitiesToDto(typeTiersCustomDao.searchTypeTiers(libelle, inacatif, pageable), pageable);

	}

}

package rm.tabou2.service.tabou.logement.impl;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rm.tabou2.service.dto.TypeLogement;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.service.mapper.tabou.logement.TypeLogementMapper;
import rm.tabou2.service.tabou.logement.TypeLogementService;
import rm.tabou2.storage.tabou.dao.logement.TypeLogementDao;
import rm.tabou2.storage.tabou.entity.logement.TypeLogementEntity;

@Service
@RequiredArgsConstructor
public class TypeLogementServiceImpl implements TypeLogementService {

	private final TypeLogementDao typeLogementDao;


	private final TypeLogementMapper typeLogementMapper;

	private final DateHelper dateHelper;

	@Override
	public TypeLogement createTypeLogement(TypeLogement typeLogement) {

		validateMandatoryFields(typeLogement);

		TypeLogementEntity entity = typeLogementMapper.dtoToEntity(typeLogement);

		entity = typeLogementDao.save(entity);

		return typeLogementMapper.entityToDto(entity);

	}

	@Override
	public TypeLogement getById(long typeLogementId) {

		Optional<TypeLogementEntity> entityOpt = typeLogementDao.findById(typeLogementId);
		if (entityOpt.isEmpty()) {
			throw new NoSuchElementException("Le type de logement n'existe pas, id=" + typeLogementId);
		}

		return typeLogementMapper.entityToDto(entityOpt.get());

	}

	@Override
	public TypeLogement updateTypeLogement(TypeLogement typeLogement) {

		if (typeLogement.getId() == null) {
			throw new IllegalArgumentException("L'id du type de logement est obligatoire");
		}

		validateMandatoryFields(typeLogement);

		Optional<TypeLogementEntity> entityOpt = typeLogementDao.findById(Objects.requireNonNull(typeLogement.getId()));
		if (entityOpt.isEmpty()) {
			throw new NoSuchElementException("Le type de logement demandé n'existe pas, id=" + typeLogement.getId());
		}

		TypeLogementEntity entity = entityOpt.get();
		typeLogementMapper.dtoToEntity(typeLogement, entity);

		typeLogementDao.save(entity);

		return typeLogementMapper.entityToDto(entity);

	}

	@Override
	public TypeLogement inactivateTypeLogement(Long typeLogementId) {

		Optional<TypeLogementEntity> entityOpt = typeLogementDao.findById(typeLogementId);
		if (entityOpt.isEmpty()) {
			throw new NoSuchElementException("Le type de logement demandé n'existe pas, id=" + typeLogementId);
		}

		TypeLogementEntity entity = entityOpt.get();
		entity.setDateFin(dateHelper.now());

		typeLogementDao.save(entity);

		return typeLogementMapper.entityToDto(entity);

	}

	@Override
	public Page<TypeLogement> searchTypeLogements(String libelle, Boolean actifUniquement, Pageable pageable) {

		return typeLogementMapper.entitiesToDto(typeLogementDao.searchTypeLogements(libelle, actifUniquement, pageable), pageable);

	}

	/**
	 * Validation des champs obligatoires d'un type de logement.
	 *
	 * @param typeLogement DTO à valider
	 */
	private void validateMandatoryFields(TypeLogement typeLogement) {
		if (StringUtils.isBlank(typeLogement.getCode())) {
			throw new IllegalArgumentException("Le code du type de logement est obligatoire");
		}
		if (StringUtils.isBlank(typeLogement.getLibelle())) {
			throw new IllegalArgumentException("Le libellé du type de logement est obligatoire");
		}
		if (typeLogement.getDateDebut() == null) {
			throw new IllegalArgumentException("La date de début du type de logement est obligatoire");
		}
	}

}


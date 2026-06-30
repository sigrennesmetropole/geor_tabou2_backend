package rm.tabou2.service.tabou.logement.impl;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rm.tabou2.service.dto.TypeAccessionLogement;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.service.mapper.tabou.logement.TypeAccessionLogementMapper;
import rm.tabou2.service.tabou.logement.TypeAccessionLogementService;
import rm.tabou2.storage.tabou.dao.logement.TypeAccessionLogementDao;
import rm.tabou2.storage.tabou.entity.logement.TypeAccessionLogementEntity;
import rm.tabou2.storage.tabou.item.TypeAccessionLogementCriteria;

@Service
@RequiredArgsConstructor
public class TypeAccessionLogementServiceImpl implements TypeAccessionLogementService {

	private final TypeAccessionLogementDao typeAccessionLogementDao;


	private final TypeAccessionLogementMapper typeAccessionLogementMapper;

	private final DateHelper dateHelper;

	@Override
	public TypeAccessionLogement createTypeAccessionLogement(TypeAccessionLogement typeAccessionLogement) {

		validateMandatoryFields(typeAccessionLogement);

		TypeAccessionLogementEntity entity = typeAccessionLogementMapper.dtoToEntity(typeAccessionLogement);

		entity = typeAccessionLogementDao.save(entity);

		return typeAccessionLogementMapper.entityToDto(entity);

	}

	@Override
	public TypeAccessionLogement getById(long typeAccessionLogementId) {

		Optional<TypeAccessionLogementEntity> entityOpt = typeAccessionLogementDao.findById(typeAccessionLogementId);
		if (entityOpt.isEmpty()) {
			throw new NoSuchElementException("Le type d'accession logement n'existe pas, id=" + typeAccessionLogementId);
		}

		return typeAccessionLogementMapper.entityToDto(entityOpt.get());

	}

	@Override
	public TypeAccessionLogement updateTypeAccessionLogement(TypeAccessionLogement typeAccessionLogement) {

		if (typeAccessionLogement.getId() == null) {
			throw new IllegalArgumentException("L'id du type d'accession logement est obligatoire");
		}

		validateMandatoryFields(typeAccessionLogement);

		Optional<TypeAccessionLogementEntity> entityOpt = typeAccessionLogementDao.findById(Objects.requireNonNull(typeAccessionLogement.getId()));
		if (entityOpt.isEmpty()) {
			throw new NoSuchElementException("Le type d'accession logement demandé n'existe pas, id=" + typeAccessionLogement.getId());
		}

		TypeAccessionLogementEntity entity = entityOpt.get();
		typeAccessionLogementMapper.dtoToEntity(typeAccessionLogement, entity);

		typeAccessionLogementDao.save(entity);

		return typeAccessionLogementMapper.entityToDto(entity);

	}

	@Override
	public TypeAccessionLogement inactivateTypeAccessionLogement(Long typeAccessionLogementId) {

		Optional<TypeAccessionLogementEntity> entityOpt = typeAccessionLogementDao.findById(typeAccessionLogementId);
		if (entityOpt.isEmpty()) {
			throw new NoSuchElementException("Le type d'accession logement demandé n'existe pas, id=" + typeAccessionLogementId);
		}

		TypeAccessionLogementEntity entity = entityOpt.get();
		entity.setDateFin(dateHelper.now());

		typeAccessionLogementDao.save(entity);

		return typeAccessionLogementMapper.entityToDto(entity);

	}

	@Override
	public Page<TypeAccessionLogement> searchTypeAccessionLogements(String libelle, Boolean actifUniquement, Pageable pageable) {

		TypeAccessionLogementCriteria criteria = TypeAccessionLogementCriteria.builder()
				.libelle(libelle)
				.actifUniquement(actifUniquement)
				.build();

		return typeAccessionLogementMapper.entitiesToDto(typeAccessionLogementDao.searchTypeAccessionLogements(criteria, pageable), pageable);

	}

	/**
	 * Validation des champs obligatoires d'un type d'accession logement.
	 *
	 * @param typeAccessionLogement DTO à valider
	 */
	private void validateMandatoryFields(TypeAccessionLogement typeAccessionLogement) {
		if (StringUtils.isBlank(typeAccessionLogement.getCode())) {
			throw new IllegalArgumentException("Le code du type d'accession logement est obligatoire");
		}
		if (StringUtils.isBlank(typeAccessionLogement.getLibelle())) {
			throw new IllegalArgumentException("Le libellé du type d'accession logement est obligatoire");
		}
		if (typeAccessionLogement.getDateDebut() == null) {
			throw new IllegalArgumentException("La date de début du type d'accession logement est obligatoire");
		}
	}

}


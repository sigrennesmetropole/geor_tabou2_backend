package rm.tabou2.service.tabou.plh.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.exception.AppServiceNotFoundException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.plh.TypePlhHelper;
import rm.tabou2.service.helper.programme.ProgrammePlannerHelper;
import rm.tabou2.service.mapper.tabou.plh.TypePLHMapper;
import rm.tabou2.service.tabou.plh.PLHService;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.TypePLHCustomDao;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.TypePLHCriteria;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PLHServiceImpl implements PLHService {

	private final TypePLHDao typePLHDao;

	private final ProgrammeDao programmeDao;

	private final TypePLHMapper typePLHMapper;

	private final AuthentificationHelper authentificationHelper;

	private final TypePlhHelper typePlhHelper;

	private final TypePLHCustomDao typePLHCustomDao;

	private final ProgrammePlannerHelper programmePlannerHelper;

	private final PermisConstruireDao permisConstruireDao;

	@Override
	@Transactional(readOnly = false)
	public TypePLH createTypePLH(TypePLH typePLH) throws AppServiceException {
		TypePLHEntity typePLHEntity = getTypePLHEntity(typePLH);

		// Vérification qu'aucun type PLH avec un TypeAttribut VALUE n'a pas de fils
		typePlhHelper.checkTypeAttributPLH(typePLHEntity);

		// Enregistrement en BDD
		try {
			typePLHEntity = typePLHDao.save(typePLHEntity);
		} catch (DataAccessException e) {
			throw new AppServiceException("Impossible d'ajouter un type PLH ", e);
		}
		return typePLHMapper.entityToDto(typePLHEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public TypePLH createTypePLHWithParent(TypePLH typePLH, long parentId) throws AppServiceException {
		TypePLHEntity typePLHEntity = getTypePLHEntity(typePLH);

		// Vérification qu'aucun type PLH avec un TypeAttribut VALUE n'a pas de fils
		typePlhHelper.checkTypeAttributPLH(typePLHEntity);

		// Enregistrement en BDD
		try {
			typePLHEntity = typePLHDao.save(typePLHEntity);
		} catch (DataAccessException e) {
			throw new AppServiceException("Impossible d'ajouter un type PLH ", e);
		}

		// rajout du type PLH en tant que fils
		TypePLHEntity parentEntity = typePLHDao.findOneById(parentId);
		typePlhHelper.checkTypeAttributPLH(parentEntity);
		parentEntity.addTypePLHToFils(typePLHEntity);
		typePLHDao.save(parentEntity);

		return typePLHMapper.entityToDto(typePLHEntity);
	}

	@Override
	public TypePLH getTypePLH(long id) throws AppServiceException {
		TypePLHEntity typePLHEntity = typePLHDao.findOneById(id);
		return typePLHMapper.entityToDto(typePLHEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public TypePLH updateTypePLH(TypePLH typePLH) throws AppServiceException {
		TypePLHEntity typePLHEntity = typePLHMapper.dtoToEntity(typePLH);
		if (!authentificationHelper.hasEditAccess()) {
			throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification " +
					"du type de PLH " + typePLH.getLibelle());
		}

		// Vérification qu'aucun type PLH avec un TypeAttribut VALUE n'a pas de fils
		typePlhHelper.checkTypeAttributPLH(typePLHEntity);

		// Enregistrement en BDD
		try {
			typePLHEntity = typePLHDao.save(typePLHEntity);
		} catch (DataAccessException e) {
			throw new AppServiceException("Impossible d'éditer le type de PLH " + typePLH.getId(), e);
		}
		return typePLHMapper.entityToDto(typePLHEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteTypePLH(long id) throws AppServiceException {
		if (!authentificationHelper.hasContributeurRole()) {
			throw new AppServiceException("Utilisateur non autorisé à supprimer le type PLH ",
					AppServiceExceptionsStatus.FORBIDDEN);
		}

		// Vérification qu'aucun programmes ne possède le type PLH à supprimer
		List<ProgrammeEntity> programmes = programmeDao.findAll();
		for(ProgrammeEntity programmeEntity : programmes) {
			if (programmeEntity.lookupOptionalTypePLHById(id).isEmpty()) {
				throw new AppServiceException("Suppression du Type PLH id = " + id +
						"interdite car toujours rattaché à un ou des programmes", AppServiceExceptionsStatus.FORBIDDEN);
			}
		}

		//Suppression du type PLH
		TypePLHEntity typePLHEntity = typePLHDao.findOneById(id);
		typePLHDao.delete(typePLHEntity);
	}

	@Override
	public TypePLH searchTypePLHParent(long idfils) throws AppServiceException {
		if (typePLHDao.findOneById(idfils) == null) {
			throw new AppServiceException("Recherche impossible, l'id du fils id = " + idfils +
					" ne correspond pas à un type PLH");
		}
		return typePLHMapper.entityToDto(typePLHCustomDao.getParentById(idfils));
	}

	@Override
	public Page<TypePLH> searchTypePLHs(TypePLHCriteria criteria, Pageable pageable) throws AppServiceException {

		if (criteria.getProgrammeId() != null) {
			assignProgrammeDates(criteria);
		}
		return typePLHMapper.entitiesToDto(typePLHCustomDao.searchTypePLHs(criteria, pageable), pageable);
	}

	private void assignProgrammeDates(TypePLHCriteria criteria) throws AppServiceNotFoundException {
		ProgrammeEntity programmeEntity = programmeDao.findById(criteria.getProgrammeId()).orElseThrow(
				AppServiceNotFoundException::new);

		List<PermisConstruireEntity> permis = permisConstruireDao.findAllByNumAds(programmeEntity.getNumAds());

		if (CollectionUtils.isNotEmpty(permis)) {
			criteria.setDateDebut(programmePlannerHelper.computeDocDate(permis));
			criteria.setDateFin(programmePlannerHelper.computeDatDate(permis));
		}

	}

	private TypePLHEntity getTypePLHEntity(TypePLH typePLH) {
		TypePLHEntity typePLHEntity = typePLHMapper.dtoToEntity(typePLH);
		if (!authentificationHelper.hasEditAccess()) {
			throw new AccessDeniedException("L'utilisateur n'a pas les droits de création d'un type de PLH "
					+ typePLH.getLibelle());
		}
		return typePLHEntity;
	}
}

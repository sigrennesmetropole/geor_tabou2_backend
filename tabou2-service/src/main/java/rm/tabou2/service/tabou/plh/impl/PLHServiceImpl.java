package rm.tabou2.service.tabou.plh.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.plh.TypePlhHelper;
import rm.tabou2.service.mapper.tabou.plh.TypePLHMapper;
import rm.tabou2.service.tabou.plh.PLHService;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

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

	@Override
	@Transactional(readOnly = false)
	public TypePLH createTypePLH(TypePLH typePLH) throws AppServiceException {
		TypePLHEntity typePLHEntity = typePLHMapper.dtoToEntity(typePLH);
		if (!authentificationHelper.hasEditAccess()) {
			throw new AccessDeniedException("L'utilisateur n'a pas les droits de création d'un type de PLH "
					+ typePLH.getLibelle());
		}

		// Vérification qu'aucun type PLH avec un TypeAttribut VALUE n'a pas de fils
		typePlhHelper.checkAucunAttributVALUEaUnFils(typePLHEntity);

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
		TypePLHEntity typePLHEntity = typePLHMapper.dtoToEntity(typePLH);
		if (!authentificationHelper.hasEditAccess()) {
			throw new AccessDeniedException("L'utilisateur n'a pas les droits de création d'un type de PLH "
					+ typePLH.getLibelle());
		}

		// Vérification qu'aucun type PLH avec un TypeAttribut VALUE n'a pas de fils
		typePlhHelper.checkAucunAttributVALUEaUnFils(typePLHEntity);

		// Enregistrement en BDD
		try {
			typePLHEntity = typePLHDao.save(typePLHEntity);
		} catch (DataAccessException e) {
			throw new AppServiceException("Impossible d'ajouter un type PLH ", e);
		}

		// rajout du type PLH en tant que fils
		TypePLHEntity parentEntity = typePLHDao.findOneById(parentId);
		typePlhHelper.checkAucunAttributVALUEaUnFils(parentEntity);
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
		typePlhHelper.checkAucunAttributVALUEaUnFils(typePLHEntity);

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
			throw new AppServiceException("Utilisateur non autorisé à supprimer le type PLH ", AppServiceExceptionsStatus.FORBIDDEN);
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
}

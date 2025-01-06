package rm.tabou2.service.tabou.plh.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.mapper.tabou.plh.TypePLHMapper;
import rm.tabou2.service.tabou.plh.PLHService;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.entity.plh.TypeAttributPLH;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

@Service
public class PLHServiceImpl implements PLHService {

	@Autowired
	private TypePLHDao typePLHDao;

	@Autowired
	private TypePLHMapper typePLHMapper;

	@Autowired
	private AuthentificationHelper authentificationHelper;

	@Override
	@Transactional(readOnly = false)
	public TypePLH createTypePLH(TypePLH typePLH) throws AppServiceException {
		if (!authentificationHelper.hasEditAccess()) {
			throw new AccessDeniedException("L'utilisateur n'a pas les droits de création d'un type de PLH "
					+ typePLH.getLibelle());
		}

		TypePLHEntity typePLHEntity = typePLHMapper.dtoToEntity(typePLH);
		if (typePLHEntity.getFils() != null && typePLH.getTypeAttributPLH().equals(TypePLH.TypeAttributPLHEnum.VALUE)) {
			typePLHEntity.setTypeAttributPLH(TypeAttributPLH.CATEGORY);
		}

		try {
			typePLHEntity = typePLHDao.save(typePLHEntity);
		} catch (DataAccessException e) {
			throw new AppServiceException("Impossible d'ajouter un type PLH ", e);
		}

		return typePLHMapper.entityToDto(typePLHEntity);
	}

	@Override
	public TypePLH getTypePLH(int id) throws AppServiceException {
		TypePLHEntity typePLHEntity = typePLHDao.getReferenceById((long)id);
		return typePLHMapper.entityToDto(typePLHEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public TypePLH updateTypePLH(TypePLH typePLH) throws AppServiceException {
		if (!authentificationHelper.hasEditAccess()) {
			throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification " +
					"du type de PLH " + typePLH.getLibelle());
		}

		TypePLHEntity typePLHEntity = typePLHDao.getReferenceById(typePLH.getId());
		typePLHMapper.dtoToEntity(typePLH, typePLHEntity);

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
	public void deleteTypePLH(int id) throws AppServiceException {
		if (authentificationHelper.hasContributeurRole()) {
			//Suppression de l'évènement
			TypePLHEntity typePLHEntity = typePLHDao.getReferenceById((long)id);
			typePLHDao.delete(typePLHEntity);
		} else {
			throw new AppServiceException("Utilisateur non autorisé à supprimer l'évènement ", AppServiceExceptionsStatus.FORBIDDEN);
		}
	}
}

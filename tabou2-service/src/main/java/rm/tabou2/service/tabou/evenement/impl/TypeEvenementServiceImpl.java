package rm.tabou2.service.tabou.evenement.impl;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.evenement.TypeEvenementRigthsHelper;
import rm.tabou2.service.mapper.tabou.evenement.TypeEvenementMapper;
import rm.tabou2.service.tabou.evenement.TypeEvenementService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementCustomDao;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

import java.util.Date;

@Service
@Validated
@Transactional(readOnly = true)
public class TypeEvenementServiceImpl implements TypeEvenementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TypeEvenementServiceImpl.class);

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private TypeEvenementCustomDao typeEvenementCustomDao;

    @Autowired
    private TypeEvenementMapper typeEvenementMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private TypeEvenementRigthsHelper typeEvenementRigthsHelper;

    @Override
    public TypeEvenement getTypeEvenementById(long typeEvenementId) {

        TypeEvenementEntity typeEvenementEntity = getTypeEvenementEntityById(typeEvenementId);

        return (typeEvenementMapper.entityToDto(typeEvenementEntity));

    }

    @Override
    @Transactional
    public TypeEvenement createTypeEvenement(TypeEvenement typeEvenement) throws AppServiceException {

        //Vérification des autorisations
        if (!authentificationHelper.hasEditAccess()) {
            throw new AppServiceException("Utilisateur non autorisé à créer un type d'évènement ", AppServiceExceptionsStatus.FORBIDDEN);
        }

        TypeEvenementEntity typeEvenementEntity = typeEvenementMapper.dtoToEntity(typeEvenement);
        typeEvenementEntity.setSysteme(false);

        try {
            typeEvenementEntity = typeEvenementDao.save(typeEvenementEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter un type evenement ", e);
        }

        return typeEvenementMapper.entityToDto(typeEvenementEntity);

    }

    @Override
    @Transactional
    public TypeEvenement updateTypeEvenement(TypeEvenement typeEvenement) throws AppServiceException {

        //Vérification des autorisations
        if (!authentificationHelper.hasEditAccess()) {
            throw new AppServiceException("Utilisateur non autorisé à créer un type d'évènement ", AppServiceExceptionsStatus.FORBIDDEN);
        }

        TypeEvenementEntity typeEvenementEntity = getTypeEvenementEntityById(typeEvenement.getId());

        //On recopie uniquement les valeurs à updater
        typeEvenementEntity.setCode(typeEvenement.getCode());
        typeEvenementEntity.setLibelle(typeEvenement.getLibelle());
        typeEvenementEntity.setDateInactif(typeEvenement.getDateInactif());


        // Enregistrement en BDD
        try {
            typeEvenementEntity = typeEvenementDao.save(typeEvenementEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'éditer le type de evenement " + typeEvenement.getId(), e);
        }

        return typeEvenementMapper.entityToDto(typeEvenementEntity);
    }

    @Override
    @Transactional
    public TypeEvenement inactivateTypeEvenement(Long typeEvenementId) throws AppServiceException {

        //Vérification des autorisations
        if (!authentificationHelper.hasEditAccess()) {
            throw new AppServiceException("Utilisateur non autorisé à créer un type d'évènement ", AppServiceExceptionsStatus.FORBIDDEN);
        }

        TypeEvenementEntity typeEvenementEntity = getTypeEvenementEntityById(typeEvenementId);

        typeEvenementEntity.setDateInactif(new Date());

        // Enregistrement en BDD
        try {
            typeEvenementEntity = typeEvenementDao.save(typeEvenementEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de rendre inactive le type evenement " + typeEvenementEntity.getId(), e);
        }

        return typeEvenementMapper.entityToDto(typeEvenementEntity);
    }

    @Override
    public Page<TypeEvenement> searchTypeEvenement(TypeEvenementCriteria typeEvenementCriteria, Pageable pageable) {

        if (BooleanUtils.isTrue(typeEvenementCriteria.getSysteme())) {
            typeEvenementCriteria.setSysteme(false);
            LOGGER.warn("Accès non autorisé à des types d'événement système");
        }
        Page<TypeEvenementEntity> typesEvenements = typeEvenementCustomDao.searchTypeEvenement(typeEvenementCriteria, pageable);

        return typeEvenementMapper.entitiesToDto(typesEvenements, pageable);

    }

    private TypeEvenementEntity getTypeEvenementEntityById(long typeEvenementId) {

        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(typeEvenementId);
        if (!typeEvenementRigthsHelper.canGetTypeEvenement(typeEvenementEntity)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer un type d'évènement id = " + typeEvenementId);
        }

        return typeEvenementEntity;
    }

}

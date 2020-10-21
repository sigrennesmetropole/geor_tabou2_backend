package rm.tabou2.service.tabou.evenement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.mapper.tabou.evenement.TypeEvenementMapper;
import rm.tabou2.service.tabou.evenement.TypeEvenementService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementCustomDao;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TypeEvenementServiceImpl implements TypeEvenementService {


    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private TypeEvenementCustomDao typeEvenementCustomDao;

    @Autowired
    private TypeEvenementMapper typeEvenementMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public TypeEvenement createTypeEvenement(TypeEvenement typeEvenement) throws AppServiceException {

        //Vérification des autorisations
        if (!authentificationHelper.hasEditAccess()) {
            throw new AppServiceException("Utilisateur non autorisé à créer un type d'évènement ", AppServiceExceptionsStatus.FORBIDDEN);
        }

        TypeEvenementEntity typeEvenementEntity = typeEvenementMapper.dtoToEntity(typeEvenement);

        //Vérification des champs obligatoires
        if (typeEvenementEntity.getLibelle().isEmpty()) {
            throw new AppServiceException("Le champ libelle est manquant");
        }

        //Historisation
        typeEvenementEntity.setCreateDate(new Date());
        typeEvenementEntity.setCreateUser(authentificationHelper.getConnectedUsername());

        try {
            typeEvenementEntity = typeEvenementDao.save(typeEvenementEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter un type evenement ", e);
        }

        return typeEvenementMapper.entityToDto(typeEvenementEntity);

    }

    @Override
    public TypeEvenement updateTypeEvenement(TypeEvenement typeEvenement) throws AppServiceException {

        TypeEvenementEntity typeEvenementEntity;

        Optional<TypeEvenementEntity> typeEvenementEntityOpt = typeEvenementDao.findById(typeEvenement.getId());

        if (typeEvenementEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le type de evenement id = " + typeEvenement.getId() + " n'existe pas");
        } else {
            typeEvenementEntity = typeEvenementEntityOpt.get();
        }

        typeEvenementEntity.setDateInactif(typeEvenement.getDateInactivite());

        typeEvenementEntity.setLibelle(typeEvenement.getLibelle());

        // Enregistrement en BDD
        try {
            typeEvenementEntity = typeEvenementDao.save(typeEvenementEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'éditer le type de evenement " + typeEvenement.getId(), e);
        }

        return typeEvenementMapper.entityToDto(typeEvenementEntity);
    }

    @Override
    public TypeEvenement inactivateTypeEvenement(Long typeEvenementId) throws AppServiceException {



        TypeEvenementEntity typeEvenement;

        Optional<TypeEvenementEntity> typeEvenementOpt = typeEvenementDao.findById(typeEvenementId);
        if (typeEvenementOpt.isEmpty()) {
            throw new NoSuchElementException("Le type de evenement id=" + typeEvenementId + " n'existe pas");
        } else {
            typeEvenement = typeEvenementOpt.get();
        }

        typeEvenement.setDateInactif(new Date());

        // Enregistrement en BDD
        try {
            typeEvenement = typeEvenementDao.save(typeEvenement);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de rendre inactive le type evenement " + typeEvenement.getId(), e);
        }

        return typeEvenementMapper.entityToDto(typeEvenement);
    }

    @Override
    public Page<TypeEvenement> searchTypeEvenement(TypeEvenementCriteria typeEvenementCriteria, Pageable pageable) {

        Page<TypeEvenementEntity> typesEvenements = typeEvenementCustomDao.searchTypeEvenement(typeEvenementCriteria, pageable);

        return typeEvenementMapper.entitiesToDto(typesEvenements, pageable);

    }
    
}

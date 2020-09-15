package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.EvenementOperationService;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.mapper.EvenementOperationMapper;
import rm.tabou2.service.utils.AuthentificationUtils;
import rm.tabou2.storage.tabou.dao.EvenementOperationDao;
import rm.tabou2.storage.tabou.dao.OperationDao;
import rm.tabou2.storage.tabou.dao.TypeEvenementDao;
import rm.tabou2.storage.tabou.entity.EvenementOperationEntity;
import rm.tabou2.storage.tabou.entity.OperationEntity;
import rm.tabou2.storage.tabou.entity.TypeEvenementEntity;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EvenementOperationServiceImpl implements EvenementOperationService {

    @Autowired
    private EvenementOperationDao evenementOperationDao;

    @Autowired
    private EvenementOperationMapper evenementOperationMapper;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private AuthentificationUtils authentificationUtils;

    @Override
    public List<Evenement> getByOperationId(Long operationId){

        Pageable pageable = PageRequest.of(0, 10);

        List<EvenementOperationEntity> evenementOperationEntities = evenementOperationDao.findByOperationId(operationId, pageable);

        return evenementOperationMapper.entitiesToDto(evenementOperationEntities);

    }

    @Override
    public Evenement addEvenement(Evenement evenement, Long operationId) throws AppServiceException {

        EvenementOperationEntity evenementOperationEntity = new EvenementOperationEntity();

        // Date
        evenementOperationEntity.setCreateDate(new Date());
        evenementOperationEntity.setModifDate(new Date());

        //Utilisateur
        evenementOperationEntity.setCreateUser(authentificationUtils.getConnectedUsername());
        evenementOperationEntity.setModifUser(authentificationUtils.getConnectedUsername());

        // Operation
        Optional<OperationEntity> operationEntityOpt = operationDao.findById(operationId);
        if (operationEntityOpt.isEmpty()) {
            throw new AppServiceException("L'opération n'existe pas, id=" + operationId );
        } else {
            evenementOperationEntity.setOperation(operationEntityOpt.get());
        }

        // Event date
        evenementOperationEntity.setEventDate(evenement.getEventDate());

        //Description
        evenementOperationEntity.setDescription(evenement.getDescription());

        //system
        evenementOperationEntity.setSysteme(false);

        //type evenement
        Optional<TypeEvenementEntity> typeEvenementEntityOpt = typeEvenementDao.findById(evenement.getIdType());
        if (typeEvenementEntityOpt.isEmpty()) {
            throw new AppServiceException("L' idEventType = " + evenement.getIdType() + " n'existe pas");
        } else {
            evenementOperationEntity.setTypeEvenement(typeEvenementEntityOpt.get());
        }

        // Enregistrement en BDD
        try {
            evenementOperationEntity = evenementOperationDao.save(evenementOperationEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter l'évènement Opération, IdEvent =  "
                    + evenement.getIdEvent(), e);
        }

        return evenementOperationMapper.entityToDto(evenementOperationEntity);
    }

    @Override
    public Evenement editEvenement(Evenement evenement, Long operationId) throws AppServiceException {

        Optional<EvenementOperationEntity> evenementOperationEntityOpt = evenementOperationDao.findById(evenement.getIdEvent());
        if (evenementOperationEntityOpt.isEmpty()) {
            throw new NoSuchElementException("L'évènement id= " + evenement.getIdEvent() + " n'existe pas");
        }
        EvenementOperationEntity evenementOperationEntity = evenementOperationEntityOpt.get();


        if (Boolean.TRUE.equals(evenementOperationEntity.getSysteme())) {
            throw new AppServiceException("Il n'est pas permis de modifier l'évènement id=" + evenement.getIdEvent());
        }

        //Utilisateur
        evenementOperationEntity.setModifUser(authentificationUtils.getConnectedUsername());

        // Date
        evenementOperationEntity.setModifDate(new Date());

        //Description
        evenementOperationEntity.setDescription(evenement.getDescription());

        // Event Date
        evenementOperationEntity.setEventDate(evenement.getEventDate());


        // Mise à jour des valeurs de l'évènement de l'opération
        try {
            evenementOperationEntity = evenementOperationDao.save(evenementOperationEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de faire la mise à jour de l'évènement Opération, id="
                    + evenement.getIdEvent(), e);
        }

        return evenementOperationMapper.entityToDto(evenementOperationEntity);
    }



}

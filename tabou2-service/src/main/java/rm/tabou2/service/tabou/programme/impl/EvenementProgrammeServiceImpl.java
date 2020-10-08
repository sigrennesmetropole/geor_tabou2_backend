package rm.tabou2.service.tabou.programme.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.tabou.programme.EvenementProgrammeService;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.mapper.tabou.programme.EvenementProgrammeMapper;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.storage.tabou.dao.EvenementProgrammeDao;
import rm.tabou2.storage.tabou.dao.ProgrammeDao;
import rm.tabou2.storage.tabou.dao.TypeEvenementDao;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EvenementProgrammeServiceImpl implements EvenementProgrammeService {

    @Autowired
    private EvenementProgrammeDao evenementProgrammeDao;

    @Autowired
    private EvenementProgrammeMapper evenementProgrammeMapper;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public List<Evenement> getByProgrammeId(Long programmeId) {

        Pageable pageable = PageRequest.of(0, 10);

        List<EvenementProgrammeEntity> evenementProgrammeEntities;
        try {
            evenementProgrammeEntities = evenementProgrammeDao.findByProgrammeId(programmeId, pageable);
        } catch (DataAccessException e) {
            throw new AppServiceException(" Impossible de récuperer la liste des évènements d'Opération ", e);
        }
        return evenementProgrammeMapper.entitiesToDto(evenementProgrammeEntities);
    }

    @Override
    public Evenement addByProgrammeId(Evenement evenement, Long programmeId) {

        EvenementProgrammeEntity evenementProgrammeEntity = new EvenementProgrammeEntity();

        // Date
        evenementProgrammeEntity.setCreateDate(new Date());
        evenementProgrammeEntity.setModifDate(new Date());

        //Utilisateur
        evenementProgrammeEntity.setModifUser(authentificationHelper.getConnectedUsername());
        evenementProgrammeEntity.setCreateUser(authentificationHelper.getConnectedUsername());

        // Operation
        Optional<ProgrammeEntity> programmeEntityOpt = programmeDao.findById(programmeId);
        if (programmeEntityOpt.isEmpty()) {
            throw new AppServiceException("Le programme n'existe pas id=" + programmeId);
        } else {
            evenementProgrammeEntity.setProgramme(programmeEntityOpt.get());
        }

        // Event date
        evenementProgrammeEntity.setEventDate(evenement.getEventDate());

        //Description
        evenementProgrammeEntity.setDescription(evenement.getDescription());

        //system
        evenementProgrammeEntity.setSysteme(false);

        //type evenement
        Optional<TypeEvenementEntity> typeEvenementEntityOpt = typeEvenementDao.findById(evenement.getIdType());
        if (typeEvenementEntityOpt.isEmpty()) {
            throw new AppServiceException("L' idEventType = " + evenement.getIdType() + " n'existe pas");
        } else {
            evenementProgrammeEntity.setTypeEvenement(typeEvenementEntityOpt.get());
        }

        // Enregistrement en BDD
        try {
            evenementProgrammeEntity = evenementProgrammeDao.save(evenementProgrammeEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter l'évènement Programme , IdEvent = "
                    + evenement.getIdEvent(), e);
        }

        return evenementProgrammeMapper.entityToDto(evenementProgrammeEntity);
    }

    @Override
    public Evenement editByProgrammeId(Evenement evenement, Long programmeId) {

        EvenementProgrammeEntity evenementProgrammeEntity;

        Optional<EvenementProgrammeEntity> evenementProgrammeEntityOpt = evenementProgrammeDao.findById(evenement.getIdEvent());
        if (evenementProgrammeEntityOpt.isEmpty()) {
            throw new AppServiceException("L' idEvent = " + evenement.getIdEvent() + " n'existe pas");
        } else {
            evenementProgrammeEntity = evenementProgrammeEntityOpt.get();
        }

        // Date
        evenementProgrammeEntity.setModifDate(new Date());

        // User
        evenementProgrammeEntity.setModifUser(authentificationHelper.getConnectedUsername());

        // Event Date
        evenementProgrammeEntity.setEventDate(evenement.getEventDate());

        //Description
        evenementProgrammeEntity.setDescription(evenement.getDescription());

        // //Mise à jour des valeurs de l'évènement Programme
        try {
            evenementProgrammeEntity = evenementProgrammeDao.save(evenementProgrammeEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException(" Impossible de faire la mise à jour de l'évènement Programme, IdEvent = "
                    + evenement.getIdEvent(), e);
        }

        return evenementProgrammeMapper.entityToDto(evenementProgrammeEntity);
    }
}

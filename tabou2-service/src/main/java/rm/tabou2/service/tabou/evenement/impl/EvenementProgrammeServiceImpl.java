package rm.tabou2.service.tabou.evenement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.EvenementProgrammeRigthsHelper;
import rm.tabou2.service.mapper.tabou.programme.EvenementProgrammeMapper;
import rm.tabou2.service.mapper.tabou.programme.ProgrammeMapper;
import rm.tabou2.service.tabou.evenement.EvenementProgrammeService;
import rm.tabou2.storage.tabou.dao.evenement.EvenementProgrammeCustomDao;
import rm.tabou2.storage.tabou.dao.programme.EvenementProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EvenementProgrammeServiceImpl implements EvenementProgrammeService {

    @Autowired
    private EvenementProgrammeCustomDao evenementProgrammeCustomDao;

    @Autowired
    private EvenementProgrammeDao evenementProgrammeDao;

    @Autowired
    private EvenementProgrammeMapper evenementProgrammeMapper;

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private EvenementProgrammeRigthsHelper evenementProgrammeRigthsHelper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public Page<Evenement> searchEvenementsProgramme(long programmeId, Pageable pageable) {

        TypeEvenementCriteria typeEvenementCriteria = new TypeEvenementCriteria();

        if (!authentificationHelper.hasAdministratorRole()) {
            typeEvenementCriteria.setSysteme(false);
        }


        return evenementProgrammeMapper.entitiesToDto(evenementProgrammeCustomDao.searchEvenementsProgramme(programmeId, typeEvenementCriteria, pageable), pageable);

    }

    @Override
    public void deleteEvenementByProgrammeId(long evenementId, long programmeId) throws AppServiceException {

        Optional<EvenementProgrammeEntity> evenementProgrammeOpt = evenementProgrammeDao.findById(evenementId);
        if (evenementProgrammeOpt.isEmpty()) {
            throw new NoSuchElementException("L'evenement id=" + evenementId + " n'existe pas");
        }

        Optional<ProgrammeEntity> programmeEntityOpt = programmeDao.findById(programmeId);
        if (programmeEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le programme id=" + programmeId + " n'existe pas");
        }
        Programme programme = programmeMapper.entityToDto(programmeEntityOpt.get());
        Evenement evenement = evenementProgrammeMapper.entityToDto(evenementProgrammeOpt.get());

        //Permissions : droit en modification et si evenement system
        if (evenementProgrammeRigthsHelper.checkCanUpdateEvenementProgramme(programme, evenement)) {
            //Suppression de l'évènement
            evenementProgrammeDao.deleteById(evenementId);
        } else {
            throw new AppServiceException("Utilisateur non autorisé à supprimer l'évènement ", AppServiceExceptionsStatus.FORBIDDEN);
        }

    }

}

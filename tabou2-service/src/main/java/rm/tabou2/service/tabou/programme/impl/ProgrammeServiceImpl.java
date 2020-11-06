package rm.tabou2.service.tabou.programme.impl;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.EvenementProgrammeRigthsHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.service.mapper.tabou.programme.EvenementProgrammeMapper;
import rm.tabou2.service.mapper.tabou.programme.ProgrammeMapper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.EvenementProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
@Validated
@Transactional(readOnly = true)
public class ProgrammeServiceImpl implements ProgrammeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammeServiceImpl.class);

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private ProgrammeCustomDao programmeCustomDao;

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private EvenementProgrammeDao evenementProgrammeDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private EvenementProgrammeMapper evenementProgrammeMapper;

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    @Autowired
    private EvenementProgrammeRigthsHelper evenementProgrammeRigthsHelper;

    @Autowired
    private ProgrammeService me;

    @Override
    @Transactional
    public Programme createProgramme(Programme programme) {

        // Ajout des valeurs par défaut
        setProgrammeDefaultValues(programme);

        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanCreateProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création du programme " + programme.getNom());
        }

        // Ajout de l'état initial
        String code = BooleanUtils.isTrue(programme.isDiffusionRestreinte()) ? "EN_PROJET_OFF" : "EN_PROJET_PUBLIC";

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findByTypeAndCode(Etape.TypeEnum.START.toString(), code);
        if (etapeProgrammeEntity == null) {
            throw new NoSuchElementException("Aucune étape initiale de type " + Etape.TypeEnum.START.toString() + " n'a été " +
                    "défini pour les programmes avec diffusion restreinte = " + programme.isDiffusionRestreinte());
        }
        ProgrammeEntity programmeEntity = programmeMapper.dtoToEntity(programme);
        programmeEntity.setEtapeProgramme(etapeProgrammeEntity);

        programmeEntity = programmeDao.save(programmeEntity);

        return programmeMapper.entityToDto(programmeEntity);

    }

    @Override
    @Transactional
    public Programme updateProgramme(Programme programme) {

        ProgrammeEntity programmeEntity = programmeDao.findOneById(programme.getId());

        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanUpdateProgramme(programme, programmeEntity.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programme.getNom());
        }

        // Mise à jour de la diffusion restreinte à partir de l'étape
        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findOneById(programme.getEtape().getId());
        programme.setDiffusionRestreinte(null);
        if (etapeProgrammeEntity.isRemoveRestriction()) {
            programme.setDiffusionRestreinte(false);
        }

        programmeMapper.dtoToEntity(programme, programmeEntity);

        programmeEntity = programmeDao.save(programmeEntity);

        return programmeMapper.entityToDto(programmeEntity);
    }

    @Override
    @Transactional
    public Programme updateEtapeOfProgrammeId (long programmeId, long etapeId) {
        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findOneById(etapeId);

        Programme programme = getProgrammeById(programmeId);
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapeProgrammeEntity));
        return me.updateProgramme(programme);
    }

    @Override
    public Programme getProgrammeById(long programmeId) {

        ProgrammeEntity programmeEntity = getProgrammeEntityById(programmeId);

        return programmeMapper.entityToDto(programmeEntity);

    }

    @Override
    public Page<Programme> searchProgrammes(ProgrammeCriteria programmeCriteria, Pageable pageable) {
        // Si l'utilisateur n'a pas le droit de voir les programmes en diffusion restreinte, on filtre sur false
        if (BooleanUtils.isTrue(programmeCriteria.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            programmeCriteria.setDiffusionRestreinte(false);
            LOGGER.warn("Accès non autorisé à des programmes d'accès restreint");
        }
        return programmeMapper.entitiesToDto(programmeCustomDao.searchProgrammes(programmeCriteria, pageable), pageable);
    }

    @Override
    public List<Evenement> getEvenementsByProgrammeId(Long programmeId) {

        ProgrammeEntity programmeEntity = getProgrammeEntityById(programmeId);

        return evenementProgrammeMapper.entitiesToDto(List.copyOf(programmeEntity.getEvenements()));
    }

    @Override
    @Transactional
    public Evenement addEvenementSystemeByProgrammeId(Long programmeId, Evenement evenement) throws AppServiceException {
        evenement.setSysteme(true);
        return addEvenementByProgrammeId(programmeId, evenement);
    }

    @Override
    @Transactional
    public Evenement addEvenementNonSystemeByProgrammeId(Long programmeId, Evenement evenement) throws AppServiceException {
        evenement.setSysteme(false);
        return addEvenementByProgrammeId(programmeId, evenement);
    }

    private Evenement addEvenementByProgrammeId(Long programmeId, Evenement evenement) throws AppServiceException {

        // Programme
        ProgrammeEntity programmeEntity = programmeDao.findOneById(programmeId);
        Programme programme = programmeMapper.entityToDto(programmeEntity);
        if (!programmeRightsHelper.checkCanUpdateProgramme(programme, programme.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de créer un évènement pour le programme id = " + programmeId);
        }

        EvenementProgrammeEntity evenementProgrammeEntity = evenementProgrammeMapper.dtoToEntity(evenement);

        //type evenement
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getIdType());
        evenementProgrammeEntity.setTypeEvenement(typeEvenementEntity);

        // Enregistrement en BDD
        evenementProgrammeEntity = evenementProgrammeDao.save(evenementProgrammeEntity);

        programmeEntity.addEvenementProgramme(evenementProgrammeEntity);

        // Enregistrement en BDD
        try {
            programmeDao.save(programmeEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter l'évènement Programme , IdEvent = "
                    + evenement.getId(), e);
        }

        return evenementProgrammeMapper.entityToDto(evenementProgrammeEntity);
    }

    @Override
    @Transactional
    public Evenement updateEvenementByProgrammeId(long idProgramme, Evenement evenement) throws AppServiceException {

        // Récupération du programme et recherche de l'évènement à modifier
        ProgrammeEntity programmeEntity = programmeDao.findOneById(idProgramme);
        Programme programme = programmeMapper.entityToDto(programmeEntity);

        Optional<EvenementProgrammeEntity> optionalEvenementProgrammeEntity = programmeEntity.lookupEvenementById(evenement.getId());
        if (optionalEvenementProgrammeEntity.isEmpty()) {
            throw new AppServiceException("L'événement id = " + evenement.getId() + " n'existe pas pour le programme id = " + programmeEntity.getId());
        }
        EvenementProgrammeEntity evenementProgrammeEntity = optionalEvenementProgrammeEntity.get();

        if (!evenementProgrammeRigthsHelper.checkCanUpdateEvenementProgramme(programme, evenementProgrammeMapper.entityToDto(evenementProgrammeEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modifier l'évènement id = " + evenementProgrammeEntity.getId()
                    + " du programme id = " + idProgramme);
        }

        // type evenement
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getIdType());
        evenementProgrammeEntity.setTypeEvenement(typeEvenementEntity);

        evenementProgrammeMapper.dtoToEntity(evenement, evenementProgrammeEntity);

        // Mise à jour de l'évènement Programme en base de données
        try {
            evenementProgrammeEntity = evenementProgrammeDao.save(evenementProgrammeEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException(" Impossible de faire la mise à jour de l'évènement Programme, IdEvent = "
                    + evenement.getId(), e);
        }

        return evenementProgrammeMapper.entityToDto(evenementProgrammeEntity);
    }

    private ProgrammeEntity getProgrammeEntityById(long programmeId) {

        ProgrammeEntity programmeEntity = programmeDao.findOneById(programmeId);

        if (!programmeRightsHelper.checkCanGetProgramme(programmeMapper.entityToDto(programmeEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le programme id = " + programmeId);
        }

        return programmeEntity;

    }

    /**
     * Ajout des valeurs par défaut d'un programme
     * @param programme programme
     */
    private void setProgrammeDefaultValues(Programme programme) {
        if (programme.isDiffusionRestreinte() == null) {
            programme.setDiffusionRestreinte(true);
        }
    }

}

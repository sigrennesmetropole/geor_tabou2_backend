package rm.tabou2.service.tabou.programme.impl;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.service.mapper.tabou.programme.ProgrammeMapper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.validator.ValidProgrammeCreation;
import rm.tabou2.service.validator.ValidProgrammeUpdate;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import java.util.NoSuchElementException;

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
    private ProgrammeMapper programmeMapper;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    @Autowired
    private ProgrammeService me;

    @Override
    @Transactional
    public Programme createProgramme(@ValidProgrammeCreation Programme programme) {

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
    public Programme updateProgramme(@ValidProgrammeUpdate Programme programme) {

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

        ProgrammeEntity programmeEntity = programmeDao.findOneById(programmeId);
        Programme programme = programmeMapper.entityToDto(programmeEntity);

        if (!programmeRightsHelper.checkCanGetProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le programme id = " + programmeId);
        }

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

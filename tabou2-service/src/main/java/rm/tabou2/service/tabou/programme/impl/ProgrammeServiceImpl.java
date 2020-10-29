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
    private ProgrammeMapper programmeMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    @Autowired
    private ProgrammeService me;

    @Override
    @Transactional
    public Programme createProgramme(@ValidProgrammeCreation Programme programme) {
        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanCreateProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création du programme " + programme.getNom());
        }

        ProgrammeEntity programmeEntity = programmeMapper.dtoToEntity(programme);

        // Ajout de l'état initial
        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findByType(Etape.TypeEnum.START.toString());
        if (etapeProgrammeEntity == null) {
            throw new NoSuchElementException("Aucune étape initiale de type " + Etape.TypeEnum.START.toString() + " n'a été défini pour les programmes");
        }
        programmeEntity.setEtapeProgramme(etapeProgrammeEntity);

        programmeEntity = programmeDao.save(programmeEntity);

        return programmeMapper.entityToDto(programmeEntity);

    }

    @Override
    @Transactional
    public Programme updateProgramme(@ValidProgrammeUpdate Programme programme) {

        ProgrammeEntity programmeEntity = programmeDao.getById(programme.getId());
        if (programmeEntity == null) {
            throw new NoSuchElementException("Le programme id=" + programme.getId() + " n'existe pas");
        }

        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanUpdateProgramme(programme,
                !programme.isDiffusionRestreinte().equals(programmeEntity.getDiffusionRestreinte()))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programme.getNom());
        }

        programmeMapper.dtoToEntity(programme, programmeEntity);

        programmeEntity = programmeDao.save(programmeEntity);

        return programmeMapper.entityToDto(programmeEntity);
    }

    @Override
    @Transactional
    public Programme updateEtapeOfProgrammeId (long programmeId, Etape etape) {
        Programme programme = getProgrammeById(programmeId);
        programme.setEtape(etape);
        return me.updateProgramme(programme);
    }

    @Override
    public Programme getProgrammeById(long programmeId) {

        Optional<ProgrammeEntity> programmeEntityOpt = programmeDao.findById(programmeId);

        if (programmeEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le programme id=" + programmeId + " n'existe pas");
        }

        return programmeMapper.entityToDto(programmeEntityOpt.get());

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

}

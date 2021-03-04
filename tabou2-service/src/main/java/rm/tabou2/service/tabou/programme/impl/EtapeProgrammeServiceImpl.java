package rm.tabou2.service.tabou.programme.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeRestrictedMapper;
import rm.tabou2.service.tabou.programme.EtapeProgrammeService;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.service.helper.programme.EtapeProgrammeWorkflowHelper;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeCustomDao;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

import java.util.List;

@Service
public class EtapeProgrammeServiceImpl implements EtapeProgrammeService {

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private EtapeProgrammeCustomDao etapeProgrammeCustomDao;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Autowired
    private EtapeProgrammeRestrictedMapper etapeProgrammeRestrictedMapper;

    @Autowired
    private EtapeProgrammeWorkflowHelper etapeProgrammeWorkflowHelper;

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public Page<EtapeRestricted> searchEtapesProgramme(EtapeCriteria etapeCriteria, Pageable pageable) {

        // si l'utilisateur n'a pas le rôle "APPS_TABOU_REFERENT" alors le filtre mode="PUBLIC" s'applique
        if (!authentificationHelper.hasReferentRole()) {
            etapeCriteria.setMode("PUBLIC");
        }

        Page<EtapeProgrammeEntity> etapes = etapeProgrammeCustomDao.searchEtapeProgrammes(etapeCriteria, pageable);

        return etapeProgrammeRestrictedMapper.entitiesToDto(etapes, pageable);

    }

    @Override
    public Etape addEtapeProgramme(Etape etape) {

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeMapper.dtoToEntity(etape);

        etapeProgrammeEntity = etapeProgrammeDao.save(etapeProgrammeEntity);

        return etapeProgrammeMapper.entityToDto(etapeProgrammeEntity);

    }

    @Override
    public List<Etape> getEtapesForProgrammeById(long programmeId) {
        if (!programmeRightsHelper.checkCanGetEtapesForProgramme(programmeId)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer la liste des étapes pour le programme id = " + programmeId);
        }
        return etapeProgrammeWorkflowHelper.getAccessibleEtapesForProgramme(programmeId);
    }

    @Override
    public List<Etape> getEtapesForProgrammes() {

        List<EtapeProgrammeEntity> etapesProgrammeEntity = etapeProgrammeDao.findAll();

        return etapeProgrammeMapper.entitiesToDto(etapesProgrammeEntity);
    }

}

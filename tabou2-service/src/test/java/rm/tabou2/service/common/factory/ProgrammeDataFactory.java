package rm.tabou2.service.common.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.storage.sig.dao.ProgrammeRmDao;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;

/**
 * Factory de création d'objets de test liés aux programmes.
 * Mutualisée entre les différents tests.
 */
@Component
@RequiredArgsConstructor
public class ProgrammeDataFactory {

    private final OperationDao operationDao;
    private final ProgrammeRmDao programmeRmDao;
    private final EtapeProgrammeDao etapeProgrammeDao;
    private final EtapeProgrammeMapper etapeProgrammeMapper;

    /**
     * Crée une opération de test en base.
     */
    public OperationEntity createOperationEntity(String nom, boolean diffusionRestreinte) {
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom(nom);
        operationEntity.setDiffusionRestreinte(diffusionRestreinte);
        return operationDao.save(operationEntity);
    }

    /**
     * Crée ou récupère une emprise programme RM en base.
     */
    public ProgrammeRmEntity getOrCreateProgrammeRm(int id) {
        return programmeRmDao.findById(id)
                .orElseGet(() -> {
                    ProgrammeRmEntity rm = new ProgrammeRmEntity();
                    rm.setId(id);
                    return programmeRmDao.save(rm);
                });
    }

    /**
     * Construit un programme de base avec opération, étape et emprise pré-configurés.
     * L'opération et l'emprise sont créées en base automatiquement.
     */
    public Programme buildBaseProgramme(String nom, String code) {
        OperationEntity operationEntity = createOperationEntity("test", false);
        ProgrammeRmEntity programmeRm = getOrCreateProgrammeRm(1);
        EtapeProgrammeEntity etapePublic = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        Programme programme = new Programme();
        programme.setNom(nom);
        programme.setCode(code);
        programme.setNumAds("numads_" + code);
        programme.setOperationId(operationEntity.getId());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapePublic));
        programme.setIdEmprise(programmeRm.getId());
        return programme;
    }
}


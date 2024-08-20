package rm.tabou2.service.helper.programme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import java.util.List;

@Component
public class EtapeProgrammeWorkflowHelper {

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    /**
     * Liste des étapes possibles pour un programme
     *
     * @param idProgramme identifiant du programme
     * @return liste des étapes
     */
    public List<Etape> getAccessibleEtapesForProgramme(Long idProgramme) {
        ProgrammeEntity programmeEntity = programmeDao.findOneById(idProgramme);
        List<EtapeProgrammeEntity> nextEtapes = List.copyOf(programmeEntity.getEtapeProgramme().getNextEtapes());
        return etapeProgrammeMapper.entitiesToDto(nextEtapes);
    }

    /**
     * Permet de savoir si on peut affecter une étape à un programme.
     *
     * @param newEtape    etape à affecter
     * @param idProgramme identifiant du programme
     * @return true si on peut affectuer newEtape au programme
     */
    public boolean checkCanAssignEtapeToProgramme(Etape newEtape, Long idProgramme) {
        ProgrammeEntity programmeEntity = programmeDao.findOneById(idProgramme);
        EtapeProgrammeEntity actualEtapeEntity = programmeEntity.getEtapeProgramme();
        if (actualEtapeEntity.getCode().equals(newEtape.getCode())) {
            return true;
        }
        List<EtapeProgrammeEntity> nextEtapesEntities = List.copyOf(actualEtapeEntity.getNextEtapes());
        List<Etape> nextEtapes = etapeProgrammeMapper.entitiesToDto(nextEtapesEntities);
        List<Long> nextEtapesIdList = nextEtapes.stream().map(Etape::getId).toList();
        List<String> nextEtapesCodesList = nextEtapes.stream().map(Etape::getCode).toList();

        return nextEtapesIdList.contains(newEtape.getId()) || nextEtapesCodesList.contains(newEtape.getCode());
    }
}

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
     * @param idProgramme
     * @return liste des étapes
     */
    public List<Etape> getPossibleEtapesForProgramme(Long idProgramme) {
        ProgrammeEntity programmeEntity = programmeDao.getById(idProgramme);
        if (programmeEntity == null) {
            throw new IllegalArgumentException("L'identifiant du programme est invalide: aucun programme trouvé pour l'id = " + idProgramme);
        }
        List<EtapeProgrammeEntity> nextEtapes = List.copyOf(programmeEntity.getEtapeProgramme().getNextEtapes());
        return etapeProgrammeMapper.entitiesToDto(nextEtapes);
    }

    /**
     * Permet de savoir si on peut affecter une étape à un programme
     * @param newEtape      etape à affecter
     * @param idProgramme   le programme
     * @return              true si on peut affectuer newEtape au programme
     */
    public boolean checkCanAssignEtapeToProgramme(Etape newEtape, Long idProgramme) {
        ProgrammeEntity programmeEntity = programmeDao.getById(idProgramme);
        if (programmeEntity == null) {
            throw new IllegalArgumentException("L'identifiant du programme est invalide: aucun programme trouvé pour l'id = " + idProgramme);
        }
        EtapeProgrammeEntity actualEtapeEntity = programmeEntity.getEtapeProgramme();
        if (actualEtapeEntity.getCode().equals(newEtape.getCode())) {
            return true;
        }
        List<EtapeProgrammeEntity> nextEtapesEntities = List.copyOf(actualEtapeEntity.getNextEtapes());
        List<Etape> nextEtapes = etapeProgrammeMapper.entitiesToDto(nextEtapesEntities);
        return nextEtapes.contains(newEtape);
    }
}

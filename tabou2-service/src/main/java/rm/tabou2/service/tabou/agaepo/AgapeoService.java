package rm.tabou2.service.tabou.agaepo;

import rm.tabou2.service.dto.Agapeo;

import java.util.List;

public interface AgapeoService {

    /**
     * liste des données agapeo d'un programme
     * @param programmeId id du programme
     * @return List de Agapeo
     */
    List<Agapeo> getApapeosByProgrammeId(long programmeId);

}
package rm.tabou2.service;

import rm.tabou2.service.dto.Agapeo;

import java.util.List;

public interface AgapeoService {

    /**
     * liste des données agapeo d'un programme
     * @param programmeId id du programme
     * @return List de Agapeo
     */
    List<Agapeo> getApapeoByProgrammeId(long programmeId);

}

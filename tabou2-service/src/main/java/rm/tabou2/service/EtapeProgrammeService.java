package rm.tabou2.service;

import rm.tabou2.service.dto.Etape;

import java.util.List;

public interface EtapeProgrammeService {
    List<Etape> searchEtapesProgramme(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;


    Etape addEtapeProgramme(Etape etape);

    /**
     * Liste des étapes possibles pour un programme
     * @param programmeId   id du programme
     * @return              liste des étapes
     */
    List<Etape> getEtapesForProgrammeById(long programmeId);

    List<Etape> getEtapesForProgrammes();
}

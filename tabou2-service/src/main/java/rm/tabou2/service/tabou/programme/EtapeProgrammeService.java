package rm.tabou2.service.tabou.programme;

import rm.tabou2.service.dto.Etape;

import java.util.List;

public interface EtapeProgrammeService {

    List<Etape> searchEtapesProgramme(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc);

    Etape addEtapeProgramme(Etape etape);

    Etape getEtapeProgrammeById(long etapeProgrammeId);

    List<Etape> getEtapesForProgrammes();
}

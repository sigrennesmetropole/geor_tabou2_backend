package rm.tabou2.service;


import rm.tabou2.service.dto.Programme;

import java.util.List;

public interface ProgrammeService {


    Programme addProgramme(Programme programme);

    Programme getProgrammeById(long programmeId);

    List<Programme> searchProgrammes(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;
}

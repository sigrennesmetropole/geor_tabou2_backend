package rm.tabou2.service;

import rm.tabou2.service.dto.Evenement;

import java.util.List;

public interface EvenementProgrammeService {

    List<Evenement> getByProgrammeId(Long programmeId) throws Exception;

    Evenement addByProgrammeId(Evenement evenement, Long programmeId) throws Exception;

    Evenement editByProgrammeId(Evenement evenement, Long programmeId) throws Exception;
}

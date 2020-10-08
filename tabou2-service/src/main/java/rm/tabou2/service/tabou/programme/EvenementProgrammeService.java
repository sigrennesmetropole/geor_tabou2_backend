package rm.tabou2.service.tabou.programme;

import rm.tabou2.service.dto.Evenement;

import java.util.List;

public interface EvenementProgrammeService {

    List<Evenement> getByProgrammeId(Long programmeId);

    Evenement addByProgrammeId(Evenement evenement, Long programmeId);

    Evenement editByProgrammeId(Evenement evenement, Long programmeId);
}

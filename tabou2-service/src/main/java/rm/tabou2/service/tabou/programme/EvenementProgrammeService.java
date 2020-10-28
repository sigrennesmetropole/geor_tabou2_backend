package rm.tabou2.service.tabou.programme;

import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;

import java.util.List;

public interface EvenementProgrammeService {

    List<Evenement> getByProgrammeId(Long programmeId) throws AppServiceException;

    Evenement addByProgrammeId(Evenement evenement, Long programmeId) throws AppServiceException;

    Evenement updateByProgrammeId(Evenement evenement, Long programmeId) throws AppServiceException;
}
package rm.tabou2.service;

import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.exception.AppServiceException;

public interface ProgrammeTiersService {

    Programme associateTiersToProgramme(long programmeId, long tiersId, long typeTiersId) throws AppServiceException;

}

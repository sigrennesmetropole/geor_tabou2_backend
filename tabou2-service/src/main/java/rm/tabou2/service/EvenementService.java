package rm.tabou2.service;

import rm.tabou2.service.dto.Evenement;

import java.util.List;

public interface EvenementService {

    List<Evenement> getByOperationId(Long operationId) throws Exception;

}

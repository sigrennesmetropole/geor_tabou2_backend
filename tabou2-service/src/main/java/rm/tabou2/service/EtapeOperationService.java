package rm.tabou2.service;

import rm.tabou2.service.dto.Etape;

import java.util.List;

public interface EtapeOperationService {

    List<Etape> searchEtapesOperation(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;

    Etape addEtapeOperation(Etape etape);

    Etape getEtapeOperationById(long etapeOperationId);

    List<Etape> getEtapesForOperation();
}

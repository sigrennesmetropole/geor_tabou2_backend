package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.Etape;

import java.util.List;

public interface EtapeOperationService {

    List<Etape> searchEtapesOperation(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc);

    Etape addEtapeOperation(Etape etape);

    Etape getEtapeOperationById(long etapeOperationId);

    List<Etape> getEtapesForOperation();
}

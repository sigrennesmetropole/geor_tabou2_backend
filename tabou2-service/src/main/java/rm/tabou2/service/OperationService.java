package rm.tabou2.service;

import org.threeten.bp.LocalDate;
import rm.tabou2.service.dto.Operation;

import java.util.Date;
import java.util.List;

public interface OperationService {
    Operation addOperation(Operation operation);

    List<Operation> getAllOperations(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc);


    List<Operation> searchOperation(String nom, String nature, String etape, Boolean diffusionRestreinte, String code, String numAds, LocalDate autorisationDateDebut, LocalDate autorisationDateFin,
                                    LocalDate operationnelDateDebut, LocalDate operationnelDateFin, LocalDate clotureDateDebut, LocalDate clotureDateFin,
                                    Integer start, Integer resultsNumber, String orderBy, Boolean asc);

    Operation getOperationById(long operationId);
}

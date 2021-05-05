package rm.tabou2.service.tabou.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Evenement;

public interface EvenementOperationService {

    Page<Evenement> searchEvenementOperation(long operationId, Pageable pageable);

}

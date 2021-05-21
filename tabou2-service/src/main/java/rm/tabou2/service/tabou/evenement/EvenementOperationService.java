package rm.tabou2.service.tabou.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;

public interface EvenementOperationService {

    Page<Evenement> searchEvenementOperation(long operationId, Pageable pageable);

    /**
     * Suppression d'un évènement d'une opération.
     *
     * @param evenementId identifiant de l'évènement
     * @param operationId identifiant de l'opération
     */
    void deleteEvenementByOperationId(long evenementId, long operationId) throws AppServiceException;
}

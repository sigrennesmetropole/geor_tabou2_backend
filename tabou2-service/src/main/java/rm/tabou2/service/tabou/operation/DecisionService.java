package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Decision;

public interface DecisionService {

    /**
     * Retourne la liste des décisions.
     *
     * @param pageable paramètres de pagination
     * @return liste des décisions
     */
    Page<Decision> searchDecisions(Pageable pageable);
}

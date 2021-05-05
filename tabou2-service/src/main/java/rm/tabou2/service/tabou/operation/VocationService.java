package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Vocation;

public interface VocationService {

    /**
     * Retourne la liste des vocations.
     *
     * @param pageable paramètres de pagination
     * @return liste des vocations
     */
    Page<Vocation> searchVocations(Pageable pageable);
}

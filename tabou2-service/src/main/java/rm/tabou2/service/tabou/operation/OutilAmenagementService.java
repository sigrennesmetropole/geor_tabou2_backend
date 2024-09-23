package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.OutilAmenagement;

public interface OutilAmenagementService {

    /**
     * Retourne la liste des outils d'aménagement.
     *
     * @param pageable paramètres de pagination
     * @return liste des outils d'aménagement
     */
    Page<OutilAmenagement> searchOutilsAmenagement(Pageable pageable);

}

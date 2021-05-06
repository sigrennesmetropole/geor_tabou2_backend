package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.ModeAmenagement;

public interface ModeAmenagementService {

    /**
     * Retourne la liste des modes d'aménagement.
     *
     * @param pageable paramètres de pagination
     * @return liste des modes d'aménagement
     */
    Page<ModeAmenagement> searchModesAmenagement(Pageable pageable);

}

package rm.tabou2.service.tabou.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Evenement;

public interface EvenementProgrammeService {

    /**
     * Rechche des évènements des programmes
     *
     * @param programmeId identifiant du programme
     * @param pageable    paramètres de pagination
     * @return liste des évènements des programmes.
     */
    Page<Evenement> searchEvenementsProgramme(long programmeId, Pageable pageable);

}

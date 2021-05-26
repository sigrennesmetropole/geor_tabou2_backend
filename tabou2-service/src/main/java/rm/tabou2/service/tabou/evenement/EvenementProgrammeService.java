package rm.tabou2.service.tabou.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;

public interface EvenementProgrammeService {

    /**
     * Rechche des évènements des programmes
     *
     * @param programmeId identifiant du programme
     * @param pageable    paramètres de pagination
     * @return liste des évènements des programmes.
     */
    Page<Evenement> searchEvenementsProgramme(long programmeId, Pageable pageable);

    /**
     * Suppression d'un évènement d'un programme.
     *
     * @param evenementId identifiant de l'évènement
     * @param programmeId identifiant du programme
     */
    void deleteEvenementByProgrammeId(long evenementId, long programmeId) throws AppServiceException;
}

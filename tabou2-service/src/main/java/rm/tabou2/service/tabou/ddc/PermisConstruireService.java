package rm.tabou2.service.tabou.ddc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.PermisConstruire;

public interface PermisConstruireService {

    /**
     * Liste de permis de construire d'un programme.
     *
     * @param programmeId identifiant du programme
     * @param pageable    paramètres de pagination
     * @return liste des permis de construire
     */

    Page<PermisConstruire> getPermisConstruiresByProgrammeId(long programmeId, Pageable pageable);

}

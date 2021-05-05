package rm.tabou2.service.tabou.agaepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Agapeo;

public interface AgapeoService {

    /**
     * Liste des données agapeo d'un programme.
     *
     * @param programmeId id du programme
     * @param pageable    paramètres de pagination
     * @return List de Agapeo
     */
    Page<Agapeo> getApapeosByProgrammeId(long programmeId, Pageable pageable);
}

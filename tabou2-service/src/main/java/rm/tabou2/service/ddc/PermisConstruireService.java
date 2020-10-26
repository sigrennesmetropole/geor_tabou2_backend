package rm.tabou2.service.ddc;

import rm.tabou2.service.dto.PermisConstruire;

import java.util.List;

public interface PermisConstruireService {

    /**
     * Liste de permis de construire d'un programme
     * @param programmeId   identifiant du programme
     * @return              liste des permis de construire
     */
    List<PermisConstruire> getPermisConstruiresByProgrammeId(long programmeId);
}

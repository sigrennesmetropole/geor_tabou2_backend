package rm.tabou2.service.tabou.programme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

import java.util.List;

public interface EtapeProgrammeService {

    /**
     * Recherche des d'etapes de programmes
     * @param etapeCriteria
     * @param pageable
     * @return
     */
    Page<EtapeRestricted> searchEtapesProgramme(EtapeCriteria etapeCriteria, Pageable pageable);

    Etape addEtapeProgramme(Etape etape);

    /**
     * Liste des étapes possibles pour un programme
     * @param programmeId   id du programme
     * @return              liste des étapes
     */
    List<Etape> getEtapesForProgrammeById(long programmeId);

    List<Etape> getEtapesForProgrammes();
}

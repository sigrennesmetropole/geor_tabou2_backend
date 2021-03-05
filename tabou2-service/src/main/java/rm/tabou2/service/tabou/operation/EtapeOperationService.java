package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

import java.util.List;

public interface EtapeOperationService {

    /**
     * Récuperation de la liste des étapes des opérations avec filtrage
     * @param etapeCriteria
     * @param pageable
     * @return
     */
    Page<EtapeRestricted> searchEtapesOperation(EtapeCriteria etapeCriteria, Pageable pageable);

    Etape addEtapeOperation(Etape etape);

    Etape getEtapeOperationById(long etapeOperationId);

    /**
     * Récupération de la liste des étapes possibles pour un programme
     * @param operationId identifiant de l'opération
     * @return liste des étapes
     */
    List<Etape> getEtapesForOperationById(long operationId);

    List<Etape> getEtapesForOperation();
}

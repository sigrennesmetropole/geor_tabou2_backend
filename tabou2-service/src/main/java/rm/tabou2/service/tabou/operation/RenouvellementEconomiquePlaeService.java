package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.RenouvellementEconomiquePlae;

public interface RenouvellementEconomiquePlaeService {

    /**
     * Retourne la liste des renouvellements économiques PLAE.
     *
     * @param actifUniquement si true, ne retourne que les renouvellements économiques PLAE actifs
     * @param pageable paramètres de pagination
     * @return liste des renouvellements économiques PLAE
     */
    Page<RenouvellementEconomiquePlae> searchRenouvellementsEconomiquesPlae(Boolean actifUniquement, Pageable pageable);

}

package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.VocationPlae;

public interface VocationPlaeService {

    /**
     * Retourne la liste des vocations PLAE.
     *
     * @param actifUniquement si true, ne retourne que les vocations PLAE actives
     * @param pageable paramètres de pagination
     * @return liste des vocations PLAE
     */
    Page<VocationPlae> searchVocationsPlae(Boolean actifUniquement, Pageable pageable);

}

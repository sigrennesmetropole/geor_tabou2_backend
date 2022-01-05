package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.VocationZA;
import rm.tabou2.storage.tabou.item.VocationZACriteria;


public interface VocationZAService {

    /**
     * Recherche les vocations ZA
     *
     * @param criteria Critères de recherche
     * @param pageable Indicateur de pagination
     * @return Les vocations ZA correspondant à la recherche
     */
    Page<VocationZA> searchVocationsZA(VocationZACriteria criteria, Pageable pageable);
}

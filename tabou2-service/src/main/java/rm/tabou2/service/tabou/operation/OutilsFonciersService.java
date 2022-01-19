package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.OutilFoncier;
import rm.tabou2.storage.tabou.item.OutilFoncierCriteria;


public interface OutilsFonciersService {

    /**
     * Recherche des outils fonciers
     *
     * @param criteria critères de recherche
     * @param pageable pagination
     * @return Page des outils fonciers correspondants à la recherche, respectant la pagination
     */
    Page<OutilFoncier> searchOutilsFonciers(OutilFoncierCriteria criteria, Pageable pageable);
}

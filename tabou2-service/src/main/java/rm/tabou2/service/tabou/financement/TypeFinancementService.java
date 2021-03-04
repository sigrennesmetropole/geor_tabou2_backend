package rm.tabou2.service.tabou.financement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeFinancement;
import rm.tabou2.storage.tabou.item.TypeFinancementCriteria;

public interface TypeFinancementService {

    /**
     * Recherche de type de financements
     * @param typeFinancementCriteria
     * @param pageable
     * @return
     */
    Page<TypeFinancement> searchTypeFinancement(TypeFinancementCriteria typeFinancementCriteria, Pageable pageable);

}

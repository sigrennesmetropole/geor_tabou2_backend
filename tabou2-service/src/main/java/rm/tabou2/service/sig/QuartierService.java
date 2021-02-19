package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.storage.sig.item.QuartierCriteria;

public interface QuartierService {

    Page<Quartier> searchQuartiers(QuartierCriteria quartierCriteria, Pageable pageable);

    Quartier getQuartierById(int quartierId);
}

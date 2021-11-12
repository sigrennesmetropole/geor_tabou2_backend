package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.EntiteRM;
import rm.tabou2.storage.tabou.item.EntiteRMCriteria;

public interface EntiteRMService {
    Page<EntiteRM> searchEntitesRM(EntiteRMCriteria criteria, Pageable pageable);
}

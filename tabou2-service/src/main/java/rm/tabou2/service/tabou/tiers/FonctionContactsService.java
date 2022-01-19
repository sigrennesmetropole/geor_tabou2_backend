package rm.tabou2.service.tabou.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.FonctionContacts;
import rm.tabou2.storage.tabou.item.FonctionContactsCriteria;

public interface FonctionContactsService {

    Page<FonctionContacts> searchFonctionContacts(FonctionContactsCriteria criteria, Pageable pageable);
}

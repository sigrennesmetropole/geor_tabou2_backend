package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.EntiteReferente;
import rm.tabou2.storage.tabou.item.EntiteReferenteCriteria;

public interface EntiteReferenteService {
    Page<EntiteReferente> searchEntitesReferentes(EntiteReferenteCriteria criteria, Pageable pageable);
}

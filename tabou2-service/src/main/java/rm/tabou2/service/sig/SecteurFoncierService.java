package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurFoncier;

public interface SecteurFoncierService {

    Page<SecteurFoncier> searchSecteursFonciers(String negociateur, Pageable pageable);


}

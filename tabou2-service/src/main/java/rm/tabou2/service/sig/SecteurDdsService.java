package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurDds;

public interface SecteurDdsService {

    Page<SecteurDds> searchSecteursDds(String secteur, Pageable pageable);

}

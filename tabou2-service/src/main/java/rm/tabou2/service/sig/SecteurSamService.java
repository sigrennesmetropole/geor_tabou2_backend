package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurSam;

public interface SecteurSamService {
    Page<SecteurSam> searchSecteursSam(String secteur, Pageable pageable);
}

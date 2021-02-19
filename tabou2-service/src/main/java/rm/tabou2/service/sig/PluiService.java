package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.PluiZonage;

public interface PluiService {


    Page<PluiZonage> searchPlui(String libelle, Pageable pageable);
}

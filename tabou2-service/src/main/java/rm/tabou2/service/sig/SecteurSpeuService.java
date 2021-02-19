package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurSpeu;

public interface SecteurSpeuService {

    Page<SecteurSpeu> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable);
}

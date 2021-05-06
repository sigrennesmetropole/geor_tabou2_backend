package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.MaitriseOuvrage;

public interface MaitriseOuvrageService {

    /**
     * Retourne la liste des maitrises d'ouvrage.
     *
     * @param pageable Paramètres de pagination
     * @return liste des maitrises d'ouvrage
     */
    Page<MaitriseOuvrage> searchMaitrisesOuvrage(Pageable pageable);

}

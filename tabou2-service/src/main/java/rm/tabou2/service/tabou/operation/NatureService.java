package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Nature;

public interface NatureService {

    /**
     * Retourne la liste des natures.
     *
     * @param onlyActive true si doit seulement retourner la liste des natures actives
     * @param pageable   paramètres de pagination
     * @return liste des natures
     */
    Page<Nature> searchNatures(Boolean onlyActive, Pageable pageable);

}

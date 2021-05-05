package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.ConsommationEspace;

public interface ConsommationEspaceService {


    /**
     * Retourne la liste des consommations d'espace.
     *
     * @param pageable paramètres de pagination
     * @return liste des consommations d'espace
     */
    Page<ConsommationEspace> searchConsommationsEspace(Pageable pageable);

}

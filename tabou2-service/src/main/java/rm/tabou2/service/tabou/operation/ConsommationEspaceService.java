package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.ConsommationEspace;

import java.util.List;

public interface ConsommationEspaceService {

    /**
     * Retourne la liste des consommations d'espace
     *
     * @return liste des consommations d'espace
     */
    List<ConsommationEspace> getAllConsommationsEspace();
}

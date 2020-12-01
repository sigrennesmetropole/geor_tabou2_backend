package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.Nature;

import java.util.List;

public interface NatureService {

    /**
     * Retourne la liste des natures
     *
     * @param onlyActive true si doit seulement retourner la liste des natures actives
     * @return liste des nature
     */
    List<Nature> getAllNatures(Boolean onlyActive);

}

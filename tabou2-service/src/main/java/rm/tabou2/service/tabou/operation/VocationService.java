package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.Vocation;

import java.util.List;

public interface VocationService {

    /**
     * Retourne la liste des vocations
     *
     * @return liste des vocations
     */
    List<Vocation> getAllVocations();
}

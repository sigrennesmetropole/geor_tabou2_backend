package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.Decision;

import java.util.List;

public interface DecisionService {

    /**
     * Retourne la liste des décisions
     *
     * @return liste des décisions
     */
    List<Decision> getAllDecisions();
}

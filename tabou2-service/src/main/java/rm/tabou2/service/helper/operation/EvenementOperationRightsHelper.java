package rm.tabou2.service.helper.operation;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.mapper.tabou.operation.OperationIntermediaire;

@Component
public class EvenementOperationRightsHelper {

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    /**
     * Permet de savoir si l'utilisateur a le droit de modifier un événement d'une opération
     *
     * @param operation       operation
     * @param actualEvenement événement actuel
     * @return true si l'utilisateur peut modifier
     */
    public boolean checkCanUpdateEvenementOperation(OperationIntermediaire operation, Evenement actualEvenement) {
        return operationRightsHelper.checkCanUpdateOperation(operation, operation)
                && BooleanUtils.isFalse(actualEvenement.isSysteme());
    }
}

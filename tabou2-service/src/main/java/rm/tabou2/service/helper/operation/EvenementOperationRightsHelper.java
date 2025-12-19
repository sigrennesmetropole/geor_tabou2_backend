package rm.tabou2.service.helper.operation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;

@Component
@RequiredArgsConstructor
public class EvenementOperationRightsHelper {

    private final OperationRightsHelper operationRightsHelper;

    /**
     * Permet de savoir si l'utilisateur a le droit de modifier un événement d'une opération
     *
     * @param operation       operation
     * @param actualEvenement événement actuel
     * @return true si l'utilisateur peut modifier
     */
    public boolean checkCanUpdateEvenementOperation(OperationIntermediaire operation, Evenement actualEvenement) {
        return operationRightsHelper.checkCanUpdateOperation(operation, operation)
                && BooleanUtils.isFalse(actualEvenement.getSysteme());
    }
}

package rm.tabou2.service.helper.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Component
public class OperationRightsHelper {

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationDao operationDao;

    /**
     * Vérifie si l'utilisateur a les droits d'ajouter une operation
     * @param operation operation
     * @return true si l'utilisateur peut ajouter l'operation
     */
    public boolean checkCanCreateOperation(Operation operation) {
        // L'utilisateur peut créer une operation en étant au moins contributeur en diffusion non restreinte
        boolean checkDiffusionNonRestreinte = !operation.isDiffusionRestreinte() && authentificationHelper.hasEditAccess();
        // L'utilisateur peut créer une operation en étant au moins référent en diffusion restreinte
        boolean checkDiffusionRestreinte = operation.isDiffusionRestreinte() && authentificationHelper.hasRestreintAccess();

        return checkDiffusionNonRestreinte || checkDiffusionRestreinte;
    }

    /**
     * Vérifie si l'utilisateur a les droits de modifier une operation
     * @param operation l'operation
     * @param diffusionRestreinteChanged true si le paramètre diffusionRestreinte est modifié
     * @return true si l'utilisateur peut modifier l'operation
     */
    public boolean checkCanUpdateOperation(Operation operation, boolean diffusionRestreinteChanged) {
        return diffusionRestreinteChanged
                ? authentificationHelper.hasRestreintAccess()
                : checkCanCreateOperation(operation);
    }

    /**
     * Vérifie si l'utilisateur peut récupérer la liste des étapes possibles pour une opération
     * @param operationId identifiant de l'opération
     * @return true si l'utilisateur le peut
     */
    public boolean checkCanGetEtapesForOperation(long operationId) {
        OperationEntity operationEntity = operationDao.getById(operationId);
        if (operationEntity == null) {
            throw new IllegalArgumentException("L'identifiant de l'operation est invalide: aucune opération trouvée pour l'id = " + operationId);
        }
        return !operationEntity.isDiffusionRestreinte() || authentificationHelper.hasRestreintAccess();
    }
}

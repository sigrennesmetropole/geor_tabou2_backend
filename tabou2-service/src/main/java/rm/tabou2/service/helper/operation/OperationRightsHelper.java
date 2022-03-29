package rm.tabou2.service.helper.operation;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Component
public class OperationRightsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationDao operationDao;

    /**
     * Vérifie si l'utilisateur a les droits d'ajouter une operation
     * @param operation operation
     * @return true si l'utilisateur peut ajouter l'operation
     */
    public boolean checkCanCreateOperation(OperationIntermediaire operation) {
        // L'utilisateur peut créer une operation en étant au moins contributeur en diffusion non restreinte
        boolean checkDiffusionNonRestreinte = !operation.getDiffusionRestreinte() && authentificationHelper.hasEditAccess();
        // L'utilisateur peut créer une operation en étant au moins référent en diffusion restreinte
        boolean checkDiffusionRestreinte = operation.getDiffusionRestreinte() && authentificationHelper.hasRestreintAccess();

        return checkDiffusionNonRestreinte || checkDiffusionRestreinte;
    }

    /**
     * Vérifie si l'utilisateur a les droits de modifier une operation
     * @param operation l'operation
     * @param actualOperation actuelle diffusion restreinte de l'opération avant modification
     * @return true si l'utilisateur peut modifier l'operation
     */
    public boolean checkCanUpdateOperation(OperationIntermediaire operation, OperationIntermediaire actualOperation) {

        if (BooleanUtils.isFalse(actualOperation.getDiffusionRestreinte()) && !authentificationHelper.hasEditAccess()) {
            LOGGER.warn("L'opération ne peut être modifié par un utilisateur qui n'a pas les droits de modification");
            return false;
        }

        if (BooleanUtils.isTrue(actualOperation.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn("L'opération avec diffusion restreinte ne peut être modifiée par un utilisateur qui n'a pas les droits");
            return false;
        }

        return true;
    }

    /**
     * Vérifie si l'utilsiateur peut récupérer une opération
     * @param operation operation
     * @return true si l'utilisateur peut récupérer l'opération
     */
    public boolean checkCanGetOperation(OperationIntermediaire operation) {
        return !operation.getDiffusionRestreinte() || authentificationHelper.hasRestreintAccess();
    }

    /**
     * Vérifie si l'utilsiateur peut récupérer une opération
     * @param operationEntity operation
     * @return true si l'utilisateur peut récupérer l'opération
     */
    public boolean checkCanGetOperation(OperationEntity operationEntity) {
        return !operationEntity.isDiffusionRestreinte() || authentificationHelper.hasRestreintAccess();
    }

    /**
     * Vérifie si l'utilisateur peut récupérer la liste des étapes possibles pour une opération
     * @param operationId identifiant de l'opération
     * @return true si l'utilisateur le peut
     */
    public boolean checkCanGetEtapesForOperation(long operationId) {
        OperationEntity operationEntity = operationDao.findOneById(operationId);
        return !operationEntity.isDiffusionRestreinte() || authentificationHelper.hasRestreintAccess();
    }
}

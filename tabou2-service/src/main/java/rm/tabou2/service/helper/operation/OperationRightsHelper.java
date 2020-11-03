package rm.tabou2.service.helper.operation;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Component
public class OperationRightsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private EtapeOperationWorkflowHelper etapeOperationWorkflowHelper;

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
     * @param actualOperation actuelle diffusion restreinte de l'opération avant modification
     * @return true si l'utilisateur peut modifier l'operation
     */
    public boolean checkCanUpdateOperation(Operation operation, Operation actualOperation) {

        if (BooleanUtils.isTrue(actualOperation.isDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn("L'opération avec diffusion restreinte ne peut être modifiée par un utilisateur qui n'a pas les droits");
            return false;
        }

        // validation de l'étape
        boolean etapeValidation = etapeOperationWorkflowHelper.checkCanAssignEtapeToOperation(operation.getEtape(), operation.getId());
        if (!etapeValidation) {
            LOGGER.warn("L'étape ne peut être assignée à l'opération");
            return false;
        }

        if (operation.getNature() != null && operation.getNature() != actualOperation.getNature()) {
            LOGGER.warn("La nature d'une opération ne doit pas être modifiée");
            return false;
        }

        if (operation.isSecteur() != null && !operation.isSecteur().equals(actualOperation.isSecteur())) {
            LOGGER.warn("le paramètre secteur d'une opération ne doit pas être modifiée");
            return false;
        }

        return true;
    }

    /**
     * Vérifie si l'utilsiateur peut récupérer une opération
     * @param operation operation
     * @return true si l'utilisateur peut récupérer l'opération
     */
    public boolean checkCanGetOperation(Operation operation) {
        return !operation.isDiffusionRestreinte() || authentificationHelper.hasRestreintAccess();
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

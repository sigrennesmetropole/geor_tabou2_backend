package rm.tabou2.service.helper.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.service.dto.Etape;

import java.util.Objects;

@Component
public class OperationValidator {

    @Autowired
    private EtapeOperationWorkflowHelper etapeOperationWorkflowHelper;

    @Autowired
    private EtapeOperationDao etapeOperationDao;

    public void validateOperation(OperationIntermediaire operation) throws AppServiceException {

         Etape etape = operation.getEtape();

        if(etape.getId() == null && etape.getCode() == null){
            throw new AppServiceException("L'étape passée en paramètre ne contient ni code, ni id", AppServiceExceptionsStatus.BADREQUEST);
        }
        // validation de l'étape
        boolean etapeValidation = etapeOperationWorkflowHelper.checkCanAssignEtapeToOperation(etape, operation.getId());
        if (!etapeValidation) {
            String identifiant = etape.getCode();
            if(identifiant == null){
                identifiant = etapeOperationDao.findOneById(etape.getId()).getCode();
            }
            throw new AppServiceException("L'étape " + identifiant + " ne peut pas être attribuée à l'opération " + operation.getCode(), AppServiceExceptionsStatus.BADREQUEST);
        }


    }
    public void validateUpdateOperation(OperationIntermediaire operation, OperationIntermediaire actualOperation) throws AppServiceException {
        validateOperation(operation);
        if (operation.getNature() != null && !Objects.equals(operation.getNature().getId(), actualOperation.getNature().getId())) {
            throw new AppServiceException("La nature d'une opération ne doit pas être modifiée", AppServiceExceptionsStatus.BADREQUEST);
        }

        if (operation.getSecteur() != null && !operation.getSecteur().equals(actualOperation.getSecteur())) {
            throw new AppServiceException("le paramètre secteur d'une opération ne doit pas être modifiée", AppServiceExceptionsStatus.BADREQUEST);
        }
    }
}

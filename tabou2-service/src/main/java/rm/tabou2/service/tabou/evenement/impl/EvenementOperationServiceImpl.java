package rm.tabou2.service.tabou.evenement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.helper.operation.EvenementOperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.EvenementOperationMapper;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.service.tabou.evenement.EvenementOperationService;
import rm.tabou2.storage.tabou.dao.evenement.EvenementOperationCustomDao;
import rm.tabou2.storage.tabou.dao.operation.EvenementOperationDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EvenementOperationServiceImpl implements EvenementOperationService {

    @Autowired
    private EvenementOperationCustomDao evenementOperationCustomDao;

    @Autowired
    private EvenementOperationMapper evenementOperationMapper;

    @Autowired
    private EvenementOperationDao evenementOperationDao;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private EvenementOperationRightsHelper evenementOperationRigthsHelper;

    @Override
    public Page<Evenement> searchEvenementsOperations(long operationId, Pageable pageable) {

        return evenementOperationMapper.entitiesToDto(evenementOperationCustomDao.searchEvenementsOperation(operationId, pageable), pageable);

    }

    @Override
    @Transactional(readOnly = false)
    public void deleteEvenementByOperationId(long evenementId, long operationId) throws AppServiceException {

        Optional<OperationEntity> operationEntityOpt = operationDao.findById(operationId);
        if (operationEntityOpt.isEmpty()) {
            throw new NoSuchElementException("L'opération id=" + operationId + " n'existe pas");
        }
        OperationEntity operationEntity = operationEntityOpt.get();
        Optional<EvenementOperationEntity> evenementOperationOpt = operationEntity.getEvenements().stream().filter(e-> e.getId() == evenementId).findFirst();
        if (evenementOperationOpt.isEmpty()) {
            throw new NoSuchElementException("L'evenement id=" + evenementId + " n'existe pas");
        }
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntityOpt.get());
        Evenement evenement = evenementOperationMapper.entityToDto(evenementOperationOpt.get());

        //Test les Permissions : droit en modification et si evenement system
        if (evenementOperationRigthsHelper.checkCanUpdateEvenementOperation(operation, evenement)) {
            //Suppression de l'évènement
            operationEntity.getEvenements().remove(evenementOperationOpt.get());
            operationDao.save(operationEntity);
        } else {
            throw new AppServiceException("Utilisateur non autorisé à supprimer l'évènement ", AppServiceExceptionsStatus.FORBIDDEN);
        }

    }

}

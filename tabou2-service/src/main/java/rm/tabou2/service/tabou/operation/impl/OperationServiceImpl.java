package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.operation.OperationCustomDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationCustomDao operationCustomDao;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Override
    public Operation createOperation(Operation operation) {
        // Vérification des droits utilisateur
        if (operationRightsHelper.checkCanCreateOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création de l'operation " + operation.getNom());
        }

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);

        operationEntity.setCreateDate(new Date());
        operationEntity.setModifDate(new Date());
        operationEntity.setCreateUser(authentificationHelper.getConnectedUsername());
        operationEntity.setModifUser(authentificationHelper.getConnectedUsername());

        operationDao.save(operationEntity);

        return operationMapper.entityToDto(operationEntity);

    }

    @Override
    @Transactional
    public Operation updateOperation(Operation operation) {

        // TODO updateOperation
        // Vérification des droits utilisateur
        if (operationRightsHelper.checkCanUpdateProgramme(operation, true)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création de l'operation " + operation.getNom());
        }

        return null;

    }

    @Override
    public Page<Operation> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable) {
        return operationMapper.entitiesToDto(operationCustomDao.searchOperations(operationsCriteria, pageable),pageable);
    }


    @Override
    public Operation getOperationById(long operationId) {

        Optional<OperationEntity> operationOpt = operationDao.findById(operationId);

        if (operationOpt.isEmpty()) {
            throw new NoSuchElementException("L'operation demandée n'existe pas, id=" + operationId);
        }

        return (operationMapper.entityToDto(operationOpt.get()));

    }



}
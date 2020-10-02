package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.mapper.OperationMapper;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.storage.tabou.dao.OperationCustomDao;
import rm.tabou2.storage.tabou.dao.OperationDao;
import rm.tabou2.storage.tabou.entity.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    private static final String DEFAULT_ORDER_BY = "createDate";

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationCustomDao operationCustomDao;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public Operation addOperation(Operation operation) {

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);

        operationEntity.setCreateDate(new Date());
        operationEntity.setModifDate(new Date());
        operationEntity.setCreateUser(authentificationHelper.getConnectedUsername());
        operationEntity.setModifUser(authentificationHelper.getConnectedUsername());

        operationDao.save(operationEntity);

        return operationMapper.entityToDto(operationEntity);

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

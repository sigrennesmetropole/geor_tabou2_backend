package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.mapper.OperationMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.OperationDao;
import rm.tabou2.storage.tabou.entity.OperationEntity;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    private static final String DEFAULT_ORDER_BY = "createDate";

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private OperationDao operationDao;

    @Override
    public Operation addOperation(Operation operation) {

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);

        operationEntity.setCreateDate(new Date());
        operationEntity.setModifDate(new Date());
        operationEntity.setCreateUser(Utils.getConnectedUsername());
        operationEntity.setModifUser(Utils.getConnectedUsername());

        operationDao.save(operationEntity);

        return operationMapper.entityToDto(operationEntity);

    }

    @Override
    public List<Operation> getAllOperations(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        //Initialisation des variables
        orderBy = (orderBy == null) ? DEFAULT_ORDER_BY : orderBy;
        keyword = (keyword == null) ? "%" : "%" + keyword + "%";

        List<OperationEntity> entites = operationDao.findByKeyword(keyword, Utils.buildPageable(start, resultsNumber, orderBy, asc));

        return operationMapper.entitiesToDto(entites);

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

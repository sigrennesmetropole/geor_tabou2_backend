package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.mapper.OperationMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.OperationDao;
import rm.tabou2.storage.tabou.entity.OperationEntity;

import javax.validation.Valid;
import java.util.List;
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

        if (null == operation) {
            //TODO
        }

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);

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

        if (null == operationOpt || operationOpt.isEmpty()) {
            //TODO : exception
        }

        return (operationMapper.entityToDto(operationOpt.get()));

    }



}

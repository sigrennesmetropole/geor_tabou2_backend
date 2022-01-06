package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeActionOperation;
import rm.tabou2.service.mapper.tabou.operation.TypeActionOperationMapper;
import rm.tabou2.service.tabou.operation.TypeActionOperationService;
import rm.tabou2.storage.tabou.dao.operation.impl.TypeActionOperationCustomDaoImpl;
import rm.tabou2.storage.tabou.item.TypeActionOperationCriteria;

@Service
public class TypeActionOperationServiceImpl implements TypeActionOperationService {

    @Autowired
    TypeActionOperationMapper mapper;

    @Autowired
    TypeActionOperationCustomDaoImpl customDao;

    @Override
    public Page<TypeActionOperation> searchTypesActionsOperations(TypeActionOperationCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(customDao.searchTypesActionsOperations(criteria, pageable), pageable);
    }
}

package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeProgrammation;
import rm.tabou2.service.mapper.tabou.operation.TypeProgrammationMapper;
import rm.tabou2.service.tabou.operation.TypeProgrammationService;
import rm.tabou2.storage.tabou.dao.operation.TypeProgrammationCustomDao;
import rm.tabou2.storage.tabou.item.TypeProgrammationCriteria;

@Service
public class TypeProgrammationServiceImpl implements TypeProgrammationService {

    @Autowired
    TypeProgrammationMapper mapper;

    @Autowired
    TypeProgrammationCustomDao customDao;

    @Override
    public Page<TypeProgrammation> searchTypesProgrammations(TypeProgrammationCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(customDao.searchTypesProgrammations(criteria, pageable), pageable);
    }
}

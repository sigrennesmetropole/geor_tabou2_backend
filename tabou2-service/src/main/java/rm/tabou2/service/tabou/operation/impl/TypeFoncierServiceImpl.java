package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeFoncier;
import rm.tabou2.service.mapper.tabou.operation.TypeFoncierMapper;
import rm.tabou2.service.tabou.operation.TypeFoncierService;
import rm.tabou2.storage.tabou.dao.operation.TypeFoncierCustomDao;
import rm.tabou2.storage.tabou.item.TypeFoncierCriteria;

@Service
public class TypeFoncierServiceImpl implements TypeFoncierService {

    @Autowired
    TypeFoncierMapper mapper;

    @Autowired
    TypeFoncierCustomDao customDao;

    @Override
    public Page<TypeFoncier> searchTypesFonciers(TypeFoncierCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(customDao.searchTypesFonciers(criteria, pageable), pageable);
    }
}

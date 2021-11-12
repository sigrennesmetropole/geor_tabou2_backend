package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.EntiteRM;
import rm.tabou2.service.mapper.tabou.operation.EntiteRMMapper;
import rm.tabou2.service.tabou.operation.EntiteRMService;
import rm.tabou2.storage.tabou.dao.operation.EntiteRMCustomDao;
import rm.tabou2.storage.tabou.item.EntiteRMCriteria;

@Service
public class EntiteRMServiceImpl implements EntiteRMService {

    @Autowired
    EntiteRMCustomDao entiteRMCustomDao;

    @Autowired
    EntiteRMMapper mapper;

    @Override
    public Page<EntiteRM> searchEntitesRM(EntiteRMCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(entiteRMCustomDao.searchEntitesRM(criteria, pageable), pageable);
    }
}

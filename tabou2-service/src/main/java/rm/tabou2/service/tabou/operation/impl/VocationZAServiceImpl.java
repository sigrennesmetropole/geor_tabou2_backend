package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.VocationZA;
import rm.tabou2.service.helper.operation.VocationZARightsHelper;
import rm.tabou2.service.mapper.tabou.operation.VocationZAMapper;
import rm.tabou2.service.tabou.operation.VocationZAService;
import rm.tabou2.storage.tabou.dao.operation.VocationZACustomDao;
import rm.tabou2.storage.tabou.item.VocationZACriteria;

@Service
public class VocationZAServiceImpl implements VocationZAService {

    @Autowired
    VocationZARightsHelper vocationZARightsHelper;

    @Autowired
    VocationZACustomDao vocationZACustomDao;

    @Autowired
    VocationZAMapper vocationZAMapper;

    @Override
    public Page<VocationZA> searchVocationsZA(VocationZACriteria criteria, Pageable pageable) {
        if (!vocationZARightsHelper.checkCanAccess()) {
            throw new AccessDeniedException("L'utilisateur n'a pas le droit de recherches les vocations ZA");
        }

        return vocationZAMapper.entitiesToDto(vocationZACustomDao.searchVocationsZA(criteria, pageable), pageable);
    }
}

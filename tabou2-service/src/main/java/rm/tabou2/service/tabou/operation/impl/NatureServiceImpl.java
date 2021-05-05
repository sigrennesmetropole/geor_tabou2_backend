package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.mapper.tabou.operation.NatureMapper;
import rm.tabou2.service.tabou.operation.NatureService;
import rm.tabou2.storage.tabou.dao.operation.NatureCustomDao;
import rm.tabou2.storage.tabou.dao.operation.NatureDao;

@Service
public class NatureServiceImpl implements NatureService {

    @Autowired
    private NatureDao natureDao;

    @Autowired
    private NatureCustomDao natureCustomDao;

    @Autowired
    private NatureMapper natureMapper;

    @Override
    public Page<Nature> searchNatures(Boolean onlyActive, Pageable pageable) {

        return natureMapper.entitiesToDto(natureCustomDao.searchNature(onlyActive, pageable), pageable);

    }

}

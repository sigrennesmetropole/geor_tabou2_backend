package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.mapper.tabou.operation.VocationMapper;
import rm.tabou2.service.tabou.operation.VocationService;
import rm.tabou2.storage.tabou.dao.operation.VocationCustomDao;
import rm.tabou2.storage.tabou.dao.operation.VocationDao;

@Service
public class VocationServiceImpl implements VocationService {

    @Autowired
    private VocationDao vocationDao;

    @Autowired
    private VocationCustomDao vocationCustomDao;

    @Autowired
    private VocationMapper vocationMapper;

    @Override
    public Page<Vocation> searchVocations(Pageable pageable) {
        return vocationMapper.entitiesToDto(vocationCustomDao.searchVocation(pageable), pageable);
    }
}

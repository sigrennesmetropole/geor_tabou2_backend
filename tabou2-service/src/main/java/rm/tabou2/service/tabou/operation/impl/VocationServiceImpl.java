package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.mapper.tabou.operation.VocationMapper;
import rm.tabou2.service.tabou.operation.VocationService;
import rm.tabou2.storage.tabou.dao.operation.VocationDao;

import java.util.List;

@Service
public class VocationServiceImpl implements VocationService {

    @Autowired
    private VocationDao vocationDao;

    @Autowired
    private VocationMapper vocationMapper;

    @Override
    public List<Vocation> getAllVocations() {
        return vocationMapper.entitiesToDto(vocationDao.findAll());
    }
}

package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.ModeAmenagement;
import rm.tabou2.service.mapper.tabou.operation.ModeAmenagementMapper;
import rm.tabou2.service.tabou.operation.ModeAmenagementService;
import rm.tabou2.storage.tabou.dao.operation.ModeAmenagementDao;

import java.util.List;

@Service
public class ModeAmenagementServiceImpl implements ModeAmenagementService {

    @Autowired
    private ModeAmenagementDao modeAmenagementDao;

    @Autowired
    private ModeAmenagementMapper modeAmenagementMapper;

    @Override
    public List<ModeAmenagement> getAllModesAmenagement() {
        return modeAmenagementMapper.entitiesToDto(modeAmenagementDao.findAll());
    }
}

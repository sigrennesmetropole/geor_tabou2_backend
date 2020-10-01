package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.AgapeoService;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.service.mapper.AgapeoMapper;
import rm.tabou2.storage.tabou.dao.AgapeoDao;

import java.util.List;

@Service
public class AgapeoServiceImpl implements AgapeoService {

    @Autowired
    private AgapeoMapper agapeoMapper;

    @Autowired
    private AgapeoDao agapeoDao;

    @Override
    public List<Agapeo> getApapeoByProgrammeId(long programmeId) {
        return agapeoMapper.entitiesToDto(agapeoDao.findAgapeoEntitiesByNumAds(String.valueOf(programmeId)));
    }
}

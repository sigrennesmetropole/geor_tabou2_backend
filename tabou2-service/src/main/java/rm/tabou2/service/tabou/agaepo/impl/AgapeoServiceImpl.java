package rm.tabou2.service.tabou.agaepo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.tabou.agaepo.AgapeoService;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.service.mapper.tabou.agapeo.AgapeoMapper;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;

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

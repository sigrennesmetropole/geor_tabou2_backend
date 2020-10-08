package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.tabou.operation.NatureService;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.mapper.tabou.operation.NatureMapper;
import rm.tabou2.storage.tabou.dao.NatureDao;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;

import java.util.List;

@Service
public class NatureServiceImpl implements NatureService {

    @Autowired
    private NatureDao natureDao;

    @Autowired
    private NatureMapper natureMapper;


    @Override
    public List<Nature> getAllNatures(Boolean onlyActive) {

        List<NatureEntity> natures = null;

        if (Boolean.TRUE.equals(onlyActive)) {
            natures = natureDao.findOnlyActive();
        } else {
            natures = natureDao.findAll();
        }

        return natureMapper.entitiesToDto(natures);

    }


}

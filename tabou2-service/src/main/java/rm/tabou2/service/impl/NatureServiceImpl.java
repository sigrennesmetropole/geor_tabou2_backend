package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.NatureService;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.mapper.NatureMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.NatureDao;
import rm.tabou2.storage.tabou.entity.NatureEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NatureServiceImpl implements NatureService {

    @Autowired
    private NatureDao natureDao;

    private NatureMapper natureMapper;

    @Override
    public Nature addNature(Nature nature) {

        if (null == nature) {
            //TODO : lever une exception
        }
        NatureEntity natureEntity = natureMapper.dtoToEntity(nature);

        natureEntity = natureDao.save(natureEntity);

        return natureMapper.entityToDto(natureEntity);
    }

    @Override
    public void inactivateNature(long natureId) {

        Optional<NatureEntity> natureEntityOpt = natureDao.findById(natureId);
        if (null == natureEntityOpt || natureEntityOpt.isEmpty()) {
            //TODO : exception
        }

        NatureEntity natureEntity = natureEntityOpt.get();
        natureEntity.setDateInactif(new Date());

        natureDao.save(natureEntity);

    }

    @Override
    public List<Nature> getAllNatures(Boolean onlyActive) throws Exception {

        List<NatureEntity> natures = null;

        if (onlyActive) {
            natures = natureDao.findOnlyActive();
        } else {
            natures = natureDao.findAll();
        }

        return natureMapper.entitiesToDto(natures);

    }


}

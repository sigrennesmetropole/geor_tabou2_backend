package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.QuartierService;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.service.mapper.QuartierMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.QuartierDao;
import rm.tabou2.storage.tabou.entity.administratif.QuartierEntity;

import java.util.List;

@Service
public class QuartierServiceImpl implements QuartierService {

    @Autowired
    private QuartierDao quartierDao;

    @Autowired
    private QuartierMapper quartierMapper;

    @Override
    public List<Quartier> searchQuartiers(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<QuartierEntity> quartiers = quartierDao.findByKeyword(keyword, Utils.buildPageable(start, resultsNumber, orderBy, asc));

        return quartierMapper.entitiesToDto(quartiers);

    }

}

package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.sig.QuartierService;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.service.mapper.sig.QuartierMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.dao.QuartierDao;
import rm.tabou2.storage.sig.entity.QuartierEntity;

import java.util.List;

@Service
public class QuartierServiceImpl implements QuartierService {

    @Autowired
    private QuartierDao quartierDao;

    @Autowired
    private QuartierMapper quartierMapper;

    @Override
    public List<Quartier> searchQuartiers(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        List<QuartierEntity> quartiers = quartierDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));

        return quartierMapper.entitiesToDto(quartiers);

    }

}

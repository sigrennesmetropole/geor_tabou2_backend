package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.PluiService;
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.service.mapper.PluiMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.PluiDao;
import rm.tabou2.storage.tabou.entity.administratif.PluiEntity;

import java.util.List;

@Service
public class PluiServiceImpl implements PluiService {

    @Autowired
    private PluiDao pluiDao;

    @Autowired
    private PluiMapper pluiMapper;

    @Override
    public List<PluiZonage> searchPlui(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<PluiEntity> pluis = pluiDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));

        return pluiMapper.entitiesToDto(pluis);

    }

}

package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.sig.PluiService;
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.service.mapper.sig.PluiMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.dao.PluiDao;
import rm.tabou2.storage.sig.entity.PluiEntity;

import java.util.List;

@Service
public class PluiServiceImpl implements PluiService {

    @Autowired
    private PluiDao pluiDao;

    @Autowired
    private PluiMapper pluiMapper;

    @Override
    public List<PluiZonage> searchPlui(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc){

        List<PluiEntity> pluis = pluiDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));

        return pluiMapper.entitiesToDto(pluis);

    }

}

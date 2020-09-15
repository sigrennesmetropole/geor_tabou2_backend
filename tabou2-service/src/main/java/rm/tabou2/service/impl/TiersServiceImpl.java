package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.TiersService;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.mapper.TiersMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.TiersDao;
import rm.tabou2.storage.tabou.entity.TiersEntity;

import java.util.List;

@Service
public class TiersServiceImpl implements TiersService {


    public static String DEFAULT_ORDER_BY = "nom";

    @Autowired
    private TiersDao tiersDao;

    @Autowired
    private TiersMapper tiersMapper;

    @Override
    public Tiers addTiers(Tiers tiers) {

        TiersEntity tiersEntity = tiersMapper.dtoToEntity(tiers);

        tiersEntity = tiersDao.save(tiersEntity);

        return tiersMapper.entityToDto(tiersEntity);

    }



    @Override
    public List<Tiers> searchTiers(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<TiersEntity> tiers = null;

        //Initialisation des variables
        orderBy = (orderBy == null) ? DEFAULT_ORDER_BY : orderBy;
        keyword = (keyword == null) ? "%" : "%" + keyword + "%";

        if (onlyActive) {
            tiers = tiersDao.findOnlyActiveByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));
        } else {
            tiers = tiersDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));
        }

        return tiersMapper.entitiesToDto(tiers);

    }


}

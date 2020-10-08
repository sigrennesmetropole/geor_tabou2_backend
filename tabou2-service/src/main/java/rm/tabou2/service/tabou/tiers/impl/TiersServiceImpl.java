package rm.tabou2.service.tabou.tiers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.mapper.tabou.tiers.TiersMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;

import java.util.List;

@Service
public class TiersServiceImpl implements TiersService {

    public static final String DEFAULT_ORDER_BY = "nom";

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
    public List<Tiers> searchTiers(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc){

        List<TiersEntity> tiers = null;

        //Initialisation des variables
        orderBy = (orderBy == null) ? DEFAULT_ORDER_BY : orderBy;
        keyword = (keyword == null) ? "%" : "%" + keyword + "%";

        if (Boolean.TRUE.equals(onlyActive)) {
            tiers = tiersDao.findOnlyActiveByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));
        } else {
            tiers = tiersDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));
        }

        return tiersMapper.entitiesToDto(tiers);

    }


}

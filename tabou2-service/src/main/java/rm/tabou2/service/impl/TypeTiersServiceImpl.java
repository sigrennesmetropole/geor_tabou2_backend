package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.TypeTiersService;
import rm.tabou2.service.dto.TypeTiers;
import rm.tabou2.service.mapper.TypeTiersMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.TypeTiersEntity;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TypeTiersServiceImpl implements TypeTiersService {

    @Autowired
    private TypeTiersDao typeTiersDao;

    @Autowired
    private TypeTiersMapper typeTiersMapper;

    @Override
    public TypeTiers addTypeTiers(TypeTiers typeTiers) {

        TypeTiersEntity typeTiersEntity = typeTiersMapper.dtoToEntity(typeTiers);

        typeTiersEntity = typeTiersDao.save(typeTiersEntity);

        return typeTiersMapper.entityToDto(typeTiersEntity);

    }

    @Override
    public TypeTiers getById(long typeTiersId) {

        Optional<TypeTiersEntity> typeTiersOpt = typeTiersDao.findById(typeTiersId);
        if (typeTiersOpt.isEmpty()) {
            throw new NoSuchElementException("Le type tiers  n'existe pas, id=" + typeTiersId);
        }

        return typeTiersMapper.entityToDto(typeTiersOpt.get());

    }

    @Override
    public TypeTiers editTypeTiers(TypeTiers typeTiers) {

        Optional<TypeTiersEntity> typeTiersOpt = typeTiersDao.findById(typeTiers.getId());
        if (typeTiersOpt.isEmpty()) {
            throw new NoSuchElementException("Le type tiers demandé n'existe pas , id=" + typeTiers.getId());
        }

        TypeTiersEntity typeTiersEntity = typeTiersOpt.get();
        typeTiersEntity.setLibelle(typeTiers.getLibelle());

        typeTiersDao.save(typeTiersEntity);

        return typeTiersMapper.entityToDto(typeTiersOpt.get());

    }

    @Override
    public TypeTiers inactivateTypeTiers(Long typeTiersId) {

        Optional<TypeTiersEntity> typeTiersOpt = typeTiersDao.findById(typeTiersId);
        if (typeTiersOpt.isEmpty()) {
            throw new NoSuchElementException("Le type tiers demandé n'existe pas, id=" + typeTiersId);
        }

        TypeTiersEntity typeTiers = typeTiersOpt.get();
        typeTiers.setDateInactif(new Date());

        typeTiersDao.save(typeTiers);

        return typeTiersMapper.entityToDto(typeTiers);

    }

    @Override
    public List<TypeTiers> searchTypeTiers(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc) {

        List<TypeTiersEntity> typesTiers = null;

        if (Boolean.TRUE.equals(onlyActive)) {
            typesTiers = typeTiersDao.findOnlyActiveByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));
        } else {
            typesTiers = typeTiersDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));
        }

        return typeTiersMapper.entitiesToDto(typesTiers);

    }


}

package rm.tabou2.service.tabou.tiers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.tiers.TiersRightsHelper;
import rm.tabou2.service.mapper.tabou.tiers.Tiers2Mapper;
import rm.tabou2.service.mapper.tabou.tiers.TiersMapper;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.storage.tabou.dao.tiers.TiersCustomDao;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.TiersCriteria;

import java.util.*;

@Service
public class TiersServiceImpl implements TiersService {


    @Autowired
    private TiersDao tiersDao;

    @Autowired
    private TiersMapper tiersMapper;

    @Autowired
    private Tiers2Mapper tiers2Mapper;

    @Autowired
    private TiersCustomDao tiersCustomDao;

    @Autowired
    private TiersRightsHelper tiersRightsHelper;

    @Override
    public Tiers getTiersById(Long tiersId) {
        Optional<TiersEntity> tiersEntityOpt = tiersDao.findById(tiersId);

        if (!tiersRightsHelper.checkCanGetTiers()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'accéder un tiers");
        }
        if (tiersEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le tiers id=" + tiersId + " n'existe pas");
        }

        return tiersMapper.entityToDto(tiersEntityOpt.get());
    }

    @Override
    public Tiers createTiers(Tiers tiers) {

        if (!tiersRightsHelper.checkCanCreateTiers()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de créer un tiers");
        }
        TiersEntity tiersEntity = tiersMapper.dtoToEntity(tiers);

        tiersEntity = tiersDao.save(tiersEntity);

        return tiersMapper.entityToDto(tiersEntity);

    }

    @Override
    public Tiers updateTiers(Tiers tiers) {

        if(!tiersRightsHelper.checkCanUpdateTiers()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'éditer un tiers");
        }
        TiersEntity tiersEntity = tiersMapper.dtoToEntity(tiers);

        tiersEntity = tiersDao.save(tiersEntity);

        return tiersMapper.entityToDto(tiersEntity);

    }

    @Override
    public Tiers inactivateTiers(long tiersId) throws AppServiceException {
        if(!tiersRightsHelper.checkCanUpdateTiers()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'éditer un tiers");
        }

        Optional<TiersEntity> tiersOpt = tiersDao.findById(tiersId);
        if (tiersOpt.isEmpty()) {
            throw new NoSuchElementException("Le tiers id=" + tiersId + " n'existe pas");
        }

        TiersEntity tiers = tiersOpt.get();
        tiers.setDateInactif(new Date());

        // Enregistrement en BDD
        try {
            tiers = tiersDao.save(tiers);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de rendre inactif le tiers " + tiersId, e);
        }

        return tiersMapper.entityToDto(tiers);

    }


    @Override
    public Page<Tiers> searchTiers(TiersCriteria tiersCriteria, Pageable pageable) {
        if (!tiersRightsHelper.checkCanGetTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de chercher un tiers");
        }
        return tiersMapper.entitiesToDto(tiersCustomDao.searchTiers(tiersCriteria, pageable), pageable);

    }


    @Override
    public Page<Tiers> searchTiers2(TiersCriteria tiersCriteria, Pageable pageable) {
        if (!tiersRightsHelper.checkCanGetTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de chercher un tiers");
        }
        return tiers2Mapper.entitiesToDto(tiersCustomDao.searchTiers(tiersCriteria, pageable), pageable);

    }

}

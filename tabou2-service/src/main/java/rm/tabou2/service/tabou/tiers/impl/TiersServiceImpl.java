package rm.tabou2.service.tabou.tiers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.mapper.tabou.tiers.TiersMapper;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.storage.tabou.dao.tiers.TiersCustomDao;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.TiersCriteria;

import java.util.*;

@Service
public class TiersServiceImpl implements TiersService {

    public static final String DEFAULT_ORDER_BY = "nom";

    @Autowired
    private TiersDao tiersDao;

    @Autowired
    private TiersMapper tiersMapper;

    @Autowired
    private TiersCustomDao tiersCustomDao;

    @Autowired
    private OperationTiersService operationTiersService;

    @Override
    public Tiers getTiersById(Long tiersId) {
        Optional<TiersEntity> tiersEntityOpt = tiersDao.findById(tiersId);

        if (tiersEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le tiers id=" + tiersId + " n'existe pas");
        }

        return tiersMapper.entityToDto(tiersEntityOpt.get());
    }

    @Override
    public Tiers createTiers(Tiers tiers) {

        TiersEntity tiersEntity = tiersMapper.dtoToEntity(tiers);

        tiersEntity = tiersDao.save(tiersEntity);

        return tiersMapper.entityToDto(tiersEntity);

    }

    @Override
    public Tiers updateTiers(Tiers tiers) {

        TiersEntity tiersEntity = tiersMapper.dtoToEntity(tiers);

        tiersEntity = tiersDao.save(tiersEntity);

        return tiersMapper.entityToDto(tiersEntity);

    }

    @Override
    public Tiers inactivateTiers(long tiersId) throws AppServiceException {

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
        return tiersMapper.entitiesToDto(tiersCustomDao.searchTiers(tiersCriteria, pageable), pageable);

    }

    @Override
    public List<Tiers> getTiersByOperationId(Long operationId) {

        List<TiersEntity> tiers = new ArrayList<>();

        List<OperationTiersEntity> operationTiers = operationTiersService.getTiersByOperationId(operationId);

        for (OperationTiersEntity opTiers : operationTiers) {
            tiers.add(opTiers.getTiers());

        }

        return tiersMapper.entitiesToDto(tiers);

    }


}

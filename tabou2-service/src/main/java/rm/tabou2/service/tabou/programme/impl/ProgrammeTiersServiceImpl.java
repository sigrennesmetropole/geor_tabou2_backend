package rm.tabou2.service.tabou.programme.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.TiersAmenagement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.mapper.tabou.operation.ProgrammeTiersMapper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeTiersService;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;


import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProgrammeTiersServiceImpl implements ProgrammeTiersService {

    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private TypeTiersDao typeTiersDao;

    @Autowired
    private TiersDao tiersDao;

    @Autowired
    private ProgrammeTiersDao programmeTiersDao;

    @Autowired
    private ProgrammeTiersCustomDao programmeTiersCustomDao;

    @Autowired
    private ProgrammeTiersMapper programmeTiersMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public Programme associateTiersToProgramme(long programmeId, long tiersId, long typeTiersId) throws AppServiceException {

        ProgrammeTiersEntity programmeTiersEntity = new ProgrammeTiersEntity();

        // récuperer type tiers
        Optional<TypeTiersEntity> typeTiersEntityOpt = typeTiersDao.findById(typeTiersId);
        if (typeTiersEntityOpt.isEmpty()) {
            throw new AppServiceException("Le typeTiers demandé n'existe pas, id=" + typeTiersId);
        }
        programmeTiersEntity.setTypeTiers(typeTiersEntityOpt.get());

        // récuperer tiers
        Optional<TiersEntity> tiersEntityOpt = tiersDao.findById(tiersId);
        if (tiersEntityOpt.isEmpty()) {
            throw new AppServiceException("Le tiers demandé n'existe pas, id=" + tiersId);
        }
        programmeTiersEntity.setTiers(tiersEntityOpt.get());

        // récuperer programme
        Optional<ProgrammeEntity> programmeEntityOpt = programmeDao.findById(programmeId);
        if (programmeEntityOpt.isEmpty()) {
            throw new AppServiceException("Le programme demandé n'existe pas, id=" + programmeId);
        }
        programmeTiersEntity.setProgramme(programmeEntityOpt.get());

        //DATE
        programmeTiersEntity.setCreateDate(new Date());

        //Utilisateur
        programmeTiersEntity.setCreateUser(authentificationHelper.getConnectedUsername());

        try {
            programmeTiersDao.save(programmeTiersEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'associer le programme id=" + programmeId + " au typeTiersId=" + typeTiersId + " et au tiersId=" + tiersId, e);
        }

        return programmeService.getProgrammeById(programmeId);
    }

    @Override
    @Transactional
    public Page<TiersAmenagement> searchProgrammeTiers(TiersAmenagementCriteria criteria, Pageable pageable) {

        Optional<ProgrammeEntity> optional = programmeDao.findById(criteria.getProgrammeId());

        if (optional.isEmpty()) {
            throw new NoSuchElementException("L'operation' = " + criteria.getOperationId() + " n'existe pas");
        }
        // Si diffusion restreinte et utilisateur non referent
        if ((boolean) optional.get().isDiffusionRestreinte() && !authentificationHelper.hasReferentRole()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0); // On retourne une page vide
        }


        return programmeTiersMapper.entitiesToDto(programmeTiersCustomDao.searchProgrammesTiers(criteria, pageable),pageable);

    }
}

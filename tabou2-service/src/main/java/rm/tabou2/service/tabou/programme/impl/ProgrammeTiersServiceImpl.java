package rm.tabou2.service.tabou.programme.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.TiersAmenagement;
import rm.tabou2.service.dto.TiersTypeTiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.mapper.tabou.operation.ProgrammeTiersMapper;
import rm.tabou2.service.mapper.tabou.tiers.TiersMapper;
import rm.tabou2.service.mapper.tabou.tiers.TypeTiersMapper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeTiersService;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
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
    private TiersMapper tiersMapper;

    @Autowired
    private TypeTiersMapper typeTiersMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public AssociationTiersTypeTiers associateTiersToProgramme(long programmeId, long tiersId, long typeTiersId) throws AppServiceException {

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
            //Enregistrement des modification
            programmeTiersEntity = programmeTiersDao.save(programmeTiersEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'associer le programme id=" + programmeId + " au typeTiersId=" + typeTiersId + " et au tiersId=" + tiersId, e);
        }

        return getAssociationById(programmeTiersEntity.getId());

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

    @Override
    public AssociationTiersTypeTiers updateTiersAssociation(long programmeId, long programmeTiersId, TiersTypeTiers tiersTypeTiers) throws AppServiceException {

        Optional<ProgrammeTiersEntity> programmeTiersOpt = programmeTiersDao.findById(programmeTiersId);
        if (programmeTiersOpt.isEmpty()) {
            throw new NoSuchElementException("Le programmeTiers id=" + programmeTiersId + " n'existe pas");
        }

        ProgrammeTiersEntity programmeTiersEntity = programmeTiersOpt.get();

        if (programmeId != programmeTiersEntity.getProgramme().getId()) {
            throw new AppServiceException("Opération non autorisée : modifier le programmeTiers id=" + programmeTiersId + " pour le programme id =" + programmeId);
        }

        // Vérification si type tiers existe
        Optional<TypeTiersEntity> typeTiersEntityOpt = typeTiersDao.findById(tiersTypeTiers.getTypeTiersId());
        if (typeTiersEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le typeTiersId = " + tiersTypeTiers.getTypeTiersId() + " n'existe pas");
        }
        programmeTiersEntity.setTypeTiers(typeTiersEntityOpt.get());

        // Vérification si tiers existe
        Optional<TiersEntity> tiersEntityOpt = tiersDao.findById(tiersTypeTiers.getTiersId());
        if (tiersEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le tiersId = " + tiersTypeTiers.getTiersId() + " n'existe pas");
        }
        programmeTiersEntity.setTiers(tiersEntityOpt.get());

        programmeTiersEntity.setModifDate(new Date());
        programmeTiersEntity.setModifUser(authentificationHelper.getConnectedUsername());

        //Enregistrement des modifications
        programmeTiersDao.save(programmeTiersEntity);

        return getAssociationById(programmeTiersEntity.getId());

    }

    @Override
    public void deleteTiersByProgrammeId(long programmeId, long programmeTiersId) throws AppServiceException {

        Optional<ProgrammeTiersEntity> programmeTiersOpt = programmeTiersDao.findById(programmeTiersId);
        if (programmeTiersOpt.isEmpty()) {
            throw new NoSuchElementException("Le programmeTiers id=" + programmeTiersId + " n'existe pas");
        }

        if (programmeId != programmeTiersOpt.get().getProgramme().getId()) {
            throw new AppServiceException("Opération non autorisée : modifier le programmeTiers id=" + programmeTiersId + " pour le programme id =" + programmeId);
        }

        programmeTiersDao.deleteById(programmeTiersId);


    }

    private AssociationTiersTypeTiers getAssociationById(long programmeTiersId) {

        Optional<ProgrammeTiersEntity> programmeTiersOpt = programmeTiersDao.findById(programmeTiersId);
        if (programmeTiersOpt.isEmpty()) {
            throw new NoSuchElementException("L'objet programmeTiers id = " + programmeTiersId);
        }

        ProgrammeTiersEntity programmeTiers = programmeTiersOpt.get();

        AssociationTiersTypeTiers associationTiersTypeTiers = new AssociationTiersTypeTiers();

        associationTiersTypeTiers.setId(programmeTiers.getId());
        associationTiersTypeTiers.setTiers(tiersMapper.entityToDto(programmeTiers.getTiers()));
        associationTiersTypeTiers.setTypeTiers(typeTiersMapper.entityToDto(programmeTiers.getTypeTiers()));

        return associationTiersTypeTiers;


    }
}

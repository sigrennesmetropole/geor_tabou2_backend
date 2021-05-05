package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.TiersAmenagement;
import rm.tabou2.service.dto.TiersTypeTiers;
import rm.tabou2.service.mapper.tabou.operation.OperationTiersMapper;
import rm.tabou2.service.mapper.tabou.tiers.TiersMapper;
import rm.tabou2.service.mapper.tabou.tiers.TypeTiersMapper;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.service.tabou.tiers.TypeTiersService;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.operation.OperationTiersCustomDao;
import rm.tabou2.storage.tabou.dao.operation.OperationTiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OperationTiersServiceImpl implements OperationTiersService {


    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationTiersCustomDao operationTiersCustomDao;

    @Autowired
    private TypeTiersDao typeTiersDao;

    @Autowired
    private TiersDao tiersDao;

    @Autowired
    private TypeTiersService typeTiersService;

    @Autowired
    private TiersService tiersService;

    @Autowired
    private OperationTiersDao operationTiersDao;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationTiersMapper operationTiersMapper;

    @Autowired
    private TiersMapper tiersMapper;

    @Autowired
    private TypeTiersMapper typeTiersMapper;



    @Override
    public Operation associateTiersToOperation(long operationId, long tiersId, long typeTiersId) throws AppServiceException {

        OperationTiersEntity operationTiersEntity = new OperationTiersEntity();

        // récuperer type tiers
        Optional<TypeTiersEntity> typeTiersEntityOpt = typeTiersDao.findById(typeTiersId);
        if (typeTiersEntityOpt.isEmpty()) {
            throw new AppServiceException("Le typeTiersId = " + typeTiersId + " n'existe pas");
        }
        operationTiersEntity.setTypeTiers(typeTiersEntityOpt.get());


        // récuperer tiers
        Optional<TiersEntity> tiersEntityOpt = tiersDao.findById(tiersId);
        if (tiersEntityOpt.isEmpty()) {
            throw new AppServiceException("Le tiersID = " + tiersId + "n'existe pas");
        }
        operationTiersEntity.setTiers(tiersEntityOpt.get());

        // récuperer opération
        Optional<OperationEntity> operationEntityOpt = operationDao.findById(operationId);
        if (operationEntityOpt.isEmpty()) {
            throw new AppServiceException("L' operationId = " + operationId + "n'existe pas");
        }
        operationTiersEntity.setOperation(operationEntityOpt.get());

        operationTiersEntity.setCreateDate(new Date());
        operationTiersEntity.setCreateUser(authentificationHelper.getConnectedUsername());


        try {
            operationTiersDao.save(operationTiersEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter l'opération tiers", e);
        }


        return operationService.getOperationById(operationId);

    }


    @Override
    @Transactional
    public Page<TiersAmenagement> searchOperationTiers(TiersAmenagementCriteria criteria, Pageable pageable) throws AppServiceException {

        Optional<OperationEntity> optional = operationDao.findById(criteria.getOperationId());

        if (optional.isEmpty()) {
            throw new AppServiceException("L'operation' = " + criteria.getOperationId() + " n'existe pas");
        }
        // Si diffusion restreinte et utilisateur non referent
        if (optional.get().isDiffusionRestreinte() && !authentificationHelper.hasReferentRole()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0); // On retourne une page vide
        }

         return operationTiersMapper.entitiesToDto(operationTiersCustomDao.searchOperationTiers(criteria, pageable),pageable);

    }

    @Override
    public AssociationTiersTypeTiers updateTiersAssociation(long operationId, long operationTiersId, TiersTypeTiers tiersTypeTiers) throws AppServiceException {

        Optional<OperationTiersEntity> operationTiersOpt = operationTiersDao.findById(operationTiersId);
        if (operationTiersOpt.isEmpty()) {
            throw new NoSuchElementException("L'operationTiers id=" + operationTiersId + " n'existe pas");
        }

        OperationTiersEntity operationTiersEntity = operationTiersOpt.get();

        if (operationId != operationTiersEntity.getOperation().getId()) {
            throw new AppServiceException("Opération non autorisée : modifier l'operationTiers id=" + operationTiersId + " pour l'operation id =" + operationId);
        }

        // Vérification si type tiers existe
        Optional<TypeTiersEntity> typeTiersEntityOpt = typeTiersDao.findById(tiersTypeTiers.getTypeTiersId());
        if (typeTiersEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le typeTiersId = " + tiersTypeTiers.getTypeTiersId() + " n'existe pas");
        }
        operationTiersEntity.setTypeTiers(typeTiersEntityOpt.get());

        // Vérification si tiers existe
        Optional<TiersEntity> tiersEntityOpt = tiersDao.findById(tiersTypeTiers.getTiersId());
        if (tiersEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le tiersId = " + tiersTypeTiers.getTiersId() + " n'existe pas");
        }
        operationTiersEntity.setTiers(tiersEntityOpt.get());

        operationTiersEntity.setModifDate(new Date());
        operationTiersEntity.setModifUser(authentificationHelper.getConnectedUsername());

        //Enregistrement des modification
        operationTiersDao.save(operationTiersEntity);

        return getAssociationById(operationTiersEntity.getId());

    }

    @Override
    public void deleteTiersByOperationId(long operationId, long operationTiersId) throws AppServiceException {

        Optional<OperationTiersEntity> operationTiersOpt = operationTiersDao.findById(operationTiersId);
        if (operationTiersOpt.isEmpty()) {
            throw new NoSuchElementException("L'operationTiers id=" + operationTiersId + " n'existe pas");
        }

        if (operationId != operationTiersOpt.get().getOperation().getId()) {
            throw new AppServiceException("Opération non autorisée : modifier l'operationTiers id=" + operationTiersId + " pour l'operation id =" + operationId);
        }

        operationTiersDao.deleteById(operationTiersId);


    }

    public AssociationTiersTypeTiers getAssociationById(long operationTiersId) {

        Optional<OperationTiersEntity> operationTiersOpt = operationTiersDao.findById(operationTiersId);
        if (operationTiersOpt.isEmpty()) {
            throw new NoSuchElementException("L'objet operationTiers id = " + operationTiersId);
        }

        OperationTiersEntity operationTiers = operationTiersOpt.get();

        AssociationTiersTypeTiers associationTiersTypeTiers = new AssociationTiersTypeTiers();

        associationTiersTypeTiers.setId(operationTiers.getId());
        associationTiersTypeTiers.setTiers(tiersMapper.entityToDto(operationTiers.getTiers()));
        associationTiersTypeTiers.setTypeTiers(typeTiersMapper.entityToDto(operationTiers.getTypeTiers()));

        return associationTiersTypeTiers;


    }

}

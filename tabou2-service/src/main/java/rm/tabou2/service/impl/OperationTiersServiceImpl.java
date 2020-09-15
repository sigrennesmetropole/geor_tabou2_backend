package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.OperationTiersService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.utils.AuthentificationUtils;
import rm.tabou2.storage.tabou.dao.OperationDao;
import rm.tabou2.storage.tabou.dao.OperationTiersDao;
import rm.tabou2.storage.tabou.dao.TiersDao;
import rm.tabou2.storage.tabou.dao.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.OperationEntity;
import rm.tabou2.storage.tabou.entity.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.TiersEntity;
import rm.tabou2.storage.tabou.entity.TypeTiersEntity;

import java.util.Date;
import java.util.Optional;

@Service
public class OperationTiersServiceImpl implements OperationTiersService {


    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private TypeTiersDao typeTiersDao;

    @Autowired
    private TiersDao tiersDao;

    @Autowired
    private OperationTiersDao operationTiersDao;

    @Autowired
    private AuthentificationUtils authentificationUtils;


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
        operationTiersEntity.setCreateUser(authentificationUtils.getConnectedUsername());


        try {
            operationTiersDao.save(operationTiersEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter l'opération tiers", e);
        }


        return operationService.getOperationById(operationId);

    }

}

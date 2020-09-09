package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.OperationTiersService;
import rm.tabou2.service.ProgrammeService;
import rm.tabou2.service.TypeTiersService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.storage.tabou.dao.*;
import rm.tabou2.storage.tabou.entity.*;

import java.util.Date;
import java.util.Optional;

@Service
public class OperationTiersServiceImpl implements OperationTiersService {

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private ProgrammeService programmeService;

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

    @Override
    public Programme associateTiersToProgramme(long tiersId, long programmeId, long typeTiersId) {

        associateTiers(tiersId, programmeId, 0, typeTiersId);

        return programmeService.getProgrammeById(programmeId);

    }

    @Override
    public Operation associateTiersToOperation(long tiersId, long operationId, long typeTiersId) {

        associateTiers(tiersId, 0, operationId, typeTiersId);

        return operationService.getOperationById(operationId);

    }


    @Override
    public void associateTiers(long tiersId, long programmeId, long operationId, long typeTiersId) {

        Optional<TypeTiersEntity>  typeTiersEntityOpt = typeTiersDao.findById(typeTiersId);
        if (null == typeTiersEntityOpt || typeTiersEntityOpt.isEmpty()) {
            //TODO : exception : illegalArgumentException
        }

        Optional<TiersEntity> tiersEntityOpt = tiersDao.findById(tiersId);
        if (null == typeTiersEntityOpt || typeTiersEntityOpt.isEmpty()) {
            //TODO : exception
        }

        OperationTiersEntity operationTiersEntity = new OperationTiersEntity();

        operationTiersEntity.setCreateDate(new Date());
        //TODO : association user
        //operationTiersEntity.setCreateUser();

        if (programmeId > 0 ){
            Optional<ProgrammeEntity> programmeEntityOpt = programmeDao.findById(programmeId);
            if (null == programmeEntityOpt || programmeEntityOpt.isEmpty()) {
                //TODO : exception
            }
            operationTiersEntity.setProgramme(programmeEntityOpt.get());
        } else if (operationId > 0) {
            Optional<OperationEntity> operationEntityOpt = operationDao.findById(operationId);
            if (null == operationEntityOpt || operationEntityOpt.isEmpty()) {
                //TODO : exception
            }
            operationTiersEntity.setOperation(operationEntityOpt.get());
        }

        operationTiersEntity.setTypeTiers(typeTiersEntityOpt.get());
        operationTiersEntity.setTiers(tiersEntityOpt.get());

        operationTiersDao.save(operationTiersEntity);

    }

}

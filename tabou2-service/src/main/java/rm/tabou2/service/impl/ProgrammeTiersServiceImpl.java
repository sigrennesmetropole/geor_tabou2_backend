package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.ProgrammeService;
import rm.tabou2.service.ProgrammeTiersService;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.storage.tabou.dao.ProgrammeDao;
import rm.tabou2.storage.tabou.dao.ProgrammeTiersDao;
import rm.tabou2.storage.tabou.dao.TiersDao;
import rm.tabou2.storage.tabou.dao.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.ProgrammeEntity;
import rm.tabou2.storage.tabou.entity.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.entity.TiersEntity;
import rm.tabou2.storage.tabou.entity.TypeTiersEntity;

import java.util.Date;
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
}

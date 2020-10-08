package rm.tabou2.service.tabou.programme.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.tabou.agaepo.AgapeoService;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.dto.Logements;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.mapper.tabou.programme.ProgrammeMapper;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProgrammeServiceImpl implements ProgrammeService {

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private AgapeoService agapeoService;

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public Programme addProgramme(Programme programme) {

        ProgrammeEntity programmeEntity = programmeMapper.dtoToEntity(programme);

        //Ajout des dates et infos sur l'utilisateur connecté
        programmeEntity.setCreateDate(new Date());
        programmeEntity.setModifDate(new Date());
        programmeEntity.setCreateUser(authentificationHelper.getConnectedUsername());
        programmeEntity.setModifUser(authentificationHelper.getConnectedUsername());

        programmeEntity = programmeDao.save(programmeEntity);

        return programmeMapper.entityToDto(programmeEntity);

    }

    @Override
    public Programme getProgrammeById(long programmeId) {

        Optional<ProgrammeEntity> programmeEntityOpt = programmeDao.findById(programmeId);

        if (programmeEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le programme id=" + programmeId + " n'existe pas");
        }

        return programmeMapper.entityToDto(programmeEntityOpt.get());

    }

    @Override
    public List<Programme> searchProgrammes(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc)  {

        List<ProgrammeEntity> programmes = programmeDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ProgrammeEntity.class));

        return programmeMapper.entitiesToDto(programmes);

    }

    public Logements getLogements() {

        //Faire la comparaison entre le nombre de logements issus de la couche agapeo

        return null;

    }


}

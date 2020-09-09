package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.AgapeoService;
import rm.tabou2.service.ProgrammeService;
import rm.tabou2.service.dto.Logements;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.mapper.ProgrammeMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.ProgrammeEntity;

import java.util.List;
import java.util.Optional;

@Service
public class ProgrammeServiceImpl implements ProgrammeService {

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private AgapeoService agapeoService;

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Override
    public Programme addProgramme(Programme programme) {

        ProgrammeEntity programmeEntity = programmeMapper.dtoToEntity(programme);

        programmeEntity = programmeDao.save(programmeEntity);

        return programmeMapper.entityToDto(programmeEntity);

    }

    @Override
    public Programme getProgrammeById(long programmeId) {

        Optional<ProgrammeEntity> programmeEntityOpt = programmeDao.findById(programmeId);

        if (null == programmeEntityOpt || programmeEntityOpt.isEmpty()) {
            //TODO : exception
            //throw new AppServiceNotFoundException("Le programme id=" + programmeId + " n'existe pas");
        }

        return programmeMapper.entityToDto(programmeEntityOpt.get());

    }

    @Override
    public List<Programme> searchProgrammes(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<ProgrammeEntity> programmes = programmeDao.findByKeyword(keyword, Utils.buildPageable(start, resultsNumber, orderBy, asc));

        return programmeMapper.entitiesToDto(programmes);

    }

    public Logements getLogements() {

        //Faire la comparaison entre le nombre de logements issus de la couche agapeo

        //logementsAccesSociale
        //logementsLocatifRegule
        //logementsPls
        //logementsLocatifSocial
        //if (agapeoService.get)


        return null;

    }


}

package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.AgapeoService;
import rm.tabou2.service.ProgrammeService;
import rm.tabou2.service.dto.Logements;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.mapper.ProgrammeMapper;
import rm.tabou2.storage.tabou.dao.AgapeoDao;
import rm.tabou2.storage.tabou.dao.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.ProgrammeEntity;

import java.util.List;

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

package rm.tabou2.service.tabou.agaepo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.service.mapper.tabou.agapeo.AgapeoMapper;
import rm.tabou2.service.tabou.agaepo.AgapeoService;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@Service
public class AgapeoServiceImpl implements AgapeoService {

    @Autowired
    private AgapeoMapper agapeoMapper;

    @Autowired
    private AgapeoCustomDao agapeoCustomDao;

    @Autowired
    ProgrammeDao programmeDao;

    @Override
    public Page<Agapeo> getApapeosByProgrammeId(long programmeId, Pageable pageable) {

        ProgrammeEntity programmeEntity = programmeDao.findOneById(programmeId);
        if (programmeEntity == null) {
            throw new IllegalArgumentException("L'identifiant du programme est invalide: aucun programme trouvé pour l'id = " + programmeId);
        }

        return agapeoMapper.entitiesToDto(agapeoCustomDao.searchAgapeo(programmeEntity.getNumAds(), pageable), pageable);
    }
}

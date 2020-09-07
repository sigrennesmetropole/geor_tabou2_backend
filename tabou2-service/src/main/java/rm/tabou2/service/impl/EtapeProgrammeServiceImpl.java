package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.EtapeProgrammeService;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.EtapeProgrammeMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.entity.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.EtapeProgrammeEntity;

import java.util.List;
import java.util.Optional;

@Service
public class EtapeProgrammeServiceImpl implements EtapeProgrammeService {

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Override
    public List<Etape> searchEtapesProgramme(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<EtapeProgrammeEntity> etapes = etapeProgrammeDao.findByKeyword(keyword, Utils.buildPageable(start, resultsNumber, orderBy, asc));

        return etapeProgrammeMapper.entitiesToDto(etapes);

    }

    @Override
    public Etape addEtapeProgramme(Etape etape) {

        if (null == etape) {
            //TODO exception
        }

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeMapper.dtoToEntity(etape);

        etapeProgrammeEntity = etapeProgrammeDao.save(etapeProgrammeEntity);

        return etapeProgrammeMapper.entityToDto(etapeProgrammeEntity);

    }

    @Override
    public Etape getEtapeProgrammeById(long etapeProgrammeId) {

        Optional<EtapeProgrammeEntity> etapeProgrammeEntity = etapeProgrammeDao.findById(etapeProgrammeId);

        if (null == etapeProgrammeEntity || etapeProgrammeEntity.isEmpty()) {
            //TODO : exception
            return null;
        }

        return etapeProgrammeMapper.entityToDto(etapeProgrammeEntity.get());
    }

    @Override
    public List<Etape> getEtapesForProgrammes() {

        List<EtapeProgrammeEntity> etapesProgrammeEntity = etapeProgrammeDao.findAll();

        if (null == etapesProgrammeEntity || etapesProgrammeEntity.isEmpty()) {
            //TODO : exception
            return null;
        }

        return etapeProgrammeMapper.entitiesToDto(etapesProgrammeEntity);
    }

}

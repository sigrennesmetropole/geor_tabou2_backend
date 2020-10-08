package rm.tabou2.service.tabou.programme.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.tabou.programme.EtapeProgrammeService;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EtapeProgrammeServiceImpl implements EtapeProgrammeService {

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Override
    public List<Etape> searchEtapesProgramme(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        List<EtapeProgrammeEntity> etapes = etapeProgrammeDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EtapeProgrammeEntity.class));

        return etapeProgrammeMapper.entitiesToDto(etapes);

    }

    @Override
    public Etape addEtapeProgramme(Etape etape) {

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeMapper.dtoToEntity(etape);

        etapeProgrammeEntity = etapeProgrammeDao.save(etapeProgrammeEntity);

        return etapeProgrammeMapper.entityToDto(etapeProgrammeEntity);

    }

    @Override
    public Etape getEtapeProgrammeById(long etapeProgrammeId) {

        Optional<EtapeProgrammeEntity> etapeProgrammeEntity = etapeProgrammeDao.findById(etapeProgrammeId);

        if (etapeProgrammeEntity.isEmpty()) {
            throw new NoSuchElementException("L'étape de programme demandée n'existe pas, id=" + etapeProgrammeId);
        }

        return etapeProgrammeMapper.entityToDto(etapeProgrammeEntity.get());
    }

    @Override
    public List<Etape> getEtapesForProgrammes() {

        List<EtapeProgrammeEntity> etapesProgrammeEntity = etapeProgrammeDao.findAll();

        return etapeProgrammeMapper.entitiesToDto(etapesProgrammeEntity);
    }

}

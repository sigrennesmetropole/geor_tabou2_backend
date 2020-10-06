package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.EtapeProgrammeService;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.helper.EtapeProgrammeWorkflowHelper;
import rm.tabou2.service.mapper.EtapeProgrammeMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.entity.EtapeProgrammeEntity;

import java.util.List;

@Service
public class EtapeProgrammeServiceImpl implements EtapeProgrammeService {

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Autowired
    private EtapeProgrammeWorkflowHelper etapeProgrammeWorkflowHelper;

    @Override
    public List<Etape> searchEtapesProgramme(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<EtapeProgrammeEntity> etapes = etapeProgrammeDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));

        return etapeProgrammeMapper.entitiesToDto(etapes);

    }

    @Override
    public Etape addEtapeProgramme(Etape etape) {

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeMapper.dtoToEntity(etape);

        etapeProgrammeEntity = etapeProgrammeDao.save(etapeProgrammeEntity);

        return etapeProgrammeMapper.entityToDto(etapeProgrammeEntity);

    }

    @Override
    public List<Etape> getEtapesForProgrammeById(long programmeId) {
        return etapeProgrammeWorkflowHelper.getPossibleEtapesForProgramme(programmeId);
    }

    @Override
    public List<Etape> getEtapesForProgrammes() {

        List<EtapeProgrammeEntity> etapesProgrammeEntity = etapeProgrammeDao.findAll();

        return etapeProgrammeMapper.entitiesToDto(etapesProgrammeEntity);
    }

}

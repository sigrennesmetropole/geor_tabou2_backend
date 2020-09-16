package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.EtapeOperationService;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.EtapeOperationMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.EtapeOperationDao;
import rm.tabou2.storage.tabou.entity.EtapeOperationEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EtapeOperationServiceImpl implements EtapeOperationService {

    @Autowired
    private EtapeOperationDao etapeOperationDao;

    @Autowired
    private EtapeOperationMapper etapeOperationMapper;

    @Override
    public List<Etape> searchEtapesOperation(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<EtapeOperationEntity> etapes = etapeOperationDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));

        return etapeOperationMapper.entitiesToDto(etapes);

    }

    @Override
    public Etape addEtapeOperation(Etape etape) {

        EtapeOperationEntity etapeOperationEntity = etapeOperationMapper.dtoToEntity(etape);

        etapeOperationEntity = etapeOperationDao.save(etapeOperationEntity);

        return etapeOperationMapper.entityToDto(etapeOperationEntity);

    }

    @Override
    public Etape getEtapeOperationById(long etapeOperationId) {

        Optional<EtapeOperationEntity> etapeOperationEntity = etapeOperationDao.findById(etapeOperationId);

        if (etapeOperationEntity.isEmpty()) {
            throw new NoSuchElementException("L'étape d'opération demandée n'existe pas, id=" + etapeOperationId);
        }

        return etapeOperationMapper.entityToDto(etapeOperationEntity.get());
    }

    @Override
    public List<Etape> getEtapesForOperation() {

        List<EtapeOperationEntity> etapesOperationEntity = etapeOperationDao.findAll();

        return etapeOperationMapper.entitiesToDto(etapesOperationEntity);
    }


}

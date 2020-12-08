package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.helper.operation.EtapeOperationWorkflowHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.service.tabou.operation.EtapeOperationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EtapeOperationServiceImpl implements EtapeOperationService {

    @Autowired
    private EtapeOperationDao etapeOperationDao;

    @Autowired
    private EtapeOperationMapper etapeOperationMapper;

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private EtapeOperationWorkflowHelper etapeOperationWorkflowHelper;

    @Override
    public List<Etape> searchEtapesOperation(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        List<EtapeOperationEntity> etapes = etapeOperationDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EtapeOperationEntity.class));

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
    public List<Etape> getEtapesForOperationById(long operationId) {
        if (!operationRightsHelper.checkCanGetEtapesForOperation(operationId)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer la liste des étapes pour l'opération id = " + operationId);
        }
        return etapeOperationWorkflowHelper.getAccessibleEtapesForOperation(operationId);
    }

    @Override
    public List<Etape> getEtapesForOperation() {

        List<EtapeOperationEntity> etapesOperationEntity = etapeOperationDao.findAll();

        return etapeOperationMapper.entitiesToDto(etapesOperationEntity);
    }


}

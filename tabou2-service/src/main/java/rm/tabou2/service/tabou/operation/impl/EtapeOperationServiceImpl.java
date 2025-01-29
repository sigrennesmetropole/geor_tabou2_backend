package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.EtapeOperationWorkflowHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationRestrictedMapper;
import rm.tabou2.service.tabou.operation.EtapeOperationService;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationCustomDao;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EtapeOperationServiceImpl implements EtapeOperationService {

    private final EtapeOperationDao etapeOperationDao;

    private final EtapeOperationCustomDao etapeOperationCustomDao;

    private final EtapeOperationMapper etapeOperationMapper;

    private final EtapeOperationRestrictedMapper etapeOperationRestrictedMapper;

    private final OperationRightsHelper operationRightsHelper;

    private final EtapeOperationWorkflowHelper etapeOperationWorkflowHelper;

    private final AuthentificationHelper authentificationHelper;

    @Override
    public Page<EtapeRestricted> searchEtapesOperation(EtapeCriteria etapeCriteria, Pageable pageable) {

        // si l'utilisateur n'a pas le rôle "APPS_TABOU_REFERENT" alors le filtre mode="PUBLIC" s'applique
        if (!authentificationHelper.hasReferentRole()) {
            etapeCriteria.setMode(Etape.ModeEnum.PUBLIC.toString());
        }

        Page<EtapeOperationEntity> etapes = etapeOperationCustomDao.searchEtapeOperations(etapeCriteria, pageable);

        return etapeOperationRestrictedMapper.entitiesToDto(etapes, pageable);

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

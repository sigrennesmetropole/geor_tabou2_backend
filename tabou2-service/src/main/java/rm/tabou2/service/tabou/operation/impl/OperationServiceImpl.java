package rm.tabou2.service.tabou.operation.impl;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.dao.operation.OperationCustomDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.NoSuchElementException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
@Validated
@Transactional(readOnly = true)
public class OperationServiceImpl implements OperationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationServiceImpl.class);

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationCustomDao operationCustomDao;

    @Autowired
    private EtapeOperationDao etapeOperationDao;

    @Autowired
    private OperationEmpriseHelper operationEmpriseHelper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private EtapeOperationMapper etapeOperationMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private OperationService me;

    @Override
    @Transactional
    public Operation createOperation(Operation operation) {

        // Ajout des valeurs par défaut
        setOperationDefaultValue(operation);

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanCreateOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création de l'operation " + operation.getNom());
        }

        // Ajout de l'état initial
        String code = BooleanUtils.isTrue(operation.isDiffusionRestreinte()) ? "EN_PROJET_OFF" : "EN_PROJET_PUBLIC";

        EtapeOperationEntity etapeOperationEntity = etapeOperationDao.findByTypeAndCode(Etape.TypeEnum.START.toString(), code);
        if (etapeOperationEntity == null) {
            throw new NoSuchElementException("Aucune étape initiale de type " + Etape.TypeEnum.START.toString() + " n'a été " +
                    "défini pour les opérations avec diffusion restreinte = " + operation.isDiffusionRestreinte());
        }

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);
        operationEntity.setEtapeOperation(etapeOperationEntity);

        operationDao.save(operationEntity);
        Operation operationSaved = operationMapper.entityToDto(operationEntity);

        operationEmpriseHelper.saveEmprise(operationSaved, operation.getIdEmprise());

        return operationSaved;

    }

    @Override
    @Transactional
    public Operation updateOperation(Operation operation) {

        OperationEntity operationEntity = operationDao.findOneById(operation.getId());

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanUpdateOperation(operation, operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification de l'operation " + operation.getNom());
        }

        // Mise à jour de la diffusion restreinte à partir de l'étape
        EtapeOperationEntity etapeOperationEntity = etapeOperationDao.findOneById(operation.getEtape().getId());
        operation.setDiffusionRestreinte(null);
        if (etapeOperationEntity.isRemoveRestriction()) {
            operation.setDiffusionRestreinte(false);
        }

        operationMapper.dtoToEntity(operation, operationEntity);

        operationEntity = operationDao.save(operationEntity);

        return operationMapper.entityToDto(operationEntity);

    }

    @Override
    @Transactional
    public Operation updateEtapeOfOperationId(long operationId, long etapeId) {
        EtapeOperationEntity etapeOperationEntity = etapeOperationDao.findOneById(etapeId);

        Operation operation = getOperationById(operationId);
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntity));
        return me.updateOperation(operation);
    }

    @Override
    public Page<Operation> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable) {
        // Si l'utilisateur n'a pas le droit de voir les opérations en diffusion restreinte, on filtre sur false
        if (BooleanUtils.isTrue(operationsCriteria.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            operationsCriteria.setDiffusionRestreinte(false);
            LOGGER.warn("Accès non autorisé à des opérations d'accès restreint");
        }
        return operationMapper.entitiesToDto(operationCustomDao.searchOperations(operationsCriteria, pageable),pageable);
    }


    @Override
    public Operation getOperationById(long operationId) {

        OperationEntity operationEntity = operationDao.findOneById(operationId);

        Operation operation = operationMapper.entityToDto(operationEntity);

        if (!operationRightsHelper.checkCanGetOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer l'opération id = " + operationId);
        }

        return operation;

    }

    /**
     * Ajout des valeurs par défaut pour une opération
     * @param operation opération
     */
    private void setOperationDefaultValue(Operation operation) {
        if (operation.isDiffusionRestreinte() == null) {
            operation.setDiffusionRestreinte(true);
        }
        if (operation.isSecteur() == null) {
            operation.setSecteur(false);
        }
    }

}

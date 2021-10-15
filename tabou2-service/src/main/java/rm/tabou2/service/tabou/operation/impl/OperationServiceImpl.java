package rm.tabou2.service.tabou.operation.impl;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.EvenementOperationRightsHelper;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.EvenementOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.dao.operation.EvenementOperationDao;
import rm.tabou2.storage.tabou.dao.operation.OperationCustomDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    private EvenementOperationDao evenementOperationDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private OperationEmpriseHelper operationEmpriseHelper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private EvenementOperationRightsHelper evenementOperationRightsHelper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private EtapeOperationMapper etapeOperationMapper;

    @Autowired
    private EvenementOperationMapper evenementOperationMapper;

    @Autowired
    private OperationService me;

    @Value("${typeevenement.changementetape.code}")
    private String etapeUpdatedCode;

    @Value("${typeevenement.changementetape.message}")
    private String etapeUpdatedMessage;

    @Override
    @Transactional
    public Operation createOperation(Operation operation) {

        // Ajout des valeurs par défaut
        setOperationDefaultValue(operation);

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanCreateOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création de l'operation " + operation.getNom());
        }

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);

        EtapeOperationEntity etapeOperation = etapeOperationDao.findById(operation.getEtape().getId()).orElseThrow(() -> new NoSuchElementException("Aucune étape d'opération id=" + operation.getId() + " n'a été trouvée pour les opérations avec diffusion restreinte"));

        //Vérification des autorisation sur l'étape
        if (etapeOperation.getCode().equals(Etape.ModeEnum.OFF.toString()) && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn("L'utilisateur n'ayant pas au moins le rôle référent ne peut pas créer une opération avec une etape en diffusion restreinte");
            //TODO : throw new AppServiceException()

        } else {
            operationEntity.setDiffusionRestreinte(etapeOperation.getCode().equals(Etape.ModeEnum.OFF.toString()));
            operationEntity.setEtapeOperation(etapeOperation);
        }


        // ajout d'un événement système de changement d'état
        operationEntity.addEvenementOperation(buildEvenementOperationEtapeUpdated(etapeOperation.getLibelle()));

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

        // Récupération de la prochaine étape
        EtapeOperationEntity etapeOperationEntity = etapeOperationDao.findOneById(operation.getEtape().getId());

        // Mise à jour de la diffusion restreinte à partir de l'étape
        operation.setDiffusionRestreinte(null);
        if (etapeOperationEntity.isRemoveRestriction()) {
            operation.setDiffusionRestreinte(false);
        }

        // Permet de savoir s'il y a changement d'étape
        boolean etapeChanged = operationEntity.getEtapeOperation().getId() != etapeOperationEntity.getId();

        operationMapper.dtoToEntity(operation, operationEntity);

        // Ajout d'un événement système en cas de changement d'étape
        if (etapeChanged) {
            operationEntity.addEvenementOperation(buildEvenementOperationEtapeUpdated(etapeOperationEntity.getLibelle()));
        }

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
        return operationMapper.entitiesToDto(operationCustomDao.searchOperations(operationsCriteria, pageable), pageable);
    }


    @Override
    public Operation getOperationById(long operationId) {

        OperationEntity operationEntity = getOperationEntityById(operationId);

        return operationMapper.entityToDto(operationEntity);

    }

    @Override
    public List<Evenement> getEvenementsByOperationId(Long operationId) {

        OperationEntity operationEntity = getOperationEntityById(operationId);

        return evenementOperationMapper.entitiesToDto(List.copyOf(operationEntity.getEvenements()));
    }

    /**
     * Construction d'un évenement opération système
     *
     * @param code                 code du type d'événement
     * @param evenementDescription description de l'événement
     * @return evenement crée
     */
    private EvenementOperationEntity buildEvenementOperationSysteme(String code, String evenementDescription) {

        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findByCode(code);
        if (typeEvenementEntity == null) {
            throw new NoSuchElementException("Le type d'événement de modification d'étape n'est pas défini.");
        }
        EvenementOperationEntity evenementOperationEntity = new EvenementOperationEntity();
        evenementOperationEntity.setEventDate(new Date());
        evenementOperationEntity.setTypeEvenement(typeEvenementEntity);
        evenementOperationEntity.setDescription(evenementDescription);
        evenementOperationEntity.setSysteme(true);

        return evenementOperationEntity;

    }

    /**
     * Construction d'un évenement opération système après changement d'étape
     *
     * @param libelleEtape libelle de l'étape
     * @return evenement crée
     */
    private EvenementOperationEntity buildEvenementOperationEtapeUpdated(String libelleEtape) {
        return this.buildEvenementOperationSysteme(etapeUpdatedCode, formatEtapeUpdatedMessage(libelleEtape));
    }

    private String formatEtapeUpdatedMessage(String libelleEtape) {
        return MessageFormat.format(etapeUpdatedMessage, libelleEtape);
    }

    @Override
    @Transactional
    public Evenement addEvenementByOperationId(Long operationId, Evenement evenement) throws AppServiceException {
        // Operation
        OperationEntity operationEntity = operationDao.findOneById(operationId);
        Operation operation = operationMapper.entityToDto(operationEntity);
        if (!operationRightsHelper.checkCanUpdateOperation(operation, operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de créer un évènement pour l'opération id = " + operationId);
        }

        //type evenement
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getTypeEvenement().getId());
        if (typeEvenementEntity.isSysteme()) {
            throw new AccessDeniedException("Un utilisateur ne peut pas créer d'événement système");
        }

        EvenementOperationEntity evenementOperationEntity = evenementOperationMapper.dtoToEntity(evenement);
        evenementOperationEntity.setTypeEvenement(typeEvenementEntity);
        evenementOperationEntity.setSysteme(false);

        // Enregistrement en BDD
        evenementOperationEntity = evenementOperationDao.save(evenementOperationEntity);

        operationEntity.addEvenementOperation(evenementOperationEntity);

        // Enregistrement en BDD
        try {
            operationDao.save(operationEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter l'évènement opération , IdEvent = "
                    + evenement.getId(), e);
        }

        return evenementOperationMapper.entityToDto(evenementOperationEntity);
    }

    @Override
    @Transactional
    public Evenement updateEvenementByOperationId(long idOperation, Evenement evenement) throws AppServiceException {

        // Récupération de l'opération et recherche de l'évènement à modifier
        OperationEntity operationEntity = operationDao.findOneById(idOperation);
        Operation operation = operationMapper.entityToDto(operationEntity);

        Optional<EvenementOperationEntity> optionalEvenementOperationEntity = operationEntity.lookupEvenementById(evenement.getId());
        if (optionalEvenementOperationEntity.isEmpty()) {
            throw new AppServiceException("L'événement id = " + evenement.getId() + " n'existe pas pour l'opération id = " + operationEntity.getId());
        }
        EvenementOperationEntity evenementOperationEntity = optionalEvenementOperationEntity.get();

        if (!evenementOperationRightsHelper.checkCanUpdateEvenementOperation(operation, evenementOperationMapper.entityToDto(evenementOperationEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modifier l'évènement id = " + evenementOperationEntity.getId()
                    + " de l'opération id = " + idOperation);
        }

        // type evenement
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getTypeEvenement().getId());
        evenementOperationEntity.setTypeEvenement(typeEvenementEntity);

        evenementOperationMapper.dtoToEntity(evenement, evenementOperationEntity);

        // Mise à jour de l'évènement Operation en base de données
        try {
            evenementOperationEntity = evenementOperationDao.save(evenementOperationEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException(" Impossible de faire la mise à jour de l'évènement Operation, IdEvent = "
                    + evenement.getId(), e);
        }

        return evenementOperationMapper.entityToDto(evenementOperationEntity);
    }

    private OperationEntity getOperationEntityById(long operationId) {

        OperationEntity operationEntity = operationDao.findOneById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer l'opération id = " + operationId);
        }

        return operationEntity;
    }

    /**
     * Ajout des valeurs par défaut pour une opération.
     *
     * @param operation opération
     */
    private void setOperationDefaultValue(Operation operation) {

        if (operation.isSecteur() == null) {
            operation.setSecteur(false);
        }
    }

}

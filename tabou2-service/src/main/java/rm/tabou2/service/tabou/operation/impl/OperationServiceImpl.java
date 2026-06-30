package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocumentRoot;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.dto.*;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceNotFoundException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.service.helper.logement.LogementSpecifiqueHelper;
import rm.tabou2.service.helper.operation.*;
import rm.tabou2.service.helper.plh.PLHSynchronizationHelper;
import rm.tabou2.service.helper.plh.TypePlhHelper;
import rm.tabou2.service.mapper.tabou.document.DocumentMapper;
import rm.tabou2.service.mapper.tabou.operation.DescriptionConcertationMapper;
import rm.tabou2.service.mapper.tabou.operation.EvenementOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.service.mapper.tabou.operation.PlhMapper;
import rm.tabou2.service.mapper.tabou.plh.TypePLHMapper;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.operation.*;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.logement.PorteeAccessionLogement;
import rm.tabou2.storage.tabou.entity.operation.*;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OperationServiceImpl implements OperationService {

    public static final String ERROR_RETRIEVE_METADATA_DOCUMENT = "Impossible de récupérer les métadonnées du document ";
    public static final String ERROR_RETRIEVE_DOCUMENT_CONTENT = "Impossible de télécharger le contenu du document ";
    public static final String ERROR_DELETE_DOCUMENT = "Impossible de supprimer le document ";
    public static final String ERROR_DROITS_DE_RECUPERATION = "L'utilisateur n'a pas les droits de récupérer l'opération id = ";
    public static final String USER_OPERATION_NOT_ALLOWED = "L'utilisateur n'a pas les droits de modifier l'opération ";
    public static final String ERROR_NON_TROUVEE = " n'a été trouvée";
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationDao operationDao;

    private final OperationCustomDao operationCustomDao;

    private final EtapeOperationDao etapeOperationDao;

    private final EvenementOperationDao evenementOperationDao;

    private final TypeEvenementDao typeEvenementDao;

    private final NatureDao natureDao;

    private final VocationDao vocationDao;

    private final VocationZADao vocationZADao;

    private final DecisionDao decisionDao;

    private final MaitriseOuvrageDao maitriseOuvrageDao;

    private final ModeAmenagementDao modeAmenagementDao;

    private final ConsommationEspaceDao consommationEspaceDao;

    private final TypeOccupationDao typeOccupationDao;

    private final PlhDao plhDao;

    private final DescriptionConcertationDao concertationDao;

    private final EntiteReferenteDao entiteReferenteDao;

    private final OutilFoncierDao outilFoncierDao;

    private final OutilAmenagementDao outilAmenagementDao;

    private final OperationEmpriseHelper operationEmpriseHelper;

    private final AuthentificationHelper authentificationHelper;

    private final OperationRightsHelper operationRightsHelper;

    private final OperationValidator operationValidator;

    private final OperationUpdateHelper operationUpdateHelper;

    private final EvenementOperationRightsHelper evenementOperationRightsHelper;

    private final OperationMapper operationMapper;

    private final EvenementOperationMapper evenementOperationMapper;

    private final DocumentMapper documentMapper;

    private final PlhMapper plhMapper;

    private final DescriptionConcertationMapper concertationMapper;

    private final AlfrescoService alfrescoService;

    private final DocumentGenerator documentGenerator;

    private final OperationFicheHelper operationFicheHelper;

    private final MosCustomDao mosCustomDao;

    private final DateHelper dateHelper;

    private final TypePLHMapper typePLHMapper;

    private final TypePlhHelper typePLHHelper;

    private final PLHSynchronizationHelper plhSyncHelper;
    private final LogementSpecifiqueHelper logementSpecifiqueHelper;
    private final TypePLHDao typePLHDao;

    @Value("${typeevenement.changementetape.code}")
    private String etapeUpdatedCode;

    @Value("${typeevenement.changementetape.message}")
    private String etapeUpdatedMessage;

    @Override
    @Transactional(rollbackFor = {AppServiceException.class})
    public OperationIntermediaire createOperation(OperationIntermediaire operation) throws AppServiceException {

        // Ajout des valeurs par défaut
        setOperationDefaultValue(operation);

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanCreateOperation(operation)) {
            throw new AccessDeniedException(
                    "L'utilisateur n'a pas les droits de création de l'operation " + operation.getNom());
        }

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);
        assignMultivaluables(operation, operationEntity);

        EtapeOperationEntity etape = operationEntity.getEtapeOperation();

        // Vérification des autorisation sur l'étape
        if (etape.getCode().equals(Etape.ModeEnum.OFF.toString()) && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn(
                    "L'utilisateur n'ayant pas au moins le rôle référent ne peut pas créer une opération avec une etape en diffusion restreinte");
            // TODO : throw new AppServiceException()

        } else {
            operationEntity.setDiffusionRestreinte(etape.getCode().equals(Etape.ModeEnum.OFF.toString()));
        }

        // ajout d'un événement système de changement d'état
        operationEntity.addEvenementOperation(buildEvenementOperationEtapeUpdated(etape.getLibelle()));
        if (operation.getPlh() != null) {
            operationEntity.setPlh(plhMapper.dtoToEntity(operation.getPlh()));
            plhDao.save(operationEntity.getPlh());
        }

        if (operation.getConcertation() != null) {
            operationEntity.setConcertation(concertationMapper.dtoToEntity(operation.getConcertation()));
            concertationDao.save(operationEntity.getConcertation());
        }

        updateListsOperation(operation, operationEntity);

        // Initialisation des logements spécifiques avec les types actifs si non fournis
        if (operationEntity.getLogementsSpecifiques().isEmpty()) {
            logementSpecifiqueHelper.initializeLogementsSpecifiques(
                    PorteeAccessionLogement.OPERATION,
                    operationEntity.getLogementsSpecifiques()
            );
        }

        operationDao.save(operationEntity);
        OperationIntermediaire operationSaved = operationMapper.entityToDto(operationEntity);

        operationEmpriseHelper.saveEmprise(operationSaved, operation.getIdEmprise());

        return operationSaved;

    }

    @Override
    @Transactional(rollbackFor = {AppServiceException.class})
    public OperationIntermediaire updateOperation(OperationIntermediaire operation) throws AppServiceException {

        OperationEntity operationEntity = operationDao.findOneById(operation.getId());

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanUpdateOperation(operation, operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException(
                    "L'utilisateur n'a pas les droits de modification de l'operation " + operation.getNom());
        }

        // Vérification contraintes métiers
        operationValidator.validateUpdateOperation(operation, operationMapper.entityToDto(operationEntity));

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
        assignMultivaluables(operation, operationEntity);

        if (operation.getPlh() != null) {
            operationEntity.setPlh(plhMapper.dtoToEntity(operation.getPlh()));
            plhDao.save(operationEntity.getPlh());
        } else if (operationEntity.getPlh() != null) {
            plhDao.delete(operationEntity.getPlh());
            operationEntity.setPlh(null);
        }

        if (operation.getConcertation() != null) {
            operationEntity.setConcertation(concertationMapper.dtoToEntity(operation.getConcertation()));
            concertationDao.save(operationEntity.getConcertation());
        } else if (operationEntity.getConcertation() != null) {
            concertationDao.delete(operationEntity.getConcertation());
            operationEntity.setConcertation(null);
        }

        updateListsOperation(operation, operationEntity);

        if (etapeOperationEntity.isRemoveRestriction()) {
            operation.setDiffusionRestreinte(false);
        }

        // Ajout d'un événement système en cas de changement d'étape
        if (etapeChanged) {
            operationEntity
                    .addEvenementOperation(buildEvenementOperationEtapeUpdated(etapeOperationEntity.getLibelle()));
        }

        operationEntity = operationDao.save(operationEntity);

        return operationMapper.entityToDto(operationEntity);

    }

    private void updateListsOperation(OperationIntermediaire operation, OperationEntity operationEntity)
            throws AppServiceException {
        if (operation.getActeurs() != null) {
            operationUpdateHelper.updateActeurs(operation, operationEntity);
        }
        if (operation.getActions() != null) {
            operationUpdateHelper.updateActions(operation, operationEntity);
        }
        if (operation.getAmenageurs() != null) {
            operationUpdateHelper.updateAmenageurs(operation, operationEntity);
        }
        if (operation.getContributions() != null) {
            operationUpdateHelper.updateContributions(operation, operationEntity);
        }
        if (operation.getDescriptionsFoncier() != null) {
            operationUpdateHelper.updateDescriptionsFonciers(operation, operationEntity);
        }
        if (operation.getFinancements() != null) {
            operationUpdateHelper.updateFinancements(operation, operationEntity);
        }
        if (operation.getInformationsProgrammation() != null) {
            operationUpdateHelper.updateInformationsProgrammation(operation, operationEntity);
        }
        if (operation.getLogementsSpecifiques() != null) {
            operationUpdateHelper.updateLogementsSpecifiques(operation, operationEntity);
        }
    }

    @Override
    @Transactional
    public OperationIntermediaire updateEtapeOfOperationId(long operationId, long etapeId) throws AppServiceException {
        EtapeOperationEntity etapeOperationEntity = etapeOperationDao.findOneById(etapeId);
        if (etapeOperationEntity == null) {
            throw new AppServiceNotFoundException(EtapeOperationEntity.class);
        }

        OperationEntity operationEntity = operationDao.findOneById(operationId);
        if (operationEntity == null) {
            throw new AppServiceNotFoundException(OperationEntity.class);
        }

        if (CollectionUtils.isEmpty(operationEntity.getEtapeOperation().getNextEtapes()) || operationEntity
                .getEtapeOperation().getNextEtapes().stream().noneMatch(opEtape -> opEtape.getId() == etapeId)) {
            throw new AppServiceException("L'étape n'est pas compatible avec l'étape actuelle de l'opération");
        }

        operationEntity.setEtapeOperation(etapeOperationEntity);
        return operationMapper.entityToDto(operationDao.save(operationEntity));
    }

    @SuppressWarnings("java:S3776") // On veut éviter de disperser le code, on accepte la complexité théorique
    private void assignMultivaluables(OperationIntermediaire operation, OperationEntity operationEntity)
            throws AppServiceException {
        if (BooleanUtils.isTrue(operation.getSecteur()) && operation.getParentId() != null) {
            OperationEntity parent = operationDao.findOneById(operation.getParentId());
            if (BooleanUtils.isTrue(parent.getSecteur())) {
                throw new AppServiceException("Un secteur ne peut pas être parent d'un autre secteur");
            }
            operationEntity.setParent(parent);
        }

        if (operation.getNature() != null && operation.getNature().getId() != null) {
            NatureEntity nature = natureDao.findById(operation.getNature().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucune nature id = " + operation.getNature().getId() + ERROR_NON_TROUVEE));
            operationEntity.setNature(nature);
        } else if (operation.getNature() == null) {
            operationEntity.setNature(null);
        }

        if (operation.getEtape() != null && operation.getEtape().getId() != null) {
            EtapeOperationEntity etapeOperation = etapeOperationDao.findById(operation.getEtape().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucune étape d'opération id=" + operation.getId() + ERROR_NON_TROUVEE));
            operationEntity.setEtapeOperation(etapeOperation);
        } else if (operation.getEtape() == null) {
            operationEntity.setEtapeOperation(null);
        }

        if (operation.getVocation() != null && operation.getVocation().getId() != null) {
            VocationEntity vocation = vocationDao.findById(operation.getVocation().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucune vocation id = " + operation.getVocation().getId() + ERROR_NON_TROUVEE));
            operationEntity.setVocation(vocation);
        } else {
            throw new AppServiceException("Une opération doit obligatoirement avoir une vocation");
        }

        if (operation.getVocationZa() != null && operation.getVocationZa().getId() != null) {
            VocationZAEntity vocationZA = vocationZADao.findById(operation.getVocationZa().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucune vocation id = " + operation.getVocation().getId() + ERROR_NON_TROUVEE));
            operationEntity.setVocationZa(vocationZA);
        } else if (operation.getVocationZa() == null) {
            operationEntity.setVocationZa(null);
        }

        if (operation.getDecision() != null && operation.getDecision().getId() != null) {
            DecisionEntity decision = decisionDao.findById(operation.getDecision().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucune décision id = " + operation.getDecision().getId() + ERROR_NON_TROUVEE));
            operationEntity.setDecision(decision);
        } else if (operation.getDecision() == null) {
            operationEntity.setDecision(null);
        }

        if (operation.getMaitriseOuvrage() != null && operation.getMaitriseOuvrage().getId() != null) {
            MaitriseOuvrageEntity maitriseOuvrage = maitriseOuvrageDao.findById(operation.getMaitriseOuvrage().getId())
                    .orElseThrow(() -> new NoSuchElementException("Aucune maitrîse d'ouvrage id = "
                            + operation.getMaitriseOuvrage().getId() + ERROR_NON_TROUVEE));
            operationEntity.setMaitriseOuvrage(maitriseOuvrage);
        } else if (operation.getMaitriseOuvrage() == null) {
            operationEntity.setMaitriseOuvrage(null);
        }

        if (operation.getModeAmenagement() != null && operation.getModeAmenagement().getId() != null) {
            ModeAmenagementEntity modeAmenagement = modeAmenagementDao.findById(operation.getModeAmenagement().getId())
                    .orElseThrow(() -> new NoSuchElementException("Aucun mode d'aménagement id = "
                            + operation.getModeAmenagement().getId() + " n'a été trouvé"));
            operationEntity.setModeAmenagement(modeAmenagement);
        } else if (operation.getModeAmenagement() == null) {
            operationEntity.setModeAmenagement(null);
        }

        if (operation.getTypeOccupation() != null && operation.getTypeOccupation().getId() != null) {
            TypeOccupationEntity typeOccupation = typeOccupationDao.findById(operation.getTypeOccupation().getId())
                    .orElseThrow(() -> new NoSuchElementException("Aucun type d'occupation id = "
                            + operation.getTypeOccupation().getId() + " n'a été trouvé"));
            operationEntity.setTypeOccupation(typeOccupation);
        } else if (operation.getTypeOccupation() == null) {
            operationEntity.setTypeOccupation(null);
        }

        if (operation.getConsommationEspace() != null && operation.getConsommationEspace().getId() != null) {
            ConsommationEspaceEntity consommationEspace = consommationEspaceDao
                    .findById(operation.getConsommationEspace().getId())
                    .orElseThrow(() -> new NoSuchElementException("Aucune consommation d'espace id = "
                            + operation.getConsommationEspace().getId() + ERROR_NON_TROUVEE));
            operationEntity.setConsommationEspace(consommationEspace);
        } else if (operation.getConsommationEspace() == null) {
            operationEntity.setConsommationEspace(null);
        }

        if (operation.getOutilFoncier() != null && operation.getOutilFoncier().getId() != null) {
            OutilFoncierEntity outilFoncier = outilFoncierDao.findById(operation.getOutilFoncier().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucun outil foncier id = " + operation.getOutilFoncier().getId()));
            operationEntity.setOutilFoncier(outilFoncier);
        } else if (operation.getOutilFoncier() == null) {
            operationEntity.setOutilFoncier(null);
        }

        if (operation.getOutilAmenagement() != null && operation.getOutilAmenagement().getId() != null) {
            OutilAmenagementEntity outilAmenagement = outilAmenagementDao
                    .findById(operation.getOutilAmenagement().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucun outil aménagement id = " + operation.getOutilAmenagement().getId()));
            operationEntity.setOutilAmenagement(outilAmenagement);
        } else if (operation.getOutilAmenagement() == null) {
            operationEntity.setOutilAmenagement(null);
        }

        if (operation.getEntiteReferente() != null && operation.getEntiteReferente().getId() != null) {
            EntiteReferenteEntity entiteReferente = entiteReferenteDao.findById(operation.getEntiteReferente().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Aucune entité référente id = " + operation.getEntiteReferente().getId()));
            operationEntity.setEntiteReferente(entiteReferente);
        } else if (operation.getEntiteReferente() == null) {
            operationEntity.setEntiteReferente(null);
        }

    }

    @Override
    public Page<OperationIntermediaire> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable) {
        // Si l'utilisateur n'a pas le droit de voir les opérations en diffusion
        // restreinte, on filtre sur false
        if (BooleanUtils.isTrue(operationsCriteria.getDiffusionRestreinte())
                && !authentificationHelper.hasRestreintAccess()) {
            operationsCriteria.setDiffusionRestreinte(false);
            LOGGER.warn("Accès non autorisé à des opérations d'accès restreint");
        }
        return operationMapper.entitiesToDto(operationCustomDao.searchOperations(operationsCriteria, pageable),
                pageable);
    }

    @Override
    public DocumentContent generateFicheSuivi(Long operationId) throws AppServiceException {
        OperationEntity operationEntity = getOperationEntityById(operationId);

        if (Boolean.TRUE.equals(operationEntity.getSecteur())) {
            throw new AppServiceException(
                    "Erreur de génération de la fiche de suivi Operation : l'opération est un secteur.");
        }
        GenerationModel generationModel = operationFicheHelper.buildGenerationModel(operationEntity);

        DocumentContent documentContent = documentGenerator.generateDocument(generationModel);
        documentContent.setFileName(buildRapportFileName(operationEntity));
        return documentContent;
    }

    @Override
    public OperationIntermediaire getOperationById(long operationId) {
        OperationEntity operationEntity = getOperationEntityById(operationId);
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntity);
        operation.setMos(mosCustomDao.computeOperationMos(operationEntity.getId(),
                Boolean.TRUE.equals(operationEntity.getSecteur())));
        return operation;
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
        evenementOperationEntity.setEventDate(dateHelper.now());
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
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntity);
        if (!operationRightsHelper.checkCanUpdateOperation(operation, operation)) {
            throw new AccessDeniedException(
                    "L'utilisateur n'a pas les droits de créer un évènement pour l'opération id = " + operationId);
        }

        // type evenement
        if (evenement.getTypeEvenement() == null || evenement.getTypeEvenement().getId() == null) {
            throw new AppServiceException("Pas de type évenment fourni");
        }
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
            throw new AppServiceException("Impossible d'ajouter l'évènement opération , IdEvent = " + evenement.getId(),
                    e);
        }

        return evenementOperationMapper.entityToDto(evenementOperationEntity);
    }

    @Override
    @Transactional
    public Evenement updateEvenementByOperationId(long idOperation, Evenement evenement) throws AppServiceException {

        // Récupération de l'opération et recherche de l'évènement à modifier
        OperationEntity operationEntity = operationDao.findOneById(idOperation);
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntity);

        Optional<EvenementOperationEntity> optionalEvenementOperationEntity = operationEntity
                .lookupEvenementById(evenement.getId());
        if (optionalEvenementOperationEntity.isEmpty()) {
            throw new AppServiceException("L'événement id = " + evenement.getId()
                    + " n'existe pas pour l'opération id = " + operationEntity.getId());
        }
        EvenementOperationEntity evenementOperationEntity = optionalEvenementOperationEntity.get();

        if (!evenementOperationRightsHelper.checkCanUpdateEvenementOperation(operation,
                evenementOperationMapper.entityToDto(evenementOperationEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modifier l'évènement id = "
                    + evenementOperationEntity.getId() + " de l'opération id = " + idOperation);
        }

        // type evenement
        if (evenement.getTypeEvenement() == null || evenement.getTypeEvenement().getId() == null) {
            throw new AppServiceException("Le type d'événement est obligatoire");
        }
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getTypeEvenement().getId());
        evenementOperationEntity.setTypeEvenement(typeEvenementEntity);

        evenementOperationMapper.dtoToEntity(evenement, evenementOperationEntity);

        // Mise à jour de l'évènement Operation en base de données
        try {
            evenementOperationEntity = evenementOperationDao.save(evenementOperationEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException(
                    " Impossible de faire la mise à jour de l'évènement Operation, IdEvent = " + evenement.getId(), e);
        }

        return evenementOperationMapper.entityToDto(evenementOperationEntity);
    }

    @Override
    public DocumentMetadata getDocumentMetadata(long operationId, String documentId) throws AppServiceException {

        // On vérifie que l'opération existe et que l'utilisateur a bien les droits de
        // consultation dessus
        OperationEntity operation = getOperationEntityById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operation))) {
            throw new AccessDeniedException(
                    "L'utilisateur n'a pas les droits de consulter l'opération id = " + operationId);
        }

        try {
            // Récupération du document Dans alfresco
            return documentMapper.entityToDto(alfrescoService.getDocumentMetadata(documentId));

        } catch (NotFound e) {
            throw new NoSuchElementException(ERROR_RETRIEVE_METADATA_DOCUMENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_RETRIEVE_METADATA_DOCUMENT + documentId, e);
        }

    }

    @Override
    public DocumentMetadata updateDocumentMetadata(long operationId, String documentId,
                                                   DocumentMetadata documentMetadata) {

        // On vérifie que l'opération existe et que l'utilisateur a bien les droits de
        // consultation dessus
        OperationEntity operation = getOperationEntityById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operation))) {
            throw new AccessDeniedException(
                    "L'utilisateur n'a pas les droits de consulter l'opération id = " + operationId);
        }

        return documentMapper.entityToDto(alfrescoService.updateDocumentMetadata(AlfrescoTabouType.OPERATION,
                operationId, documentId, documentMetadata, false));

    }

    @Override
    public DocumentContent downloadDocument(long operationId, String documentId) throws AppServiceException {

        // On vérifie que l'opération existe et que l'utilisateur a bien les droits de
        // suppression dessus
        OperationIntermediaire operation = getOperationById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operation)) {
            throw new AccessDeniedException(ERROR_DROITS_DE_RECUPERATION + operationId);
        }

        try {
            return alfrescoService.downloadDocument(AlfrescoTabouType.OPERATION, operationId, documentId);

        } catch (WebClientResponseException.NotFound e) {
            throw new NoSuchElementException(ERROR_RETRIEVE_DOCUMENT_CONTENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_RETRIEVE_DOCUMENT_CONTENT + documentId, e);
        }

    }

    @Override
    public void updateDocumentContent(long operationId, String documentId, MultipartFile file)
            throws AppServiceException {

        // On vérifie que l'opération existe et que l'utilisateur a bien les droits de
        // suppression dessus
        OperationIntermediaire operation = getOperationById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operation)) {
            throw new AccessDeniedException(ERROR_DROITS_DE_RECUPERATION + operationId);
        }

        alfrescoService.updateDocumentContent(AlfrescoTabouType.OPERATION, operationId, documentId, file);

    }

    @Override
    public DocumentMetadata addDocument(long operationId, String nom, String libelleTypeDocument,
                                        LocalDateTime dateDocument, Object file) throws AppServiceException {

        // On vérifie que l'opération existe et que l'utilisateur a bien les droits
        // d'ajout sur le document
        OperationIntermediaire operation = getOperationById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operation)) {
            throw new AccessDeniedException(ERROR_DROITS_DE_RECUPERATION + operationId);
        }

        // Récupération du document Dans alfresco
        return documentMapper.entityToDto(alfrescoService.addDocument(nom, libelleTypeDocument,
                AlfrescoTabouType.OPERATION, operationId, dateDocument, (MultipartFile) file));

    }

    @Override
    public Page<DocumentMetadata> searchDocuments(long operationId, String nom, String libelleTypeDocument,
                                                  String typeMime, Pageable pageable) {

        OperationEntity operationEntity = operationDao.findOneById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException(ERROR_DROITS_DE_RECUPERATION + operationId);
        }

        AlfrescoDocumentRoot documentRoot = alfrescoService.searchDocuments(AlfrescoTabouType.OPERATION, operationId,
                nom, libelleTypeDocument, typeMime, pageable);

        List<DocumentMetadata> results = documentMapper
                .entitiesToDto(new ArrayList<>(documentRoot.getList().getEntries()));

        return new PageImpl<>(results, pageable, documentRoot.getList().getPagination().getTotalItems());
    }

    @Override
    public void deleteDocument(long operationId, String documentId) throws AppServiceException {

        // On vérifie que l'opération existe
        OperationIntermediaire operationToDelete = getOperationById(operationId);

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanUpdateOperation(operationToDelete, operationToDelete)) {
            throw new AccessDeniedException(ERROR_DROITS_DE_RECUPERATION + operationToDelete.getNom());
        }

        try {
            // Suppression du document Dans alfresco
            alfrescoService.deleteDocument(AlfrescoTabouType.OPERATION, operationId, documentId);

        } catch (WebClientResponseException.NotFound e) {
            throw new NoSuchElementException(ERROR_DELETE_DOCUMENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_DELETE_DOCUMENT + documentId, e);
        }

    }

    @Override
    @Transactional(rollbackFor = {AppServiceException.class})
    public TypePLH updatePLHOperation(long operationId, long typePLHId, TypePLH typePLH) throws AppServiceException {

        // Récupération de l'opération
        OperationEntity operationEntity = getOperationEntityById(operationId);

        // Recuperation du lien type PLH <-> operation. S'il n'existe pas encore, on
        // rattache le type PLH a l'operation (cas de l'ajout d'un PLH a l'operation).
        TypePLHEntity typePLHEntity = operationEntity.lookupTypePLHById(typePLHId).orElse(null);
        if (typePLHEntity == null) {
            typePLHEntity = typePLHHelper.getTypePLHEntity(typePLHId);
            operationEntity.addTypePLHOperation(typePLHEntity);
        }

        if (typePLH.getId() == null) {
            typePLH.setId(typePLHId);
        }

        // Mise à jour récursive des valeurs PLH
        typePLHHelper.updateValuesTypePLH(typePLH, operationEntity);

        // Report des valeurs PLH vers les champs de l'opération (syncField)
        plhSyncHelper.synchronizePLHToOperation(typePLHEntity, operationEntity);

        operationDao.save(operationEntity);

        // Retour du TypePLH peuplé avec ses nouvelles valeurs
        TypePLH typePLHMisAJour = typePLHMapper.entityToDto(typePLHEntity);
        return typePLHHelper.populateTypePLH(typePLHMisAJour, operationEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TypePLH getPLHOperation(long operationId, long typePLHId) throws AppServiceException {

        // Récupération de l'opération
        OperationEntity operationEntity = getOperationEntityById(operationId);

        if (CollectionUtils.isEmpty(operationEntity.getPlhs())) {
            throw new AppServiceException("Impossible de récupérer le TypePLH id = " + typePLHId
                    + " Aucun TypePLH n'est rattaché à l'opération id = " + operationId);
        }

        // Récupération du TypePLH et peuplement avec ses valeurs
        TypePLHEntity typePLHEntity = lookupTypePLHById(operationEntity, typePLHId);
        TypePLH typePLH = typePLHMapper.entityToDto(typePLHEntity);
        return typePLHHelper.populateTypePLH(typePLH, operationEntity);
    }

    @Override
    @Transactional(rollbackFor = {AppServiceException.class})
    public TypePLH addPLHOperationById(long operationId, long typePLHId) throws AppServiceException {

        // Récupération de l'opération
        OperationEntity operationEntity = operationDao.findOneById(operationId);
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntity);

        // Vérification des droits
        if (!operationRightsHelper.checkCanUpdateOperation(operation, operation)) {
            throw new AccessDeniedException(USER_OPERATION_NOT_ALLOWED + operation.getNom());
        }

        // Récupération du type PLH
        TypePLHEntity typePLHEntity = typePLHDao.findOneById(typePLHId);

        // Vérification que le type PLH n'est pas déjà associé
        if (operationEntity.getPlhs() != null
                && operationEntity.getPlhs().stream().anyMatch(candidate -> candidate.getId() == typePLHId)) {
            throw new AppServiceException("Impossible d'ajouter le typePLH à l'opération, il est déjà présent");
        }

        if (!typePLHEntity.isSelectionnable()) {
            throw new AppServiceException("Le typePLH n'est pas sélectionnable");
        }

        // Association du type PLH à l'opération
        operationEntity.addTypePLHOperation(typePLHEntity);
        operationDao.save(operationEntity);

        TypePLH typePLH = typePLHMapper.entityToDto(typePLHEntity);
        return typePLHHelper.populateTypePLH(typePLH, operationEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void removePLHOperationById(long operationId, long typePLHId) throws AppServiceException {
        // Récupération de l'opération et recherche du type PLH à supprimer
        OperationEntity operationEntity = operationDao.findOneById(operationId);
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntity);
        TypePLHEntity typePLHEntity = lookupTypePLHById(operationEntity, typePLHId);

        // Vérification si l'utilisateur a le droit de modifier une opération
        if (!operationRightsHelper.checkCanUpdateOperation(operation, operation)) {
            throw new AccessDeniedException(USER_OPERATION_NOT_ALLOWED + operation.getNom());
        }
        // Suppression du lien type PLH (les attributs PLH sont conservés
        // intentionnellement)
        operationEntity.getPlhs().remove(typePLHEntity);

        operationDao.save(operationEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypePLHBean> getPLHsOperation(long operationId) {

        OperationEntity operationEntity = getOperationEntityById(operationId);

        if (CollectionUtils.isEmpty(operationEntity.getPlhs())) {
            return List.of();
        }

        return operationEntity.getPlhs().stream()
                .map(entity -> {
                    TypePLHBean bean = new TypePLHBean();
                    bean.setId(entity.getId());
                    bean.setLibelle(entity.getLibelle());
                    return bean;
                })
                .toList();
    }

    private TypePLHEntity lookupTypePLHById(OperationEntity operationEntity, long typePLHId)
            throws AppServiceException {
        Optional<TypePLHEntity> optionalTypePLHEntity = operationEntity.lookupTypePLHById(typePLHId);
        if (optionalTypePLHEntity.isEmpty()) {
            throw new AppServiceException("Le type PLH id = " + typePLHId
                    + " n'existe pas pour l'opération id = " + operationEntity.getId());
        }
        return optionalTypePLHEntity.get();
    }

    private OperationEntity getOperationEntityById(long operationId) {

        OperationEntity operationEntity = operationDao.findOneById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException(ERROR_DROITS_DE_RECUPERATION + operationId);
        }

        return operationEntity;
    }

    /**
     * Ajout des valeurs par défaut pour une opération.
     *
     * @param operation opération
     */
    private void setOperationDefaultValue(OperationIntermediaire operation) {

        if (operation.getSecteur() == null) {
            operation.setSecteur(false);
        }
    }

    private String buildRapportFileName(OperationEntity operationEntity) {
        StringBuilder fileName = new StringBuilder("FicheSuivi_");
        fileName.append(operationEntity.getId());
        fileName.append("_");
        fileName.append(operationEntity.getCode());
        fileName.append("_");
        fileName.append(operationEntity.getNom());
        fileName.append("_");
        fileName.append(System.nanoTime());

        return fileName.toString();
    }

}

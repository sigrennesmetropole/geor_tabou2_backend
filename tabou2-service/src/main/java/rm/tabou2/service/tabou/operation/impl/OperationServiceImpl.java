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
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.EvenementOperationRightsHelper;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.service.helper.operation.OperationFicheHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.helper.operation.OperationUpdateHelper;
import rm.tabou2.service.helper.operation.OperationValidator;
import rm.tabou2.service.mapper.tabou.document.DocumentMapper;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.EvenementOperationMapper;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.evenement.EvenementOperationCustomDao;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.operation.*;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.operation.*;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.text.MessageFormat;
import java.util.*;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
@Validated
@Transactional(readOnly = true)
public class OperationServiceImpl implements OperationService {

    public static final String ERROR_RETRIEVE_METADATA_DOCUMENT = "Impossible de récupérer les métadonnées du document ";
    public static final String ERROR_RETRIEVE_DOCUMENT_CONTENT = "Impossible de télécharger le contenu du document ";
    public static final String ERROR_DELETE_DOCUMENT = "Impossible de supprimer le document ";
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
    private EvenementOperationCustomDao evenementOperationCustomDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private NatureDao natureDao;

    @Autowired
    private VocationDao vocationDao;

    @Autowired
    private VocationZADao vocationZADao;

    @Autowired
    private DecisionDao decisionDao;

    @Autowired
    private MaitriseOuvrageDao maitriseOuvrageDao;

    @Autowired
    private ModeAmenagementDao modeAmenagementDao;

    @Autowired
    private ConsommationEspaceDao consommationEspaceDao;

    @Autowired
    private TypeOccupationDao typeOccupationDao;

    @Autowired
    private PlhDao plhDao;

    @Autowired
    private OperationEmpriseHelper operationEmpriseHelper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private OperationValidator operationValidator;

    @Autowired
    private OperationUpdateHelper operationUpdateHelper;

    @Autowired
    private EvenementOperationRightsHelper evenementOperationRightsHelper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private EtapeOperationMapper etapeOperationMapper;

    @Autowired
    private EvenementOperationMapper evenementOperationMapper;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private OperationService me;

    @Autowired
    private AlfrescoService alfrescoService;

    @Autowired
    private DocumentGenerator documentGenerator;

    @Autowired
    private OperationFicheHelper operationFicheHelper;

    @Value("${typeevenement.changementetape.code}")
    private String etapeUpdatedCode;

    @Value("${typeevenement.changementetape.message}")
    private String etapeUpdatedMessage;

    @Override
    @Transactional
    public OperationIntermediaire createOperation(OperationIntermediaire operation) {

        // Ajout des valeurs par défaut
        setOperationDefaultValue(operation);

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanCreateOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création de l'operation " + operation.getNom());
        }

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);
        assignMultivaluables(operation, operationEntity);

        EtapeOperationEntity etape = operationEntity.getEtapeOperation();

        //Vérification des autorisation sur l'étape
        if (etape.getCode().equals(Etape.ModeEnum.OFF.toString()) && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn("L'utilisateur n'ayant pas au moins le rôle référent ne peut pas créer une opération avec une etape en diffusion restreinte");
            //TODO : throw new AppServiceException()

        } else {
            operationEntity.setDiffusionRestreinte(etape.getCode().equals(Etape.ModeEnum.OFF.toString()));
        }

        // ajout d'un événement système de changement d'état
        operationEntity.addEvenementOperation(buildEvenementOperationEtapeUpdated(etape.getLibelle()));
        if(operationEntity.getPlh() != null){
            plhDao.save(operationEntity.getPlh());
        }
        operationDao.save(operationEntity);
        OperationIntermediaire operationSaved = operationMapper.entityToDto(operationEntity);

        operationEmpriseHelper.saveEmprise(operationSaved, operation.getIdEmprise());

        return operationSaved;

    }

    @Override
    @Transactional
    public OperationIntermediaire updateOperation(OperationIntermediaire operation) throws AppServiceException {

        OperationEntity operationEntity = operationDao.findOneById(operation.getId());

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanUpdateOperation(operation, operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification de l'operation " + operation.getNom());
        }

        //Vérification contraintes métiers
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

        if(operation.getActeurs() != null){
            operationUpdateHelper.updateActeurs(operation, operationEntity);
        }
        if(operation.getActions() != null){
            operationUpdateHelper.updateActions(operation, operationEntity);
        }
        if(operation.getAmenageurs() != null){
            operationUpdateHelper.updateAmenageurs(operation, operationEntity);
        }
        if(operation.getContributions() != null){
            operationUpdateHelper.updateContributions(operation, operationEntity);
        }
        if(operation.getDescriptionsFoncier() != null){
            operationUpdateHelper.updateDescriptionsFonciers(operation, operationEntity);
        }
        if(operation.getFinancements() != null){
            operationUpdateHelper.updateFinancements(operation, operationEntity);
        }
        if(operation.getInformationsProgrammation() != null){
            operationUpdateHelper.updateInformationsProgrammation(operation, operationEntity);
        }

        if (etapeOperationEntity.isRemoveRestriction()) {
            operation.setDiffusionRestreinte(false);
        }

        // Ajout d'un événement système en cas de changement d'étape
        if (etapeChanged) {
            operationEntity.addEvenementOperation(buildEvenementOperationEtapeUpdated(etapeOperationEntity.getLibelle()));
        }

        operationEntity = operationDao.save(operationEntity);

        return operationMapper.entityToDto(operationEntity);

    }

    @Override
    @Transactional
    public OperationIntermediaire updateEtapeOfOperationId(long operationId, long etapeId) throws AppServiceException {
        EtapeOperationEntity etapeOperationEntity = etapeOperationDao.findOneById(etapeId);

        OperationIntermediaire operation = getOperationById(operationId);
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntity));
        return me.updateOperation(operation);
    }

    private void assignMultivaluables(OperationIntermediaire operation, OperationEntity operationEntity){
        if(operation.getNature() != null && operation.getNature().getId() != null){
            NatureEntity nature = natureDao.findById(operation.getNature().getId()).orElseThrow(() -> new NoSuchElementException("Aucune nature id = " + operation.getNature().getId() + " n'a été trouvée"));
            operationEntity.setNature(nature);
        }

        if(operation.getEtape() != null && operation.getEtape().getId() != null){
            EtapeOperationEntity etapeOperation = etapeOperationDao.findById(operation.getEtape().getId()).orElseThrow(() -> new NoSuchElementException("Aucune étape d'opération id=" + operation.getId() + " n'a été trouvée"));
            operationEntity.setEtapeOperation(etapeOperation);
        }

        if(operation.getVocation() != null && operation.getVocation().getId() != null){
            VocationEntity vocation = vocationDao.findById(operation.getVocation().getId()).orElseThrow(() -> new NoSuchElementException("Aucune vocation id = " + operation.getVocation().getId() + " n'a été trouvée"));
            operationEntity.setVocation(vocation);
        }

        if(operation.getVocationZa() != null && operation.getVocationZa().getId() != null){
            VocationZAEntity vocationZA = vocationZADao.findById(operation.getVocationZa().getId()).orElseThrow(() -> new NoSuchElementException("Aucune vocation id = " + operation.getVocation().getId() + " n'a été trouvée"));
            operationEntity.setVocationZa(vocationZA);
        }

        if(operation.getDecision() != null && operation.getDecision().getId() != null){
            DecisionEntity decision = decisionDao.findById(operation.getDecision().getId()).orElseThrow(() -> new NoSuchElementException("Aucune décision id = " + operation.getDecision().getId() + " n'a été trouvée"));
            operationEntity.setDecision(decision);
        }

        if(operation.getMaitriseOuvrage() != null && operation.getMaitriseOuvrage().getId() != null){
            MaitriseOuvrageEntity maitriseOuvrage = maitriseOuvrageDao.findById(operation.getMaitriseOuvrage().getId()).orElseThrow(() -> new NoSuchElementException("Aucune maitrîse d'ouvrage id = " + operation.getMaitriseOuvrage().getId() + " n'a été trouvée"));
            operationEntity.setMaitriseOuvrage(maitriseOuvrage);
        }

        if(operation.getModeAmenagement() != null && operation.getModeAmenagement().getId() != null){
            ModeAmenagementEntity modeAmenagement = modeAmenagementDao.findById(operation.getModeAmenagement().getId()).orElseThrow(() -> new NoSuchElementException("Aucun mode d'aménagement id = " + operation.getModeAmenagement().getId() + " n'a été trouvé"));
            operationEntity.setModeAmenagement(modeAmenagement);
        }

        if(operation.getTypeOccupation() != null && operation.getTypeOccupation().getId() != null){
            TypeOccupationEntity typeOccupation = typeOccupationDao.findById(operation.getTypeOccupation().getId()).orElseThrow(() -> new NoSuchElementException("Aucun type d'occupation id = " + operation.getTypeOccupation().getId() + " n'a été trouvé"));
            operationEntity.setTypeOccupation(typeOccupation);
        }

        if(operation.getConsommationEspace() != null && operation.getConsommationEspace().getId() != null){
            ConsommationEspaceEntity consommationEspace = consommationEspaceDao.findById(operation.getConsommationEspace().getId())
                    .orElseThrow(() -> new NoSuchElementException("Aucune consommation d'espace id = " + operation.getConsommationEspace().getId() + " n'a été trouvée"));
            operationEntity.setConsommationEspace(consommationEspace);
        }

    }

    @Override
    public Page<OperationIntermediaire> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable) {
        // Si l'utilisateur n'a pas le droit de voir les opérations en diffusion restreinte, on filtre sur false
        if (BooleanUtils.isTrue(operationsCriteria.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            operationsCriteria.setDiffusionRestreinte(false);
            LOGGER.warn("Accès non autorisé à des opérations d'accès restreint");
        }
        return operationMapper.entitiesToDto(operationCustomDao.searchOperations(operationsCriteria, pageable), pageable);
    }

    @Override
    public DocumentContent generateFicheSuivi(Long operationId) throws AppServiceException {
        OperationEntity operationEntity = getOperationEntityById(operationId);
        GenerationModel generationModel = operationFicheHelper.buildGenerationModel(operationEntity);

        DocumentContent documentContent = documentGenerator.generateDocument(generationModel);
        documentContent.setFileName(buildRapportFileName(operationEntity));
        return documentContent;
    }

    @Override
    public OperationIntermediaire getOperationById(long operationId) {

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
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntity);
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
        OperationIntermediaire operation = operationMapper.entityToDto(operationEntity);

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


    @Override
    public DocumentMetadata getDocumentMetadata(long operationId, String documentId) throws AppServiceException {

        //On vérifie que l'opération existe et que l'utilisateur a bien les droits de consultation dessus
        OperationEntity operation = getOperationEntityById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operation))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de consulter l'opération id = " + operationId);
        }

        try {
            //Récupération du document Dans alfresco
            return documentMapper.entityToDto(alfrescoService.getDocumentMetadata(documentId));

        } catch (NotFound e) {
            throw new NoSuchElementException(ERROR_RETRIEVE_METADATA_DOCUMENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_RETRIEVE_METADATA_DOCUMENT + documentId, e);
        }

    }

    @Override
    public DocumentMetadata updateDocumentMetadata(long operationId, String documentId, DocumentMetadata documentMetadata) throws AppServiceException {

        //On vérifie que l'opération existe et que l'utilisateur a bien les droits de consultation dessus
        OperationEntity operation = getOperationEntityById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operation))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de consulter l'opération id = " + operationId);
        }

        return documentMapper.entityToDto(alfrescoService.updateDocumentMetadata(AlfrescoTabouType.OPERATION, operationId, documentId, documentMetadata, false));

    }


    @Override
    public DocumentContent downloadDocument(long operationId, String documentId) throws AppServiceException {

        //On vérifie que l'opération existe et que l'utilisateur a bien les droits de suppression dessus
        OperationIntermediaire operation = getOperationById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer l'opération id = " + operationId);
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
    public void updateDocumentContent(long operationId, String documentId, MultipartFile file) throws AppServiceException {

        //On vérifie que l'opération existe et que l'utilisateur a bien les droits de suppression dessus
        OperationIntermediaire operation = getOperationById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer l'opération id = " + operationId);
        }

        alfrescoService.updateDocumentContent(AlfrescoTabouType.OPERATION, operationId, documentId, file);

    }

    @Override
    public DocumentMetadata addDocument(long operationId, String nom, String libelleTypeDocument, MultipartFile file) throws AppServiceException {

        //On vérifie que l'opération existe et que l'utilisateur a bien les droits d'ajout sur le document
        OperationIntermediaire operation = getOperationById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operation)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer l'opération id = " + operationId);
        }

        //Récupération du document Dans alfresco
        return documentMapper.entityToDto(alfrescoService.addDocument(nom, libelleTypeDocument, AlfrescoTabouType.OPERATION, operationId, file));

    }


    @Override
    public Page<DocumentMetadata> searchDocuments(long operationId, String nom, String libelleTypeDocument, String typeMime, Pageable pageable) {

        OperationEntity operationEntity = operationDao.findOneById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer l'opération id = " + operationId);
        }

        AlfrescoDocumentRoot documentRoot = alfrescoService.searchDocuments(AlfrescoTabouType.OPERATION, operationId, nom, libelleTypeDocument, typeMime, pageable);

        List<DocumentMetadata> results = documentMapper.entitiesToDto(new ArrayList<>(documentRoot.getList().getEntries()));

        return new PageImpl<>(results, pageable, documentRoot.getList().getPagination().getTotalItems());
    }

    @Override
    public void deleteDocument(long operationId, String documentId) throws AppServiceException {

        //On vérifie que l'opération existe
        OperationIntermediaire operationToDelete = getOperationById(operationId);

        // Vérification des droits utilisateur
        if (!operationRightsHelper.checkCanUpdateOperation(operationToDelete, operationToDelete)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de suppression de l'opération " + operationToDelete.getNom());
        }

        try {
            //Suppression du document Dans alfresco
            alfrescoService.deleteDocument(AlfrescoTabouType.OPERATION, operationId, documentId);

        } catch (WebClientResponseException.NotFound e) {
            throw new NoSuchElementException(ERROR_DELETE_DOCUMENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_DELETE_DOCUMENT + documentId, e);
        }

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

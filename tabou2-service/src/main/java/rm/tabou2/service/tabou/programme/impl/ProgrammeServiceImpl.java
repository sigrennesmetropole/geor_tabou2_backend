package rm.tabou2.service.tabou.programme.impl;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocumentRoot;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.ProgrammeLight;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.EvenementProgrammeRigthsHelper;
import rm.tabou2.service.helper.programme.ProgrammePlannerHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.mapper.sig.ProgrammeRmMapper;
import rm.tabou2.service.mapper.tabou.document.DocumentMapper;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.service.mapper.tabou.programme.EvenementProgrammeMapper;
import rm.tabou2.service.mapper.tabou.programme.ProgrammeLightMapper;
import rm.tabou2.service.mapper.tabou.programme.ProgrammeMapper;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.FicheSuiviProgrammeDataModel;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.dao.ProgrammeRmCustomDao;
import rm.tabou2.storage.sig.dao.ProgrammeRmDao;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.EvenementProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersDao;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.item.AgapeoSuiviHabitat;
import rm.tabou2.storage.tabou.item.PermisConstruireSuiviHabitat;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
@Validated
@Transactional(readOnly = true)
public class ProgrammeServiceImpl implements ProgrammeService {

    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammeServiceImpl.class);

    //Message d'erreur
    public static final String ERROR_RETRIEVE_METADATA_DOCUMENT = "Impossible de récupérer les métadonnées du document ";
    public static final String ERROR_RETRIEVE_DOCUMENT_CONTENT = "Impossible de télécharger le contenu du document ";
    public static final String ERROR_DELETE_DOCUMENT = "Impossible de supprimer le document ";

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private ProgrammeCustomDao programmeCustomDao;

    @Autowired
    private ProgrammeTiersCustomDao programmeTiersCustomDao;

    @Autowired
    private ProgrammeRmCustomDao programmeRmCustomDao;

    @Autowired
    private ProgrammeRmDao programmeRmDao;

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private EvenementProgrammeDao evenementProgrammeDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private AgapeoDao agapeoDao;

    @Autowired
    private PermisConstruireDao permisConstruireDao;

    @Autowired
    private ProgrammeTiersDao programmeTiersDao;

    @Autowired
    private EvenementProgrammeMapper evenementProgrammeMapper;

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private ProgrammeLightMapper programmeLightMapper;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Autowired
    private ProgrammeRmMapper programmeRmMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    @Autowired
    private EvenementProgrammeRigthsHelper evenementProgrammeRigthsHelper;

    @Autowired
    private ProgrammePlannerHelper programmePlannerHelper;

    @Autowired
    private DocumentGenerator documentGenerator;

    @Autowired
    private ProgrammeService me;

    @Value("${typeevenement.changementetape.code}")
    private String etapeUpdatedCode;

    @Value("${typeevenement.changementetape.message}")
    private String etapeUpdatedMessage;

    @Autowired
    private AlfrescoService alfrescoService;

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    @Transactional
    public Programme createProgramme(Programme programme) {


        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanCreateProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création du programme " + programme.getNom());
        }


        ProgrammeEntity programmeEntity = programmeMapper.dtoToEntity(programme);

        EtapeProgrammeEntity etapProgramme = etapeProgrammeDao.findById(programme.getEtape().getId()).orElseThrow(() -> new NoSuchElementException("Aucune étape id= " + programme.getId() + " n'a été trouvée pour les programmes"));

        //Vérification des autorisation sur l'étape
        if (etapProgramme.getCode().equals(Etape.ModeEnum.OFF.toString()) && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn("L'utilisateur n'ayant pas au moins le rôle référent ne peut pas créer un programme avec une etape en diffusion restreinte");
            //TODO : throw new AppServiceException()

        } else {
            programmeEntity.setDiffusionRestreinte(etapProgramme.getMode().equals(Etape.ModeEnum.OFF.toString()));
            programmeEntity.setEtapeProgramme(etapProgramme);
        }

        // ajout d'un événement système de changement d'état
        programmeEntity.addEvenementProgramme(buildEvenementProgrammeEtapeUpdated(etapProgramme.getLibelle()));

        // Ajout de l'opération associée
        programmeEntity.setOperation(operationDao.findOneById(programme.getOperationId()));

        // Vérification que l'id emprise existe bien
        ProgrammeRmEntity programmeRm = programmeRmDao.findOneById(programme.getIdEmprise());
        if (programmeRm == null) {
            throw new NoSuchElementException("L'emprise de programme avec id " + programme.getIdEmprise() + " n'existe pas");
        }

        programmeEntity = programmeDao.save(programmeEntity);
        Programme programmeSaved = programmeMapper.entityToDto(programmeEntity);

        // mise à jour du programme avec les données de suivi
        programmePlannerHelper.computeSuiviHabitatOfProgramme(programmeSaved);

        //mise à jour de l'id de l'emprise dans la table des programme RM
        programmeRm.setIdTabou(programmeSaved.getId().intValue());
        programmeRmDao.save(programmeRm);

        return programmeSaved;

    }

    @Override
    @Transactional
    public Programme updateProgramme(Programme programme) {

        ProgrammeEntity programmeEntity = programmeDao.findOneById(programme.getId());

        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanUpdateProgramme(programme, programmeEntity.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programme.getNom());
        }

        // Récupération de la prochaine étape
        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findOneById(programme.getEtape().getId());

        // Mise à jour de la diffusion restreinte à partir de l'étape
        programme.setDiffusionRestreinte(null);
        if (etapeProgrammeEntity.isRemoveRestriction()) {
            programme.setDiffusionRestreinte(false);
        }

        // Permet de savoir s'il y a changement d'étape
        boolean etapeChanged = programmeEntity.getEtapeProgramme().getId() != etapeProgrammeEntity.getId();

        programmeMapper.dtoToEntity(programme, programmeEntity);

        // Ajout d'un événement système en cas de changement d'étape
        if (etapeChanged) {
            programmeEntity.addEvenementProgramme(buildEvenementProgrammeEtapeUpdated(etapeProgrammeEntity.getLibelle()));
        }

        // Mise à jour de l'opération associée
        if (programme.getOperationId() != null) {
            programmeEntity.setOperation(operationDao.findOneById(programme.getOperationId()));
        }

        programmeEntity = programmeDao.save(programmeEntity);
        Programme programmeSaved = programmeMapper.entityToDto(programmeEntity);

        // mise à jour du programme avec les données de suivi
        programmePlannerHelper.computeSuiviHabitatOfProgramme(programmeSaved);

        return programmeSaved;
    }

    @Override
    @Transactional
    public Programme updateEtapeOfProgrammeId(long programmeId, long etapeId) {
        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findOneById(etapeId);

        Programme programme = getProgrammeById(programmeId);
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapeProgrammeEntity));
        return me.updateProgramme(programme);
    }

    @Override
    public Programme getProgrammeById(long programmeId) {

        ProgrammeEntity programmeEntity = getProgrammeEntityById(programmeId);

        Programme programme = programmeMapper.entityToDto(programmeEntity);

        // mise à jour du programme avec les données de suivi
        programmePlannerHelper.computeSuiviHabitatOfProgramme(programme);

        return programme;

    }

    @Override
    public Page<Programme> searchProgrammes(ProgrammeCriteria programmeCriteria, Pageable pageable) {
        // Si l'utilisateur n'a pas le droit de voir les programmes en diffusion restreinte, on filtre sur false
        if (BooleanUtils.isTrue(programmeCriteria.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            programmeCriteria.setDiffusionRestreinte(false);
            LOGGER.warn("Accès non autorisé à des programmes d'accès restreint");
        }
        Page<Programme> programmes = programmeMapper.entitiesToDto(programmeCustomDao.searchProgrammes(programmeCriteria, pageable), pageable);
        programmePlannerHelper.computeSuiviHabitatOfProgramme(programmes);

        return programmes;
    }

    @Override
    public Page<ProgrammeLight> searchProgrammesOfOperation(ProgrammeCriteria programmeCriteria, Pageable pageable) {

        // Si l'utilisateur n'a pas le droit de voir les programmes en diffusion restreinte, on filtre sur false
        if (BooleanUtils.isTrue(programmeCriteria.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            programmeCriteria.setDiffusionRestreinte(false);
            LOGGER.warn("Accès non autorisé à des programmes d'accès restreint");
        }

        TiersAmenagementCriteria tiersAmenagementCriteria = new TiersAmenagementCriteria();
        tiersAmenagementCriteria.setProgrammeId(programmeCriteria.getProgrammeId());
        tiersAmenagementCriteria.setLibelle("MAITRE_OEUVRE");

        OperationEntity operation = operationDao.findOneById(programmeCriteria.getOperationId());

        Page<ProgrammeLight> results = null;

        if (Boolean.FALSE.equals(operation.getSecteur())) {

            Page<ProgrammeEntity> programmes = programmeCustomDao.searchProgrammes(programmeCriteria, pageable);
            results = programmeLightMapper.entitiesToDto(programmes, pageable);

        } else {

            List<ProgrammeLight> resultsList = new ArrayList<>();

            Page<ProgrammeRmEntity> programmes = programmeRmCustomDao.searchProgrammesWithinOperation(programmeCriteria, pageable);
            for (ProgrammeRmEntity p : programmes.getContent()) {

                ProgrammeEntity programme = programmeDao.findOneById(p.getIdTabou().longValue());

                //On cherche les maitres d'oeuvres de chaque programme
                Page<ProgrammeTiersEntity> programmeTiers = programmeTiersCustomDao.searchProgrammesTiers(tiersAmenagementCriteria, PaginationUtils.buildPageable(0, null, null, true, ProgrammeTiersEntity.class));

                //On construit la chaine de caractère avec tous les noms des MA
                String nomMaitresOeuvre = programmeTiers.stream()
                        .filter(programmeTiersEntity -> programmeTiersEntity.getTypeTiers().getCode().equals("MAITRE_OEUVRE"))
                        .map(programmeTiersEntity -> programmeTiersEntity.getTiers().getNom())
                        .collect(Collectors.joining(", "));

                ProgrammeLight programmeLight = programmeLightMapper.entityToDto(programme);
                programmeLight.setPromoteur(nomMaitresOeuvre);

                //Ajout du programme light à la liste de résultats
                resultsList.add(programmeLight);


            }

            results = new PageImpl<>(resultsList, pageable, programmes.getTotalElements());
        }

        return results;
    }

    @Override
    public List<Evenement> getEvenementsByProgrammeId(Long programmeId) {

        ProgrammeEntity programmeEntity = getProgrammeEntityById(programmeId);

        return evenementProgrammeMapper.entitiesToDto(List.copyOf(programmeEntity.getEvenements()));
    }

    /**
     * Construction d'un évenement programme système
     *
     * @param code                 code du type d'événement
     * @param evenementDescription description de l'événement
     * @return evenement crée
     */
    private EvenementProgrammeEntity buildEvenementProgrammeSysteme(String code, String evenementDescription) {

        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findByCode(code);
        if (typeEvenementEntity == null) {
            throw new NoSuchElementException("Le type d'événement de modification d'étape n'est pas défini.");
        }
        EvenementProgrammeEntity evenementProgrammeEntity = new EvenementProgrammeEntity();
        evenementProgrammeEntity.setEventDate(new Date());
        evenementProgrammeEntity.setTypeEvenement(typeEvenementEntity);
        evenementProgrammeEntity.setDescription(evenementDescription);
        evenementProgrammeEntity.setSysteme(true);

        return evenementProgrammeEntity;

    }

    /**
     * Construction d'un évenement programme système après changement d'étape
     *
     * @param libelleEtape libelle de l'étape
     * @return evenement crée
     */
    private EvenementProgrammeEntity buildEvenementProgrammeEtapeUpdated(String libelleEtape) {
        return this.buildEvenementProgrammeSysteme(etapeUpdatedCode, formatEtapeUpdatedMessage(libelleEtape));
    }

    private String formatEtapeUpdatedMessage(String libelleEtape) {
        return MessageFormat.format(etapeUpdatedMessage, libelleEtape);
    }

    @Override
    @Transactional
    public Evenement addEvenementByProgrammeId(Long programmeId, Evenement evenement) throws AppServiceException {

        // Programme
        ProgrammeEntity programmeEntity = programmeDao.findOneById(programmeId);
        Programme programme = programmeMapper.entityToDto(programmeEntity);
        if (!programmeRightsHelper.checkCanUpdateProgramme(programme, programme.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de créer un évènement pour le programme id = " + programmeId);
        }

        //type evenement
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getTypeEvenement().getId());
        if (typeEvenementEntity.isSysteme()) {
            throw new AccessDeniedException("Un utilisateur ne peut pas créer d'événement système");
        }

        evenement.setSysteme(false);
        EvenementProgrammeEntity evenementProgrammeEntity = evenementProgrammeMapper.dtoToEntity(evenement);
        evenementProgrammeEntity.setTypeEvenement(typeEvenementEntity);

        // Enregistrement en BDD
        evenementProgrammeEntity = evenementProgrammeDao.save(evenementProgrammeEntity);

        programmeEntity.addEvenementProgramme(evenementProgrammeEntity);

        // Enregistrement en BDD
        try {
            programmeDao.save(programmeEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter l'évènement Programme , IdEvent = "
                    + evenement.getId(), e);
        }

        return evenementProgrammeMapper.entityToDto(evenementProgrammeEntity);
    }

    @Override
    @Transactional
    public Evenement updateEvenementByProgrammeId(long idProgramme, Evenement evenement) throws AppServiceException {

        // Récupération du programme et recherche de l'évènement à modifier
        ProgrammeEntity programmeEntity = programmeDao.findOneById(idProgramme);
        Programme programme = programmeMapper.entityToDto(programmeEntity);

        Optional<EvenementProgrammeEntity> optionalEvenementProgrammeEntity = programmeEntity.lookupEvenementById(evenement.getId());
        if (optionalEvenementProgrammeEntity.isEmpty()) {
            throw new AppServiceException("L'événement id = " + evenement.getId() + " n'existe pas pour le programme id = " + programmeEntity.getId());
        }
        EvenementProgrammeEntity evenementProgrammeEntity = optionalEvenementProgrammeEntity.get();

        if (!evenementProgrammeRigthsHelper.checkCanUpdateEvenementProgramme(programme, evenementProgrammeMapper.entityToDto(evenementProgrammeEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modifier l'évènement id = " + evenementProgrammeEntity.getId()
                    + " du programme id = " + idProgramme);
        }

        // type evenement
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getTypeEvenement().getId());
        evenementProgrammeEntity.setTypeEvenement(typeEvenementEntity);

        evenementProgrammeMapper.dtoToEntity(evenement, evenementProgrammeEntity);

        // Mise à jour de l'évènement Programme en base de données
        try {
            evenementProgrammeEntity = evenementProgrammeDao.save(evenementProgrammeEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException(" Impossible de faire la mise à jour de l'évènement Programme, IdEvent = "
                    + evenement.getId(), e);
        }

        return evenementProgrammeMapper.entityToDto(evenementProgrammeEntity);
    }

    @Override
    public DocumentContent generateFicheSuivi(Long programmeId) throws AppServiceException {

        ProgrammeEntity programmeEntity = getProgrammeEntityById(programmeId);
        GenerationModel generationModel = buildGenerationModelByProgrammeId(programmeEntity);

        DocumentContent documentContent = documentGenerator.generateDocument(generationModel);
        documentContent.setFileName(buildRapportFileName(programmeEntity));

        return documentContent;
    }

    @Override
    public Page<Emprise> getEmprisesAvailables(String nom, Long operationId, Pageable pageable) {

        if (!authentificationHelper.hasAdministratorRole() && !authentificationHelper.hasContributeurRole()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de consultation des programmes");
        }

        Page<ProgrammeRmEntity> programmeRmEntities = programmeRmCustomDao.searchEmprisesNonSuivies(operationId, nom, pageable);

        return programmeRmMapper.entitiesToDto(programmeRmEntities, pageable);

    }

    @Override
    public DocumentContent downloadDocument(long programmeId, String documentId) throws AppServiceException {

        //On vérifie que le programme existe et que l'utilisateur a bien les droits de consultation dessus
        Programme programme = getProgrammeById(programmeId);

        if (!programmeRightsHelper.checkCanGetProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le programme id = " + programmeId);
        }

        try {
            return alfrescoService.downloadDocument(AlfrescoTabouType.PROGRAMME, programmeId, documentId);

        } catch (WebClientResponseException.NotFound e) {
            throw new NoSuchElementException(ERROR_RETRIEVE_DOCUMENT_CONTENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_RETRIEVE_DOCUMENT_CONTENT + documentId, e);
        }


    }

    private GenerationModel buildGenerationModelByProgrammeId(ProgrammeEntity programmeEntity) throws AppServiceException {

        InputStream templateFileInputStream;

        try {
            templateFileInputStream = new ClassPathResource("template/template_fiche_suivi.odt").getInputStream();
        } catch (IOException e) {
            throw new AppServiceException("Erreur lors de la récupération du template", e);
        }


        FicheSuiviProgrammeDataModel ficheSuiviProgrammeDataModel = new FicheSuiviProgrammeDataModel();
        ficheSuiviProgrammeDataModel.setProgramme(programmeEntity);
        ficheSuiviProgrammeDataModel.setOperation(programmeEntity.getOperation());
        ficheSuiviProgrammeDataModel.setNature(programmeEntity.getOperation().getNature());
        ficheSuiviProgrammeDataModel.setEtape(programmeEntity.getEtapeProgramme());
        ficheSuiviProgrammeDataModel.setIllustration(documentGenerator.generatedImgForTemplate());
        ficheSuiviProgrammeDataModel.setNomFichier(buildRapportFileName(programmeEntity));

        if (programmeEntity.getNumAds() != null) { // traiter le cas où le nusAds ne retourne rien

            AgapeoSuiviHabitat agapeoSuiviHabitat = agapeoDao.getAgapeoSuiviHabitatByNumAds(programmeEntity.getNumAds());
            if (agapeoSuiviHabitat != null) ficheSuiviProgrammeDataModel.setAgapeoSuiviHabitat(agapeoSuiviHabitat);

            PermisConstruireSuiviHabitat permisConstruireSuiviHabitat = permisConstruireDao.getPermisSuiviHabitatByNumAds(programmeEntity.getNumAds());
            if (permisConstruireSuiviHabitat != null)
                ficheSuiviProgrammeDataModel.setPermisSuiviHabitat(permisConstruireSuiviHabitat);

            List<AgapeoEntity> agapeos = agapeoDao.findAllByNumAds(programmeEntity.getNumAds());
            if (agapeos != null) ficheSuiviProgrammeDataModel.setAgapeos(agapeos);

            List<PermisConstruireEntity> permis = permisConstruireDao.findAllByNumAds(programmeEntity.getNumAds());
            if (permis != null) ficheSuiviProgrammeDataModel.setPermis(permis);
        }

        ficheSuiviProgrammeDataModel.setEvenements(List.copyOf(programmeEntity.getEvenements()));
        ficheSuiviProgrammeDataModel.setProgrammeTiers(programmeTiersDao.findByProgrammeId(programmeEntity.getId()));

        return new GenerationModel(ficheSuiviProgrammeDataModel, templateFileInputStream, MediaType.APPLICATION_PDF.getSubtype());
    }

    private ProgrammeEntity getProgrammeEntityById(long programmeId) {

        ProgrammeEntity programmeEntity = programmeDao.findOneById(programmeId);

        if (!programmeRightsHelper.checkCanGetProgramme(programmeMapper.entityToDto(programmeEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le programme id = " + programmeId);
        }

        return programmeEntity;

    }

    private String buildRapportFileName(ProgrammeEntity programme) {

        StringBuilder fileName = new StringBuilder("FicheSuivi_");
        fileName.append(programme.getId());
        fileName.append("_");
        fileName.append(programme.getCode());
        fileName.append("_");
        fileName.append(programme.getNom());
        fileName.append("_");
        fileName.append(System.nanoTime());

        return fileName.toString();

    }

    @Override
    public DocumentMetadata getDocumentMetadata(long programmeId, String documentId) throws AppServiceException {

        //On vérifie que le programme existe et que l'utilisateur a bien les droits de consultation dessus
        Programme programme = getProgrammeById(programmeId);

        if (!programmeRightsHelper.checkCanGetProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création du programme " + programme.getNom());
        }

        try {
            //Récupération du document Dans alfresco
            return documentMapper.entityToDto(alfrescoService.getDocumentMetadata(documentId));

        } catch (WebClientResponseException.NotFound e) {
            throw new NoSuchElementException(ERROR_RETRIEVE_METADATA_DOCUMENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_RETRIEVE_METADATA_DOCUMENT + documentId, e);
        }

    }

    @Override
    public DocumentMetadata addDocument(long programmeId, String nom, String libelle, MultipartFile file) throws AppServiceException{

        //On vérifie que le programme existe et que l'utilisateur a bien les droits de consultation dessus
        Programme programme = getProgrammeById(programmeId);

        if (!programmeRightsHelper.checkCanGetProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programme.getNom());
        }

        //Récupération du document Dans alfresco
        return documentMapper.entityToDto(alfrescoService.addDocument(nom, libelle, AlfrescoTabouType.PROGRAMME, programmeId, file));

    }

    @Override
    public void deleteDocument(long programmeId, String documentId) throws AppServiceException {

        //On vérifie que le programme existe
        Programme programmeToDelete = getProgrammeById(programmeId);

        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanUpdateProgramme(programmeToDelete, programmeToDelete.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programmeToDelete.getNom());
        }

        try {
            //Suppression du document Dans alfresco
           alfrescoService.deleteDocument(AlfrescoTabouType.PROGRAMME, programmeId, documentId);

        } catch (WebClientResponseException.NotFound e) {
            throw new NoSuchElementException(ERROR_DELETE_DOCUMENT + documentId);
        } catch (Exception e) {
            throw new AppServiceException(ERROR_DELETE_DOCUMENT + documentId, e);
        }

    }


    @Override
    public DocumentMetadata updateDocumentMetadata(long programmeId, String documentId, DocumentMetadata documentMetadata) throws AppServiceException {

        //On vérifie que le programme existe et que l'utilisateur a bien les droits de consultation dessus
        Programme programme = getProgrammeById(programmeId);

        if (!programmeRightsHelper.checkCanUpdateProgramme(programme, programme.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programme.getNom());
        }

        return documentMapper.entityToDto(alfrescoService.updateDocumentMetadata(AlfrescoTabouType.PROGRAMME, programmeId, documentId, documentMetadata, false));

    }


    @Override
    public void updateDocumentContent(long programmeId, String documentId, MultipartFile file) throws AppServiceException {

        //On vérifie que le programme existe et que l'utilisateur a bien les droits de consultation dessus
        Programme programme = getProgrammeById(programmeId);

        if (!programmeRightsHelper.checkCanUpdateProgramme(programme, programme.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programme.getNom());
        }

        alfrescoService.updateDocumentContent(AlfrescoTabouType.PROGRAMME, programmeId, documentId, file);

    }

    @Override
    public Page<DocumentMetadata> searchDocuments(long programmeId, String nom, String libelle, String typeMime, Pageable pageable){

        //On vérifie que le programme existe
        Programme programmeToDelete = getProgrammeById(programmeId);

        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanUpdateProgramme(programmeToDelete, programmeToDelete.isDiffusionRestreinte())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification du programme " + programmeToDelete.getNom());
        }

        AlfrescoDocumentRoot documentRoot = alfrescoService.searchDocuments(AlfrescoTabouType.PROGRAMME, programmeId, nom, libelle, typeMime, pageable);

        List<DocumentMetadata> results = documentMapper.entitiesToDto(new ArrayList<>(documentRoot.getList().getEntries()));

        return new PageImpl<>(results, pageable, documentRoot.getList().getPagination().getTotalItems());

    }


}

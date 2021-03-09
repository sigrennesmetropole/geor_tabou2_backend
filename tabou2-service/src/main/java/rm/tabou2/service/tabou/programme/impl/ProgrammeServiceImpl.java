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
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.ProgrammeLight;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.EvenementProgrammeRigthsHelper;
import rm.tabou2.service.helper.programme.ProgrammePlannerHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
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
import rm.tabou2.storage.ddc.dao.PermisConstruireDao;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.EvenementProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersCustomDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersDao;
import rm.tabou2.storage.tabou.dao.programme.impl.ProgrammeRmCustomDaoImpl;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import java.io.File;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammeServiceImpl.class);

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private ProgrammeCustomDao programmeCustomDao;

    @Autowired
    private ProgrammeTiersCustomDao programmeTiersCustomDao;

    @Autowired
    private ProgrammeRmCustomDaoImpl programmeRmCustomDao;

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

    @Override
    @Transactional
    public Programme createProgramme(Programme programme) {

        // Ajout des valeurs par défaut
        setProgrammeDefaultValues(programme);

        // Vérification des droits utilisateur
        if (!programmeRightsHelper.checkCanCreateProgramme(programme)) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création du programme " + programme.getNom());
        }

        // Ajout de l'état initial
        String code = BooleanUtils.isTrue(programme.isDiffusionRestreinte()) ? "EN_PROJET_OFF" : "EN_PROJET_PUBLIC";

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findByTypeAndCode(Etape.TypeEnum.START.toString(), code);
        if (etapeProgrammeEntity == null) {
            throw new NoSuchElementException("Aucune étape initiale de type " + Etape.TypeEnum.START.toString() + " n'a été " +
                    "défini pour les programmes avec diffusion restreinte = " + programme.isDiffusionRestreinte());
        }
        ProgrammeEntity programmeEntity = programmeMapper.dtoToEntity(programme);
        programmeEntity.setEtapeProgramme(etapeProgrammeEntity);

        // ajout d'un événement système de changement d'état
        programmeEntity.addEvenementProgramme(buildEvenementProgrammeEtapeUpdated(etapeProgrammeEntity.getLibelle()));

        // Ajout de l'opération associée
        programmeEntity.setOperation(operationDao.findOneById(programme.getOperationId()));

        programmeEntity = programmeDao.save(programmeEntity);
        Programme programmeSaved = programmeMapper.entityToDto(programmeEntity);

        // mise à jour du programme avec les données de suivi
        programmePlannerHelper.computeSuiviHabitatOfProgramme(programmeSaved);

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
        return programmeMapper.entitiesToDto(programmeCustomDao.searchProgrammes(programmeCriteria, pageable), pageable);
    }

    @Override
    public Page<ProgrammeLight> searchProgrammesOfOperation(ProgrammeCriteria programmeCriteria, Pageable pageable) {

        // Si l'utilisateur n'a pas le droit de voir les programmes en diffusion restreinte, on filtre sur false
        if (BooleanUtils.isTrue(programmeCriteria.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            programmeCriteria.setDiffusionRestreinte(false);
            LOGGER.warn("Accès non autorisé à des programmes d'accès restreint");
        }

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
                Page<ProgrammeTiersEntity> programmeTiers = programmeTiersCustomDao.searchProgrammesTiers(null, "MAITRE_OEUVRE", programme.getId(), PaginationUtils.buildPageable(0, null, null, true, ProgrammeTiersEntity.class));

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
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getIdType());
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
        TypeEvenementEntity typeEvenementEntity = typeEvenementDao.findOneById(evenement.getIdType());
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
        documentContent.setFileName("fiche_suivi_" + programmeEntity.getCode() + "_" + System.nanoTime());

        return documentContent;
    }

    private GenerationModel buildGenerationModelByProgrammeId(ProgrammeEntity programmeEntity) throws AppServiceException {

        InputStream templateFileInputStream;
        File fileiImgIllustration;
        try {
            templateFileInputStream = new ClassPathResource("template/template_fiche_suivi.odt").getInputStream();
        } catch (IOException e) {
            throw new AppServiceException("Erreur lors de la récupération du template", e);
        }

        try {
            // TODO: récupérer l'image avec alfresco
            fileiImgIllustration = new ClassPathResource("img/default_img.jpg").getFile();
        } catch (IOException e) {
            throw new AppServiceException("Erreur lors de la récupération de l'illustration", e);
        }

        FicheSuiviProgrammeDataModel ficheSuiviProgrammeDataModel = new FicheSuiviProgrammeDataModel();
        ficheSuiviProgrammeDataModel.setProgramme(programmeEntity);
        ficheSuiviProgrammeDataModel.setOperation(programmeEntity.getOperation());
        ficheSuiviProgrammeDataModel.setNature(programmeEntity.getOperation().getNature());
        ficheSuiviProgrammeDataModel.setEtape(programmeEntity.getEtapeProgramme());
        ficheSuiviProgrammeDataModel.setIllustration(fileiImgIllustration);
        ficheSuiviProgrammeDataModel.setAgapeoSuiviHabitat(agapeoDao.getAgapeoSuiviHabitatByNumAds(programmeEntity.getNumAds()));
        ficheSuiviProgrammeDataModel.setPermisSuiviHabitat(permisConstruireDao.getPermisSuiviHabitatByNumAds(programmeEntity.getNumAds()));
        ficheSuiviProgrammeDataModel.setAgapeos(agapeoDao.findAllByNumAds(programmeEntity.getNumAds()));
        ficheSuiviProgrammeDataModel.setPermis(permisConstruireDao.findAllByNumAds(programmeEntity.getNumAds()));
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

    /**
     * Ajout des valeurs par défaut d'un programme
     *
     * @param programme programme
     */
    private void setProgrammeDefaultValues(Programme programme) {
        if (programme.isDiffusionRestreinte() == null) {
            programme.setDiffusionRestreinte(true);
        }
    }

}

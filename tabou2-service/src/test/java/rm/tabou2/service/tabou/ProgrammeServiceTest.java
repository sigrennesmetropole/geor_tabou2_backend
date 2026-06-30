package rm.tabou2.service.tabou;


import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.common.factory.LogementDataFactory;
import rm.tabou2.service.common.factory.ProgrammeDataFactory;
import rm.tabou2.service.dto.*;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.tabou.logement.TypeLogementService;
import rm.tabou2.storage.sig.dao.ProgrammeRmDao;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.programme.*;
import rm.tabou2.storage.tabou.dao.logement.LogementsSpecifiquesDao;
import rm.tabou2.storage.tabou.dao.logement.TypeAccessionLogementDao;
import rm.tabou2.storage.tabou.dao.logement.TypeLogementDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.logement.PorteeAccessionLogement;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;
import rm.tabou2.storage.tabou.item.TypeAccessionLogementCriteria;

import java.time.OffsetDateTime;
import java.util.List;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class ProgrammeServiceTest extends DatabaseInitializerTest implements ExceptionTest {

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private ProgrammationHabitatDao programmationHabitatDao;

    @Autowired
    private LogementsSpecifiquesDao logementsSpecifiquesDao;

    @Autowired
    private ProgrammeRmDao programmeRmDao;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Autowired
    private ProgrammeDataFactory programmeDataFactory;

    @Autowired
    private LogementDataFactory logementDataFactory;

    @Autowired
    private TypeLogementDao typeLogementDao;

    @Autowired
    private TypeLogementService typeLogementService;

    @Autowired
    private TypeAccessionLogementDao typeAccessionLogementDao;

    @MockitoBean
    private ProgrammeRightsHelper programmeRightsHelper;

    @BeforeEach
    void initTest() {
        Mockito.when(programmeRightsHelper.checkCanGetProgramme(Mockito.any(Programme.class))).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanGetProgramme(Mockito.any(ProgrammeEntity.class))).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanCreateProgramme(Mockito.any())).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanUpdateProgramme(Mockito.any(), Mockito.anyBoolean())).thenReturn(true);
    }

    @AfterEach
    void afterTest() {
        programmeDao.deleteAll();
        programmationHabitatDao.deleteAll();
        logementsSpecifiquesDao.deleteAll();
        typeAccessionLogementDao.deleteAll();
        typeLogementDao.deleteAll();
        operationDao.deleteAll();
    }

    @DisplayName("testSearchProgramme: Test de la recherche de programmes")
    @Test
    void testSearchProgramme() throws AppServiceException {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity = operationDao.save(operationEntity);

        EtapeProgrammeEntity etapePublic = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");
        EtapeProgrammeEntity etapeRestreinte = etapeProgrammeDao.findByCode("EN_PROJET_OFF");

        ProgrammeRmEntity programmeRm = new ProgrammeRmEntity();
        programmeRm.setId(1);
        programmeRmDao.save(programmeRm);

        Programme programme1 = new Programme();
        programme1.setNom("nom1");
        programme1.setCode("code1");
        programme1.setNumAds("numads1");
        programme1.setOperationId(operationEntity.getId());
        programme1.setEtape(etapeProgrammeMapper.entityToDto(etapePublic));
        programme1.setIdEmprise(1);

        Programme programme2 = new Programme();
        programme2.setNom("nom2");
        programme2.setCode("code2");
        programme2.setNumAds("numads2");
        programme2.setOperationId(operationEntity.getId());
        programme2.setEtape(etapeProgrammeMapper.entityToDto(etapeRestreinte));
        programme2.setIdEmprise(1);

        Programme programme3 = new Programme();
        programme3.setNom("nom3");
        programme3.setCode("code3");
        programme3.setNumAds("numads3");
        programme3.setOperationId(operationEntity.getId());
        programme3.setEtape(etapeProgrammeMapper.entityToDto(etapePublic));
        programme3.setIdEmprise(1);

        programmeService.createProgramme(programme1);
        programmeService.createProgramme(programme2);
        programmeService.createProgramme(programme3);

        ProgrammeCriteria programmeCriteria = new ProgrammeCriteria();
        programmeCriteria.setNom("nom*");
        programmeCriteria.setDiffusionRestreinte(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));
        Page<Programme> page = programmeService.searchProgrammes(programmeCriteria, pageable);

        Assertions.assertNotNull(page.getContent());
        Assertions.assertEquals(2, page.getTotalElements());
        Assertions.assertEquals("nom3", page.getContent().get(1).getNom());
    }

    @DisplayName("testCannotCreateProgrammeWithInvalidParameters: Test de l'interdiction de la création d'un programme " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotCreateProgrammeWithInvalidParameters() {

        final Programme programme = new Programme();
        programme.setDiffusionRestreinte(true);
        programme.setNumAds("numads4");

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> programmeService.createProgramme(programme)
        );

        testConstraintViolationException(constraintViolationException, List.of("nom", "code", "operationId", "etape"));

    }

    @DisplayName("testCannotUpdateProgrammeWithInvalidParameters: Test de l'interdiction de la modification d'un programme " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotUpdateProgrammeWithInvalidParameters() {

        final Programme programme = new Programme();
        programme.setDiffusionRestreinte(true);
        programme.setNumAds("numads4");

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> programmeService.updateProgramme(programme)
        );

        testConstraintViolationException(constraintViolationException, List.of("nom", "code", "id", "etape"));

    }

    @DisplayName("testUpdateProgrammeWithDiffusionRestreinte: Test de l'édition d'un programme avec une étape qui change la diffusion restreinte'")
    @Test
    void testUpdateProgrammeWithDiffusionRestreinte() throws AppServiceException {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity = operationDao.save(operationEntity);

        ProgrammeRmEntity programmeRm = new ProgrammeRmEntity();
        programmeRm.setId(1);
        programmeRmDao.save(programmeRm);

        Operation operation = new Operation();
        operation.setId(operationEntity.getId());

        EtapeProgrammeEntity etapeRestreinte = etapeProgrammeDao.findByCode("EN_PROJET_OFF");

        Programme programme = new Programme();
        programme.setNom("nom4");
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapeRestreinte));
        programme.setCode("code4");
        programme.setNumAds("numads4");
        programme.setOperationId(operationEntity.getId());
        programme.setIdEmprise(programmeRm.getId());


        programme = programmeService.createProgramme(programme);

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");
        Assertions.assertNotNull(programme.getId());
        long programmeId = programme.getId();
        long etapeId = etapeProgrammeEntity.getId();

        programme = programmeService.updateEtapeOfProgrammeId(programmeId, etapeId);

        Assertions.assertEquals(Boolean.FALSE, programme.getDiffusionRestreinte());
    }

    @DisplayName("testCreateProgrammeWithProgrammationHabitat: Test de la création d'un programme avec une programmation habitat")
    @Test
    void testCreateProgrammeWithProgrammationHabitat() throws AppServiceException {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity = operationDao.save(operationEntity);

        ProgrammeRmEntity programmeRm = new ProgrammeRmEntity();
        programmeRm.setId(1);
        programmeRmDao.save(programmeRm);

        EtapeProgrammeEntity etapePublic = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        ProgrammationHabitat programmationHabitat = new ProgrammationHabitat();
        programmationHabitat.setNbLogements(100);
        programmationHabitat.setNbLogementsHFV(10);
        programmationHabitat.setSurfaceSHAB(5000.0);

        Programme programme = new Programme();
        programme.setNom("progHabitat1");
        programme.setCode("codeHabitat1");
        programme.setNumAds("numads_habitat1");
        programme.setOperationId(operationEntity.getId());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapePublic));
        programme.setIdEmprise(programmeRm.getId());
        programme.setProgrammationHabitat(programmationHabitat);

        Programme result = programmeService.createProgramme(programme);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getProgrammationHabitat());
        Assertions.assertEquals(100, result.getProgrammationHabitat().getNbLogements());
        Assertions.assertEquals(10, result.getProgrammationHabitat().getNbLogementsHFV());
        Assertions.assertEquals(5000.0, result.getProgrammationHabitat().getSurfaceSHAB());
    }

    @DisplayName("testUpdateProgrammeWithProgrammationHabitat: Test de la mise à jour d'un programme avec ajout d'une programmation habitat")
    @Test
    void testUpdateProgrammeWithProgrammationHabitat() throws AppServiceException {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity = operationDao.save(operationEntity);

        ProgrammeRmEntity programmeRm = new ProgrammeRmEntity();
        programmeRm.setId(1);
        programmeRmDao.save(programmeRm);

        EtapeProgrammeEntity etapePublic = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        // Créer un programme sans programmation habitat
        Programme programme = new Programme();
        programme.setNom("progHabitat2");
        programme.setCode("codeHabitat2");
        programme.setNumAds("numads_habitat2");
        programme.setOperationId(operationEntity.getId());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapePublic));
        programme.setIdEmprise(programmeRm.getId());

        programme = programmeService.createProgramme(programme);
        // Sans types actifs en base, la programmation habitat n'est pas auto-créée
        Assertions.assertNull(programme.getProgrammationHabitat());

        // Mettre à jour le programme avec une programmation habitat
        ProgrammationHabitat programmationHabitat = new ProgrammationHabitat();
        programmationHabitat.setNbLogements(200);
        programmationHabitat.setNbLogementsHFV(20);
        programmationHabitat.setSurfaceSHAB(8000.0);

        programme.setProgrammationHabitat(programmationHabitat);
        Programme updated = programmeService.updateProgramme(programme);

        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getProgrammationHabitat());
        Assertions.assertEquals(200, updated.getProgrammationHabitat().getNbLogements());
        Assertions.assertEquals(20, updated.getProgrammationHabitat().getNbLogementsHFV());
        Assertions.assertEquals(8000.0, updated.getProgrammationHabitat().getSurfaceSHAB());
    }

    @DisplayName("testUpdateProgrammeProgrammationHabitatModification: Test de la modification de la programmation habitat existante d'un programme")
    @Test
    void testUpdateProgrammeProgrammationHabitatModification() throws AppServiceException {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity = operationDao.save(operationEntity);

        ProgrammeRmEntity programmeRm = new ProgrammeRmEntity();
        programmeRm.setId(1);
        programmeRmDao.save(programmeRm);

        EtapeProgrammeEntity etapePublic = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        // Créer un programme avec une programmation habitat
        ProgrammationHabitat programmationHabitat = new ProgrammationHabitat();
        programmationHabitat.setNbLogements(100);
        programmationHabitat.setNbLogementsHFV(10);
        programmationHabitat.setSurfaceSHAB(5000.0);

        Programme programme = new Programme();
        programme.setNom("progHabitat3");
        programme.setCode("codeHabitat3");
        programme.setNumAds("numads_habitat3");
        programme.setOperationId(operationEntity.getId());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapePublic));
        programme.setIdEmprise(programmeRm.getId());
        programme.setProgrammationHabitat(programmationHabitat);

        programme = programmeService.createProgramme(programme);
        Assertions.assertNotNull(programme.getProgrammationHabitat());

        // Modifier la programmation habitat
        ProgrammationHabitat updatedHabitat = new ProgrammationHabitat();
        updatedHabitat.setNbLogements(300);
        updatedHabitat.setNbLogementsHFV(30);
        updatedHabitat.setSurfaceSHAB(12000.0);

        programme.setProgrammationHabitat(updatedHabitat);
        Programme updated = programmeService.updateProgramme(programme);

        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getProgrammationHabitat());
        Assertions.assertEquals(300, updated.getProgrammationHabitat().getNbLogements());
        Assertions.assertEquals(30, updated.getProgrammationHabitat().getNbLogementsHFV());
        Assertions.assertEquals(12000.0, updated.getProgrammationHabitat().getSurfaceSHAB());
    }

    @DisplayName("testUpdateProgrammeRemoveProgrammationHabitat: Test de la suppression de la programmation habitat d'un programme")
    @Test
    void testUpdateProgrammeRemoveProgrammationHabitat() throws AppServiceException {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity = operationDao.save(operationEntity);

        ProgrammeRmEntity programmeRm = new ProgrammeRmEntity();
        programmeRm.setId(1);
        programmeRmDao.save(programmeRm);

        EtapeProgrammeEntity etapePublic = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        // Créer un programme avec une programmation habitat
        ProgrammationHabitat programmationHabitat = new ProgrammationHabitat();
        programmationHabitat.setNbLogements(150);
        programmationHabitat.setNbLogementsHFV(15);
        programmationHabitat.setSurfaceSHAB(6000.0);

        Programme programme = new Programme();
        programme.setNom("progHabitat4");
        programme.setCode("codeHabitat4");
        programme.setNumAds("numads_habitat4");
        programme.setOperationId(operationEntity.getId());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapePublic));
        programme.setIdEmprise(programmeRm.getId());
        programme.setProgrammationHabitat(programmationHabitat);

        programme = programmeService.createProgramme(programme);
        Assertions.assertNotNull(programme.getProgrammationHabitat());

        // Supprimer la programmation habitat en passant null
        programme.setProgrammationHabitat(null);
        Programme updated = programmeService.updateProgramme(programme);

        Assertions.assertNotNull(updated);
        Assertions.assertNull(updated.getProgrammationHabitat());
    }

    // ===================== Tests de cascade : Programmation (surfaces) =====================

    @DisplayName("testCreateProgrammeWithProgrammation: création d'un programme avec une programmation (surfaces) en cascade")
    @Test
    void testCreateProgrammeWithProgrammation() throws AppServiceException {

        Programme programme = programmeDataFactory.buildBaseProgramme("progProg1", "codeProg1");

        Programmation programmation = new Programmation();
        programmation.setSurfaceBureaux(1500.0);
        programmation.setSurfaceCommerces(800.0);
        programmation.setSurfaceIndustrie(2000.0);
        programmation.setSurfaceEquipements(500.0);
        programmation.setSurfaceAutres(300.0);
        programme.setProgrammation(programmation);

        Programme result = programmeService.createProgramme(programme);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getProgrammation());
        Assertions.assertEquals(1500.0, result.getProgrammation().getSurfaceBureaux());
        Assertions.assertEquals(800.0, result.getProgrammation().getSurfaceCommerces());
        Assertions.assertEquals(2000.0, result.getProgrammation().getSurfaceIndustrie());
        Assertions.assertEquals(500.0, result.getProgrammation().getSurfaceEquipements());
        Assertions.assertEquals(300.0, result.getProgrammation().getSurfaceAutres());
    }

    @DisplayName("testUpdateProgrammeProgrammation: mise à jour de la programmation d'un programme en cascade")
    @Test
    void testUpdateProgrammeProgrammation() throws AppServiceException {

        Programme programme = programmeDataFactory.buildBaseProgramme("progProg2", "codeProg2");

        Programmation programmation = new Programmation();
        programmation.setSurfaceBureaux(1000.0);
        programme.setProgrammation(programmation);

        programme = programmeService.createProgramme(programme);
        Assertions.assertNotNull(programme.getProgrammation());
        Assertions.assertEquals(1000.0, programme.getProgrammation().getSurfaceBureaux());

        // Modifier la programmation
        programme.getProgrammation().setSurfaceBureaux(2500.0);
        programme.getProgrammation().setSurfaceCommerces(1200.0);
        Programme updated = programmeService.updateProgramme(programme);

        Assertions.assertNotNull(updated.getProgrammation());
        Assertions.assertEquals(2500.0, updated.getProgrammation().getSurfaceBureaux());
        Assertions.assertEquals(1200.0, updated.getProgrammation().getSurfaceCommerces());
    }

    // ===================== Tests de cascade : ProjetUrbain =====================

    @DisplayName("testCreateProgrammeWithProjetUrbain: création d'un programme avec un projet urbain en cascade")
    @Test
    void testCreateProgrammeWithProjetUrbain() throws AppServiceException {

        Programme programme = programmeDataFactory.buildBaseProgramme("progPU1", "codePU1");

        ProjetUrbain projetUrbain = new ProjetUrbain();
        projetUrbain.setTitle("Projet Centre-Ville");
        projetUrbain.setChapeau("Réaménagement du centre");
        projetUrbain.setProjet("Description complète du projet");
        programme.setProjetUrbain(projetUrbain);

        Programme result = programmeService.createProgramme(programme);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getProjetUrbain());
        Assertions.assertEquals("Projet Centre-Ville", result.getProjetUrbain().getTitle());
        Assertions.assertEquals("Réaménagement du centre", result.getProjetUrbain().getChapeau());
        Assertions.assertEquals("Description complète du projet", result.getProjetUrbain().getProjet());
    }

    @DisplayName("testUpdateProgrammeProjetUrbain: mise à jour du projet urbain d'un programme en cascade")
    @Test
    void testUpdateProgrammeProjetUrbain() throws AppServiceException {

        Programme programme = programmeDataFactory.buildBaseProgramme("progPU2", "codePU2");

        ProjetUrbain projetUrbain = new ProjetUrbain();
        projetUrbain.setTitle("Titre initial");
        programme.setProjetUrbain(projetUrbain);

        programme = programmeService.createProgramme(programme);
        Assertions.assertNotNull(programme.getProjetUrbain());
        Assertions.assertEquals("Titre initial", programme.getProjetUrbain().getTitle());

        programme.getProjetUrbain().setTitle("Titre modifié");
        programme.getProjetUrbain().setActualites("Nouvelles actualités");
        Programme updated = programmeService.updateProgramme(programme);

        Assertions.assertNotNull(updated.getProjetUrbain());
        Assertions.assertEquals("Titre modifié", updated.getProjetUrbain().getTitle());
        Assertions.assertEquals("Nouvelles actualités", updated.getProjetUrbain().getActualites());
    }

    // ===================== Tests de cascade : ProgrammationHabitat avec LogementsSpecifiques =====================

    @DisplayName("testCreateProgrammeWithProgrammationHabitatEtLogements: création avec logements spécifiques en cascade complète")
    @Test
    void testCreateProgrammeWithProgrammationHabitatEtLogements() throws AppServiceException {

        // Créer les types de référence
        TypeAccessionLogement typeAccession = logementDataFactory.createDefaultTypeAccessionLogementForProgramme();
        TypeLogement typeLogement = logementDataFactory.createDefaultTypeLogement();

        // Construire les logements spécifiques
        LogementSpecifique logement = new LogementSpecifique();
        logement.setTypeLogement(typeLogement);
        logement.setValeurPrevue(50);
        logement.setValeurRealisee(30);

        LogementsSpecifiques logementsSpec = new LogementsSpecifiques();
        logementsSpec.setTypeAccessionLogement(typeAccession);
        logementsSpec.setValeur(75);
        logementsSpec.setLogements(List.of(logement));

        ProgrammationHabitat habitat = new ProgrammationHabitat();
        habitat.setNbLogements(100);
        habitat.setNbLogementsHFV(10);
        habitat.setSurfaceSHAB(5000.0);
        habitat.setLogementsSpecifiques(List.of(logementsSpec));

        Programme programme = programmeDataFactory.buildBaseProgramme("progLS1", "codeLS1");
        programme.setProgrammationHabitat(habitat);

        Programme result = programmeService.createProgramme(programme);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getProgrammationHabitat());
        Assertions.assertEquals(100, result.getProgrammationHabitat().getNbLogements());
        Assertions.assertNotNull(result.getProgrammationHabitat().getLogementsSpecifiques());
        Assertions.assertEquals(1, result.getProgrammationHabitat().getLogementsSpecifiques().size());

        LogementsSpecifiques savedLS = result.getProgrammationHabitat().getLogementsSpecifiques().get(0);
        Assertions.assertNotNull(savedLS.getTypeAccessionLogement());
        Assertions.assertEquals("LOC_AIDE", savedLS.getTypeAccessionLogement().getCode());
        Assertions.assertEquals(75, savedLS.getValeur());
        Assertions.assertEquals(1, savedLS.getLogements().size());
        Assertions.assertEquals(50, savedLS.getLogements().get(0).getValeurPrevue());
        Assertions.assertEquals(30, savedLS.getLogements().get(0).getValeurRealisee());
    }

    @DisplayName("testCreateProgrammeComplet: création d'un programme avec toutes les sous-entités en cascade")
    @Test
    void testCreateProgrammeComplet() throws AppServiceException {

        Programme programme = programmeDataFactory.buildBaseProgramme("progComplet", "codeComplet");

        // Programmation (surfaces)
        Programmation programmation = new Programmation();
        programmation.setSurfaceBureaux(1000.0);
        programmation.setSurfaceCommerces(500.0);
        programme.setProgrammation(programmation);

        // Projet urbain
        ProjetUrbain projetUrbain = new ProjetUrbain();
        projetUrbain.setTitle("Grand projet");
        projetUrbain.setProjet("Un très grand projet");
        programme.setProjetUrbain(projetUrbain);

        // Programmation habitat
        ProgrammationHabitat habitat = new ProgrammationHabitat();
        habitat.setNbLogements(200);
        habitat.setNbLogementsHFV(20);
        habitat.setSurfaceSHAB(10000.0);
        programme.setProgrammationHabitat(habitat);

        Programme result = programmeService.createProgramme(programme);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getProgrammation());
        Assertions.assertEquals(1000.0, result.getProgrammation().getSurfaceBureaux());
        Assertions.assertNotNull(result.getProjetUrbain());
        Assertions.assertEquals("Grand projet", result.getProjetUrbain().getTitle());
        Assertions.assertNotNull(result.getProgrammationHabitat());
        Assertions.assertEquals(200, result.getProgrammationHabitat().getNbLogements());
    }

    // ===================== Tests du filtre logementsAides (predicateBooleanOrGreaterThanIntegerCriteria) =====================

    @DisplayName("testSearchProgrammeWithLogementsAidesTrue: recherche de programmes ayant des logements aidés")
    @Test
    void testSearchProgrammeWithLogementsAidesTrue() throws AppServiceException {

        // Programme avec logements aidés (locatif aide > 0)
        Programme progAvecAide = programmeDataFactory.buildBaseProgramme("progAide", "codeAide");
        progAvecAide.setLogementsLocatifAidePrevu(10);
        progAvecAide.setLogementsAccessAidePrevu(0);
        programmeService.createProgramme(progAvecAide);

        // Programme sans logements aidés
        Programme progSansAide = programmeDataFactory.buildBaseProgramme("progSansAide", "codeSansAide");
        progSansAide.setLogementsLocatifAidePrevu(0);
        progSansAide.setLogementsAccessAidePrevu(0);
        programmeService.createProgramme(progSansAide);

        ProgrammeCriteria criteria = new ProgrammeCriteria();
        criteria.setLogementsAides(true);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Programme> page = programmeService.searchProgrammes(criteria, pageable);

        Assertions.assertTrue(page.getTotalElements() >= 1,
                "Au moins un programme avec logements aidés doit être retourné");
        Assertions.assertTrue(page.getContent().stream()
                        .allMatch(p -> (p.getLogementsLocatifAidePrevu() != null && p.getLogementsLocatifAidePrevu() > 0)
                                || (p.getLogementsAccessAidePrevu() != null && p.getLogementsAccessAidePrevu() > 0)),
                "Tous les programmes retournés doivent avoir au moins un champ logements aidés > 0");
    }

    @DisplayName("testSearchProgrammeWithLogementsAidesFalse: recherche de programmes sans logements aidés")
    @Test
    void testSearchProgrammeWithLogementsAidesFalse() throws AppServiceException {

        // Programme avec logements aidés
        Programme progAvecAide = programmeDataFactory.buildBaseProgramme("progAide2", "codeAide2");
        progAvecAide.setLogementsLocatifAidePrevu(5);
        progAvecAide.setLogementsAccessAidePrevu(3);
        programmeService.createProgramme(progAvecAide);

        // Programme sans logements aidés
        Programme progSansAide = programmeDataFactory.buildBaseProgramme("progSansAide2", "codeSansAide2");
        progSansAide.setLogementsLocatifAidePrevu(0);
        progSansAide.setLogementsAccessAidePrevu(0);
        programmeService.createProgramme(progSansAide);

        ProgrammeCriteria criteria = new ProgrammeCriteria();
        criteria.setLogementsAides(false);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Programme> page = programmeService.searchProgrammes(criteria, pageable);

        Assertions.assertTrue(page.getTotalElements() >= 1,
                "Au moins un programme sans logements aidés doit être retourné");
        Assertions.assertTrue(page.getContent().stream()
                        .allMatch(p -> (p.getLogementsLocatifAidePrevu() == null || p.getLogementsLocatifAidePrevu() <= 0)
                                && (p.getLogementsAccessAidePrevu() == null || p.getLogementsAccessAidePrevu() <= 0)),
                "Aucun programme retourné ne doit avoir de logements aidés > 0");
    }

    @DisplayName("testSearchProgrammeWithLogementsAidesAccessOnly: recherche avec logements aidés via accession uniquement")
    @Test
    void testSearchProgrammeWithLogementsAidesAccessOnly() throws AppServiceException {

        // Programme avec logements aidés en accession seulement
        Programme progAccess = programmeDataFactory.buildBaseProgramme("progAccess", "codeAccess");
        progAccess.setLogementsLocatifAidePrevu(0);
        progAccess.setLogementsAccessAidePrevu(8);
        programmeService.createProgramme(progAccess);

        ProgrammeCriteria criteria = new ProgrammeCriteria();
        criteria.setLogementsAides(true);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Programme> page = programmeService.searchProgrammes(criteria, pageable);

        boolean found = page.getContent().stream()
                .anyMatch(p -> "progAccess".equals(p.getNom()));
        Assertions.assertTrue(found,
                "Le programme avec logements en accession aidée doit être retourné par le filtre logementsAides=true");
    }

    @DisplayName("testSearchProgrammeWithLogementsAidesNullNoFilter: recherche sans filtre logementsAides retourne tous les programmes")
    @Test
    void testSearchProgrammeWithLogementsAidesNullNoFilter() throws AppServiceException {

        // Programme avec logements aidés
        Programme progAvecAide = programmeDataFactory.buildBaseProgramme("progAll1", "codeAll1");
        progAvecAide.setLogementsLocatifAidePrevu(10);
        programmeService.createProgramme(progAvecAide);

        // Programme sans logements aidés
        Programme progSansAide = programmeDataFactory.buildBaseProgramme("progAll2", "codeAll2");
        progSansAide.setLogementsLocatifAidePrevu(0);
        progSansAide.setLogementsAccessAidePrevu(0);
        programmeService.createProgramme(progSansAide);

        ProgrammeCriteria criteriaAll = new ProgrammeCriteria();
        // logementsAides reste null → pas de filtre

        ProgrammeCriteria criteriaTrue = new ProgrammeCriteria();
        criteriaTrue.setLogementsAides(true);

        ProgrammeCriteria criteriaFalse = new ProgrammeCriteria();
        criteriaFalse.setLogementsAides(false);

        Pageable pageable = PageRequest.of(0, 100);
        long totalAll = programmeService.searchProgrammes(criteriaAll, pageable).getTotalElements();
        long totalAides = programmeService.searchProgrammes(criteriaTrue, pageable).getTotalElements();
        long totalSansAide = programmeService.searchProgrammes(criteriaFalse, pageable).getTotalElements();

        Assertions.assertTrue(totalAll >= totalAides,
                "La recherche sans filtre doit retourner au moins autant de résultats que la recherche avec logementsAides=true");
        Assertions.assertTrue(totalAll >= totalSansAide,
                "La recherche sans filtre doit retourner au moins autant de résultats que la recherche avec logementsAides=false");
    }

    // ===================== Tests auto-initialisation LogementsSpecifiques à la création =====================

    @DisplayName("testCreateProgrammeSansProgrammationHabitat_InitLogements: à la création sans programmation habitat, les logements spécifiques sont auto-initialisés avec les types actifs")
    @Test
    void testCreateProgrammeSansProgrammationHabitat_InitLogements() throws AppServiceException {

        Pageable pageable = PageRequest.of(0, 100);
        // Compter les types actifs existants AVANT la creation de nos donnees de test
        long nbTypeLogementActifsAvant = typeLogementService.searchTypeLogements(null, true, pageable).getTotalElements();
        TypeAccessionLogementCriteria criteriaProgramme = TypeAccessionLogementCriteria.builder()
                .actifUniquement(true)
                .portee(PorteeAccessionLogement.PROGRAMME)
                .build();
        long nbTypeAccessionProgrammeAvant = typeAccessionLogementDao
                .searchTypeAccessionLogements(criteriaProgramme, pageable).getTotalElements();
        // Creer des types actifs en base
        logementDataFactory.createTypeAccessionLogement("ACC_SOCIAL", "Accession sociale",
                List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));
        logementDataFactory.createTypeAccessionLogement("LOC_LIBRE", "Locatif libre",
                List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));
        // Creer un type avec portee OPERATION uniquement (ne doit pas etre inclus)
        logementDataFactory.createTypeAccessionLogement("OPERATION_ONLY", "Operation seulement",
                List.of(TypeAccessionLogement.PorteesEnum.OPERATION));
        logementDataFactory.createTypeLogement("T2", "T2");
        logementDataFactory.createTypeLogement("T3", "T3");
        // Creer un type logement inactif (dateFin dans le passe pour garantir qu'il est inactif)
        TypeLogement typeInactif = logementDataFactory.buildTypeLogement("INACTIF", "Inactif", 99);
        typeInactif.setDateDebut(OffsetDateTime.now().minusDays(60));
        typeInactif.setDateFin(OffsetDateTime.now().minusDays(1));
        typeLogementService.createTypeLogement(typeInactif);
        // Compter les types actifs MAINTENANT (juste avant la creation du programme)
        // C'est ce que le service va reellement utiliser pour l'auto-initialisation
        long nbTypeLogementActifsAttendu = typeLogementService.searchTypeLogements(null, true, pageable).getTotalElements();
        long nbTypeAccessionProgrammeAttendu = typeAccessionLogementDao
                .searchTypeAccessionLogements(criteriaProgramme, pageable).getTotalElements();
        // Creer un programme SANS programmation habitat
        Programme programme = programmeDataFactory.buildBaseProgramme("progAutoInit", "codeAutoInit");
        Programme result = programmeService.createProgramme(programme);
        // Verifications : la programmation habitat doit avoir ete creee automatiquement
        Assertions.assertNotNull(result.getProgrammationHabitat(),
                "La programmation habitat devrait etre auto-creee");
        Assertions.assertNotNull(result.getProgrammationHabitat().getLogementsSpecifiques(),
                "Les logements specifiques devraient etre auto-initialises");
        List<LogementsSpecifiques> logementsSpec = result.getProgrammationHabitat().getLogementsSpecifiques();
        // On doit avoir autant de LogementsSpecifiques que de TypeAccessionLogement actifs avec portee PROGRAMME
        Assertions.assertEquals(nbTypeAccessionProgrammeAttendu, logementsSpec.size(),
                "Il devrait y avoir un LogementsSpecifiques par TypeAccessionLogement actif avec portee PROGRAMME");
        // Chaque LogementsSpecifiques doit avoir autant d'enfants que de TypeLogement actifs
        for (LogementsSpecifiques ls : logementsSpec) {
            Assertions.assertNotNull(ls.getTypeAccessionLogement());
            Assertions.assertEquals(nbTypeLogementActifsAttendu, ls.getLogements().size(),
                    "Chaque LogementsSpecifiques devrait avoir un LogementSpecifique par TypeLogement actif");
            for (LogementSpecifique logement : ls.getLogements()) {
                Assertions.assertNotNull(logement.getTypeLogement());
                Assertions.assertNull(logement.getValeurPrevue(), "La valeur prevue devrait etre null par defaut");
                Assertions.assertNull(logement.getValeurRealisee(), "La valeur realisee devrait etre null par defaut");
            }
        }
        // Verifier que les codes des types d'accession sont bien ceux attendus
        List<String> codesAccession = logementsSpec.stream()
                .map(LogementsSpecifiques::getTypeAccessionLogement)
                .filter(java.util.Objects::nonNull)
                .map(TypeAccessionLogement::getCode)
                .filter(java.util.Objects::nonNull)
                .sorted()
                .toList();
        Assertions.assertTrue(codesAccession.contains("ACC_SOCIAL"));
        Assertions.assertTrue(codesAccession.contains("LOC_LIBRE"));
        Assertions.assertFalse(codesAccession.contains("OPERATION_ONLY"),
                "Le type avec portee OPERATION ne devrait pas etre inclus dans un programme");
    }

    @DisplayName("testCreateProgrammeAvecProgrammationHabitatVide_InitLogements: à la création avec programmation habitat vide, les logements spécifiques sont auto-initialisés")
    @Test
    void testCreateProgrammeAvecProgrammationHabitatVide_InitLogements() throws AppServiceException {

        // Créer un type actif en base
        logementDataFactory.createTypeAccessionLogement("LOC_AIDE", "Locatif aidé",
                List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));
        logementDataFactory.createTypeLogement("T2", "T2");

        // Créer un programme avec programmation habitat mais sans logements spécifiques
        Programme programme = programmeDataFactory.buildBaseProgramme("progHabitatVide", "codeHabitatVide");
        ProgrammationHabitat habitat = new ProgrammationHabitat();
        habitat.setNbLogements(50);
        programme.setProgrammationHabitat(habitat);

        Programme result = programmeService.createProgramme(programme);

        Assertions.assertNotNull(result.getProgrammationHabitat());
        Assertions.assertEquals(50, result.getProgrammationHabitat().getNbLogements());
        Assertions.assertNotNull(result.getProgrammationHabitat().getLogementsSpecifiques());
        Assertions.assertFalse(result.getProgrammationHabitat().getLogementsSpecifiques().isEmpty(),
                "Les logements spécifiques devraient être auto-initialisés même avec une programmation habitat sans logements");
    }
}

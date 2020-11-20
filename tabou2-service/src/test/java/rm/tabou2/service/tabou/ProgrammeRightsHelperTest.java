package rm.tabou2.service.tabou;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@Transactional
class ProgrammeRightsHelperTest extends DatabaseInitializerTest {

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(true);
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        programmeDao.deleteAll();
        operationDao.deleteAll();
    }

    @DisplayName("testCanCreateProgramme: Test de la possibilité de création d'un programme avec le rôle referent")
    @Test
    void testCanCreateProgramme() {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity = operationDao.save(operationEntity);

        Programme programme = new Programme();
        programme.setNom("nom1");
        programme.setDiffusionRestreinte(false);
        programme.setCode("code1");
        programme.setNumAds("numads1");
        programme.setOperationId(operationEntity.getId());

        Assertions.assertTrue(programmeRightsHelper.checkCanCreateProgramme(programme));
    }

    @DisplayName("testCanCreateProgramme: Test de l'interdiction de création d'un programme en diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotCreateProgramme() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        Programme programme = new Programme();
        programme.setNom("nom1");
        programme.setDiffusionRestreinte(true);
        programme.setCode("code1");
        programme.setNumAds("numads1");

        Assertions.assertFalse(programmeRightsHelper.checkCanCreateProgramme(programme));
    }

    @DisplayName("testCannotCreateProgrammeWithAccessDeniedOperation: Test de l'interdiction de création d'un programme le rôle contributeur" +
            " en lui affectant une opération innaccessible par l'utilisateur")
    @Test
    void testCannotCreateProgrammeWithAccessDeniedOperation() {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity = operationDao.save(operationEntity);

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        Programme programme = new Programme();
        programme.setNom("nom1");
        programme.setDiffusionRestreinte(false);
        programme.setCode("code1");
        programme.setNumAds("numads1");
        programme.setOperationId(operationEntity.getId());

        Assertions.assertFalse(programmeRightsHelper.checkCanCreateProgramme(programme));
    }

    @DisplayName("testCanUpdateProgramme: Test de la possibilité de modification d'un programme avec le rôle referent, " +
            "avec diffusion restreinte à true et en affectant une étape en non diffusion restreinte")
    @Test
    void testCanUpdateProgramme() {

        EtapeProgrammeEntity etapeProgrammeEntityRestreint = etapeProgrammeDao.findByCode("EN_PROJET_OFF");

        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");
        programmeEntity.setEtapeProgramme(etapeProgrammeEntityRestreint);

        programmeDao.save(programmeEntity);

        EtapeProgrammeEntity etapeProgrammeEntityNonRestreint = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        Programme programme = new Programme();
        programme.setId(programmeEntity.getId());
        programme.setNom(programmeEntity.getNom());
        programme.setCode(programmeEntity.getCode());
        programme.setNumAds(programmeEntity.getNumAds());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapeProgrammeEntityNonRestreint));

        Assertions.assertTrue(programmeRightsHelper.checkCanUpdateProgramme(programme, programmeEntity.isDiffusionRestreinte()));
    }

    @DisplayName("testCannotUpdateProgrammeWithRoleConsultation: Test de l'interdiction de modification " +
            "d'un programme avec le rôle consultation")
    @Test
    void testCannotUpdateProgrammeWithRoleConsultation() {

        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(false);
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        EtapeProgrammeEntity etapeProgrammeEntityPublic = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(false);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");
        programmeEntity.setEtapeProgramme(etapeProgrammeEntityPublic);

        programmeDao.save(programmeEntity);

        EtapeProgrammeEntity etapeProgrammeEntityEtude = etapeProgrammeDao.findByCode("EN_ETUDE_PUBLIC");

        Programme programme = new Programme();
        programme.setId(programmeEntity.getId());
        programme.setNom(programmeEntity.getNom());
        programme.setCode(programmeEntity.getCode());
        programme.setNumAds(programmeEntity.getNumAds());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapeProgrammeEntityEtude));

        Assertions.assertFalse(programmeRightsHelper.checkCanUpdateProgramme(programme, programmeEntity.isDiffusionRestreinte()));
    }

    @DisplayName("testCannotUpdateProgrammeWithDiffusionRestreinte: Test de l'interdiction de modification d'un programme " +
            "avec le rôle contributeur dont la diffusion restreinte est true")
    @Test
    void testCannotUpdateProgrammeWithDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        EtapeProgrammeEntity etapeProgrammeEntityRestreint = etapeProgrammeDao.findByCode("EN_PROJET_OFF");

        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");
        programmeEntity.setEtapeProgramme(etapeProgrammeEntityRestreint);

        programmeDao.save(programmeEntity);

        EtapeProgrammeEntity etapeProgrammeEntityNonRestreint = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        Programme programme = new Programme();
        programme.setId(programmeEntity.getId());
        programme.setNom(programmeEntity.getNom());
        programme.setCode(programmeEntity.getCode());
        programme.setNumAds(programmeEntity.getNumAds());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapeProgrammeEntityNonRestreint));

        Assertions.assertFalse(programmeRightsHelper.checkCanUpdateProgramme(programme, programmeEntity.isDiffusionRestreinte()));
    }

    @DisplayName("testCannotUpdateProgrammeWithInnaccessibleEtape: Test de l'interdiction de modification d'un programme " +
            "sans diffusion restreinte avec le rôle contributeur et une étape innacessible")
    @Test
    void testCannotUpdateProgrammeWithInnaccessibleEtape() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        EtapeProgrammeEntity etapeProgrammeEntityEnProjet = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");

        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(false);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");
        programmeEntity.setEtapeProgramme(etapeProgrammeEntityEnProjet);

        programmeDao.save(programmeEntity);

        EtapeProgrammeEntity etapeProgrammeEntityEnChantier = etapeProgrammeDao.findByCode("EN_CHANTIER_PUBLIC");

        Programme programme = new Programme();
        programme.setId(programmeEntity.getId());
        programme.setNom(programmeEntity.getNom());
        programme.setCode(programmeEntity.getCode());
        programme.setNumAds(programmeEntity.getNumAds());
        programme.setEtape(etapeProgrammeMapper.entityToDto(etapeProgrammeEntityEnChantier));

        Assertions.assertFalse(programmeRightsHelper.checkCanUpdateProgramme(programme, programmeEntity.isDiffusionRestreinte()));
    }

    @DisplayName("testCanGetEtapesForProgrammeDiffusionRestreinte: Test de la possibilité de récupérer la liste des étapes possibles " +
            "pour un programme en diffusion restreinte avec le rôle referent")
    @Test
    void testCanGetEtapesForProgrammeDiffusionRestreinte() {

        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");

        programmeDao.save(programmeEntity);

        Assertions.assertTrue(programmeRightsHelper.checkCanGetEtapesForProgramme(programmeEntity.getId()));
    }

    @DisplayName("testCanGetEtapesForProgrammeNonDiffusionRestreinte: Test de la possibilité de récupérer la liste des étapes " +
            "possibles pour un programme non diffusion restreinte avec le rôle contributeur")
    @Test
    void testCanGetEtapesForProgrammeNonDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(false);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");

        programmeDao.save(programmeEntity);

        Assertions.assertTrue(programmeRightsHelper.checkCanGetEtapesForProgramme(programmeEntity.getId()));
    }

    @DisplayName("testCannotGetEtapesForProgramme: Test de l'interdiction de récupérer la liste des étapes possibles " +
            "pour un programme en diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotGetEtapesForProgramme() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");

        programmeDao.save(programmeEntity);

        Assertions.assertFalse(programmeRightsHelper.checkCanGetEtapesForProgramme(programmeEntity.getId()));
    }
}

package rm.tabou2.service.tabou;

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
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class ProgrammeRightsHelperTest {

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    @Autowired
    private ProgrammeDao programmeDao;

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(true);
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(true);
    }

    @DisplayName("testCanCreateProgramme: Test de la possibilité de création d'un programme avec le rôle referent")
    @Test
    void testCanCreateProgramme() {

        Programme programme = new Programme();
        programme.setNom("nom1");
        programme.setDiffusionRestreinte(false);
        programme.setCode("code1");
        programme.setNumAds("numads1");

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

    @DisplayName("testCanUpdateProgramme: Test de la possibilité de modification d'un programme avec le rôle referent, avec diffusion restreinte en cours de modification")
    @Test
    void testCanUpdateProgramme() {

        Programme programme = new Programme();
        programme.setNom("nom1");
        programme.setDiffusionRestreinte(false);
        programme.setCode("code1");
        programme.setNumAds("numads1");

        Assertions.assertTrue(programmeRightsHelper.checkCanUpdateProgramme(programme, true));
    }

    @DisplayName("testCannotUpdateProgrammeWithUpdateDiffusionRestreinte: Test de l'interdiction de modification d'un programme avec le rôle contributeur dont la diffusion restreinte est en cours de modification")
    @Test
    void testCannotUpdateProgrammeWithUpdateDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        Programme programme = new Programme();
        programme.setNom("nom1");
        programme.setDiffusionRestreinte(false);
        programme.setCode("code1");
        programme.setNumAds("numads1");

        Assertions.assertFalse(programmeRightsHelper.checkCanUpdateProgramme(programme, true));
    }

    @DisplayName("testCannotUpdateProgrammeWithDiffusionRestreinte: Test de l'interdiction de modification d'un programme avec diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotUpdateProgrammeWithDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        Programme programme = new Programme();
        programme.setNom("nom1");
        programme.setDiffusionRestreinte(true);
        programme.setCode("code1");
        programme.setNumAds("numads1");

        Assertions.assertFalse(programmeRightsHelper.checkCanUpdateProgramme(programme, false));
    }

    @DisplayName("testCanGetEtapesForProgrammeDiffusionRestreinte: Test de la possibilité de récupérer la liste des étapes possibles pour un programme en diffusion restreinte avec le rôle referent")
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

    @DisplayName("testCanGetEtapesForProgrammeNonDiffusionRestreinte: Test de la possibilité de récupérer la liste des étapes possibles pour un programme non diffusion restreinte avec le rôle contributeur")
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

    @DisplayName("testCannotGetEtapesForProgramme: Test de l'interdiction de récupérer la liste des étapes possibles pour un programme en diffusion restreinte avec le rôle contributeur")
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

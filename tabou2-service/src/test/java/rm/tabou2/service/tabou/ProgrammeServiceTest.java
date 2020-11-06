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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class ProgrammeServiceTest extends DatabaseInitializerTest implements ExceptionTest {

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private ProgrammeService programmeService;

    @MockBean
    private ProgrammeRightsHelper programmeRightsHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(programmeRightsHelper.checkCanGetProgramme(Mockito.any())).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanCreateProgramme(Mockito.any())).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanUpdateProgramme(Mockito.any(), Mockito.anyBoolean())).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        programmeDao.deleteAll();
    }

    @DisplayName("testSearchProgramme: Test de la recherche de programmes")
    @Test
    void testSearchProgramme() {

        Programme programme1 = new Programme();
        programme1.setNom("nom1");
        programme1.setDiffusionRestreinte(false);
        programme1.setCode("code1");
        programme1.setNumAds("numads1");

        Programme programme2 = new Programme();
        programme2.setNom("nom2");
        programme2.setDiffusionRestreinte(true);
        programme2.setCode("code2");
        programme2.setNumAds("numads2");

        Programme programme3 = new Programme();
        programme3.setNom("nom3");
        programme3.setDiffusionRestreinte(false);
        programme3.setCode("code3");
        programme3.setNumAds("numads3");

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
        Assertions.assertEquals( "nom3", page.getContent().get(1).getNom());
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

        testConstraintViolationException(constraintViolationException, List.of("nom", "code"));

    }

    @DisplayName("testUpdateProgrammeWithDiffusionRestreinte: Test de l'édition d'un programme avec une étape qui change la diffusion restreinte'")
    @Test
    @Transactional
    void testUpdateProgrammeWithInaccessibleEtape() {

        Programme programme = new Programme();
        programme.setNom("nom4");
        programme.setDiffusionRestreinte(true);
        programme.setCode("code4");
        programme.setNumAds("numads4");

        programme = programmeService.createProgramme(programme);

        EtapeProgrammeEntity etapeProgrammeEntity = etapeProgrammeDao.findByCode("EN_PROJET_PUBLIC");
        long programmeId = programme.getId();
        long etapeId = etapeProgrammeEntity.getId();

        programme = programmeService.updateEtapeOfProgrammeId(programmeId, etapeId);

        Assertions.assertFalse(programme.isDiffusionRestreinte());

    }
}

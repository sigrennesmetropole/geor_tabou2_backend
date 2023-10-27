package rm.tabou2.service.tabou;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.programme.EvenementProgrammeRigthsHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.programme.EvenementProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class EvenementProgrammeServiceTest extends DatabaseInitializerTest implements ExceptionTest {

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private EvenementProgrammeDao evenementProgrammeDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private ProgrammeService programmeService;

    @MockBean
    private ProgrammeRightsHelper programmeRightsHelper;

    @MockBean
    private EvenementProgrammeRigthsHelper evenementProgrammeRigthsHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(programmeRightsHelper.checkCanGetProgramme(Mockito.any(Programme.class))).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanGetProgramme(Mockito.any(ProgrammeEntity.class))).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanCreateProgramme(Mockito.any())).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanUpdateProgramme(Mockito.any(), Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(evenementProgrammeRigthsHelper.checkCanUpdateEvenementProgramme(Mockito.any(), Mockito.any())).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        evenementProgrammeDao.deleteAll();
        programmeDao.deleteAll();
    }

    @DisplayName("testGetListEvenementsProgramme: test de la récupération de la liste des événements d'un programme")
    @Test
    void testGetListEvenementsProgramme() throws AppServiceException {
        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");

        programmeEntity = programmeDao.save(programmeEntity);

        TypeEvenement typeEvenement = new TypeEvenement();
        typeEvenement.setId(1L);

        Evenement evenement1 = new Evenement();
        evenement1.setEventDate(new Date());
        evenement1.setDescription("evenement1");
        evenement1.setTypeEvenement(typeEvenement);

        Evenement evenement2 = new Evenement();
        evenement2.setEventDate(new Date());
        evenement2.setDescription("evenement2");
        evenement2.setTypeEvenement(typeEvenement);

        Evenement evenement3 = new Evenement();
        evenement3.setEventDate(new Date());
        evenement3.setDescription("evenement3");
        evenement3.setTypeEvenement(typeEvenement);

        programmeService.addEvenementByProgrammeId(programmeEntity.getId(), evenement1);
        programmeService.addEvenementByProgrammeId(programmeEntity.getId(), evenement2);
        programmeService.addEvenementByProgrammeId(programmeEntity.getId(), evenement3);

        List<Evenement> evenementList = programmeService.getEvenementsByProgrammeId(programmeEntity.getId());

        Assertions.assertEquals(3, evenementList.size());
        Assertions.assertTrue(evenementList.stream().anyMatch(evenement -> evenement.getDescription().equals("evenement1")));
        Assertions.assertTrue(evenementList.stream().anyMatch(evenement -> evenement.getDescription().equals("evenement2")));
        Assertions.assertTrue(evenementList.stream().anyMatch(evenement -> evenement.getDescription().equals("evenement3")));
    }

    @DisplayName("testCannotCreateEvenementProgrammeWithInvalidParameters: Test de l'interdiction de la création d'un événement d'un programme " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotCreateEvenementProgrammeWithInvalidParameters() {

        final Evenement evenement = new Evenement();

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> programmeService.addEvenementByProgrammeId(1L, evenement)
        );

        testConstraintViolationException(constraintViolationException, List.of("idType", "eventDate", "description"));

    }

    @DisplayName("testCannotUpdateEvenementProgrammeWithInvalidParameters: Test de l'interdiction de la modification d'un événement d'un programme " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotUpdateEvenementProgrammeWithInvalidParameters() {

        final Evenement evenement = new Evenement();

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> programmeService.updateEvenementByProgrammeId(1L, evenement)
        );

        testConstraintViolationException(constraintViolationException, List.of("id", "idType", "eventDate", "description"));

    }

    @DisplayName("testUpdateEvenementProgramme: Test de la mise à jour d'un événement")
    @Test
    void testUpdateEvenementProgramme() throws AppServiceException {
        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");

        programmeEntity = programmeDao.save(programmeEntity);
        
        List<TypeEvenementEntity> typeEvenements = typeEvenementDao.findAll();

        TypeEvenement typeEvenement = new TypeEvenement();
        typeEvenement.setId(typeEvenements.get(0).getId());

        Evenement evenement1 = new Evenement();
        evenement1.setEventDate(new Date());
        evenement1.setDescription("evenement1");
        evenement1.setTypeEvenement(typeEvenement);

        evenement1 = programmeService.addEvenementByProgrammeId(programmeEntity.getId(), evenement1);

        Evenement evenement2 = new Evenement();
        evenement2.setId(evenement1.getId());
        evenement2.setDescription("evenement2");
        evenement2.setEventDate(evenement1.getEventDate());
        evenement2.setTypeEvenement(typeEvenement);
        evenement2.setSysteme(evenement1.getSysteme());

        evenement2 = programmeService.updateEvenementByProgrammeId(programmeEntity.getId(), evenement2);

        Assertions.assertEquals("evenement2", evenement2.getDescription());
    }
}

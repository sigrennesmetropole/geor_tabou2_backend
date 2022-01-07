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
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.operation.EvenementOperationRightsHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.operation.EvenementOperationDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class EvenementOperationServiceTest extends DatabaseInitializerTest implements ExceptionTest {

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private EvenementOperationDao evenementOperationDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private OperationService operationService;

    @MockBean
    private OperationRightsHelper operationRightsHelper;

    @MockBean
    private EvenementOperationRightsHelper evenementOperationRightsHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(operationRightsHelper.checkCanGetOperation(Mockito.any(OperationEntity.class))).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanGetOperation(Mockito.any(OperationIntermediaire.class))).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanCreateOperation(Mockito.any())).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanUpdateOperation(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(evenementOperationRightsHelper.checkCanUpdateEvenementOperation(Mockito.any(), Mockito.any())).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        evenementOperationDao.deleteAll();
        operationDao.deleteAll();
    }

    @DisplayName("testGetListEvenementsOperation: test de la récupération de la liste des événements d'une opération")
    @Test
    void testGetListEvenementsOperation() throws AppServiceException {
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");

        operationEntity = operationDao.save(operationEntity);

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

        operationService.addEvenementByOperationId(operationEntity.getId(), evenement1);
        operationService.addEvenementByOperationId(operationEntity.getId(), evenement2);
        operationService.addEvenementByOperationId(operationEntity.getId(), evenement3);

        List<Evenement> evenementList = operationService.getEvenementsByOperationId(operationEntity.getId());

        Assertions.assertEquals(3, evenementList.size());
        Assertions.assertTrue(evenementList.stream().anyMatch(evenement -> evenement.getDescription().equals("evenement1")));
        Assertions.assertTrue(evenementList.stream().anyMatch(evenement -> evenement.getDescription().equals("evenement2")));
        Assertions.assertTrue(evenementList.stream().anyMatch(evenement -> evenement.getDescription().equals("evenement3")));
    }

    @DisplayName("testCannotCreateEvenementOperationWithInvalidParameters: Test de l'interdiction de la création d'un événement d'une opération " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotCreateEvenementOperationWithInvalidParameters() {

        final Evenement evenement = new Evenement();

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> operationService.addEvenementByOperationId(1L, evenement)
        );

        testConstraintViolationException(constraintViolationException, List.of("idType", "eventDate", "description"));

    }

    @DisplayName("testCannotUpdateEvenementOperationWithInvalidParameters: Test de l'interdiction de la modification d'un événement d'une opération " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotUpdateEvenementOperationWithInvalidParameters() {

        final Evenement evenement = new Evenement();

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> operationService.updateEvenementByOperationId(1L, evenement)
        );

        testConstraintViolationException(constraintViolationException, List.of("id", "idType", "eventDate", "description"));

    }

    @DisplayName("testUpdateEvenementOperation: Test de la mise à jour d'un événement")
    @Test
    void testUpdateEvenementOperation() throws AppServiceException {
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");

        operationEntity = operationDao.save(operationEntity);

        TypeEvenement typeEvenement = new TypeEvenement();
        typeEvenement.setId(1L);

        Evenement evenement1 = new Evenement();
        evenement1.setEventDate(new Date());
        evenement1.setDescription("evenement1");
        evenement1.setTypeEvenement(typeEvenement);

        evenement1 = operationService.addEvenementByOperationId(operationEntity.getId(), evenement1);

        Evenement evenement2 = new Evenement();
        evenement2.setId(evenement1.getId());
        evenement2.setDescription("evenement2");
        evenement2.setEventDate(evenement1.getEventDate());
        evenement2.setTypeEvenement(typeEvenement);
        evenement2.setSysteme(evenement1.isSysteme());

        evenement2 = operationService.updateEvenementByOperationId(operationEntity.getId(), evenement2);

        Assertions.assertEquals("evenement2", evenement2.getDescription());
    }
}

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
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class OperationRightsHelperTest {

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private OperationDao operationDao;

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(true);
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(true);
    }

    @DisplayName("testCanCreateOperation: Test de la possibilité de création d'une opération avec le rôle referent")
    @Test
    void testCanCreateOperation() {

        Operation operation = new Operation();
        operation.setNom("nom1");
        operation.setDiffusionRestreinte(false);
        operation.setCode("code1");
        operation.setNumAds("numads1");

        Assertions.assertTrue(operationRightsHelper.checkCanCreateOperation(operation));
    }

    @DisplayName("testCannotCreateOperation: Test de l'interdiction de création d'une opération en diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotCreateOperation() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        Operation operation = new Operation();
        operation.setNom("nom1");
        operation.setDiffusionRestreinte(true);
        operation.setCode("code1");
        operation.setNumAds("numads1");

        Assertions.assertFalse(operationRightsHelper.checkCanCreateOperation(operation));
    }

    @DisplayName("testCanUpdateOperation: Test de la possibilité de modification d'une opération avec le rôle referent, avec diffusion restreinte en cours de modification")
    @Test
    void testCanUpdateOperation() {

        Operation operation = new Operation();
        operation.setNom("nom1");
        operation.setDiffusionRestreinte(false);
        operation.setCode("code1");
        operation.setNumAds("numads1");

        Assertions.assertTrue(operationRightsHelper.checkCanUpdateOperation(operation, true));
    }

    @DisplayName("testCannotUpdateOperationWithUpdateDiffusionRestreinte: Test de l'interdiction de modification d'une opération avec le rôle contributeur dont la diffusion restreinte est en cours de modification")
    @Test
    void testCannotUpdateOperationWithUpdateDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        Operation operation = new Operation();
        operation.setNom("nom1");
        operation.setDiffusionRestreinte(false);
        operation.setCode("code1");
        operation.setNumAds("numads1");

        Assertions.assertFalse(operationRightsHelper.checkCanUpdateOperation(operation, true));
    }

    @DisplayName("testCannotUpdateOperationWithDiffusionRestreinte: Test de l'interdiction de modification d'une opération avec diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotUpdateOperationWithDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        Operation operation = new Operation();
        operation.setNom("nom1");
        operation.setDiffusionRestreinte(true);
        operation.setCode("code1");
        operation.setNumAds("numads1");

        Assertions.assertFalse(operationRightsHelper.checkCanUpdateOperation(operation, false));
    }

    @DisplayName("testCanGetEtapesForOperationDiffusionRestreinte: Test de la possibilité de récupérer la liste des étapes possibles pour une opération en diffusion restreinte avec le rôle referent")
    @Test
    void testCanGetEtapesForOperationDiffusionRestreinte() {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");

        operationDao.save(operationEntity);

        Assertions.assertTrue(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }

    @DisplayName("testCanGetEtapesForOperationNonDiffusionRestreinte: Test de la possibilité de récupérer la liste des étapes possibles pour une opération non diffusion restreinte avec le rôle contributeur")
    @Test
    void testCanGetEtapesForOperationNonDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");

        operationDao.save(operationEntity);

        Assertions.assertTrue(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }

    @DisplayName("testCannotGetEtapesForOperation: Test de l'interdiction de récupérer la liste des étapes possibles pour une opération en diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotGetEtapesForOperation() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom4");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code4");
        operationEntity.setNumAds("numads4");

        operationDao.save(operationEntity);

        Assertions.assertFalse(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }
}

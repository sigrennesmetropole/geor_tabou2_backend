package rm.tabou2.service.tabou;

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
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.helper.operation.EvenementOperationRightsHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class EvenementOperationRightsHelperTest {

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private EvenementOperationRightsHelper evenementOperationRightsHelper;

    @MockBean
    private OperationRightsHelper operationRightsHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(operationRightsHelper.checkCanUpdateOperation(Mockito.any(), Mockito.any())).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        operationDao.deleteAll();
    }

    @DisplayName("testCanUpdateEvenementOperation: test pour vérifier qu'un événement ne peut être modifié que s'il n'est pas un événement systeme")
    @Test
    void testCanUpdateEvenementOperation() {
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setDiffusionRestreinte(true);
        operationEntity = operationDao.save(operationEntity);

        Evenement actualEvenement = new Evenement();
        actualEvenement.setSysteme(false);

        Assertions.assertTrue(evenementOperationRightsHelper.checkCanUpdateEvenementOperation(operationMapper.entityToDto(operationEntity), actualEvenement));

        actualEvenement = new Evenement();
        actualEvenement.setSysteme(true);

        Assertions.assertFalse(evenementOperationRightsHelper.checkCanUpdateEvenementOperation(operationMapper.entityToDto(operationEntity), actualEvenement));

    }
}

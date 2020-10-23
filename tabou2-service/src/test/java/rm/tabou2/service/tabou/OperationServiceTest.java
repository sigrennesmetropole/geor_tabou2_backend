package rm.tabou2.service.tabou;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class OperationServiceTest extends DatabaseInitializerTest {

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationService operationService;

    @Test
    void testSearchOperation() {

        // enregistrer une operation dans la base temporaire H2
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(false);
        operationDao.save(operationEntity);


        OperationsCriteria operationsCriteria = new OperationsCriteria();
        operationsCriteria.setNom("tes*");
        operationsCriteria.setDiffusionRestreinte(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));

        Page<Operation> page = operationService.searchOperations(operationsCriteria, pageable);

        Assertions.assertNotNull(page.getContent());
        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals( "test", page.getContent().get(0).getNom());
    }


}

package rm.tabou2.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.storage.tabou.dao.OperationDao;
import rm.tabou2.storage.tabou.entity.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:application.properties" })
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class OperationServiceTest{

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationService operationService;

    @Test
    public void testSearchOperation() {

        // enregistrer une operation dans la base temporaire H2
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRetreinte(false);
        operationDao.save(operationEntity);


        OperationsCriteria operationsCriteria = new OperationsCriteria();
        operationsCriteria.setNom("test");
        operationsCriteria.setDiffusionRestreinte(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));

        Page<Operation> page = operationService.searchOperations(operationsCriteria, pageable);


        Assert.assertNotNull(page.getContent());
        Assert.assertEquals(page.getTotalElements(), 1);
        Assert.assertEquals(page.getContent().get(0).getNom(), "test");
    }


}

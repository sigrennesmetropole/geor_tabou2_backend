package rm.tabou2.service.tabou;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.storage.sig.dao.SecteurDao;
import rm.tabou2.storage.sig.dao.ZaDao;
import rm.tabou2.storage.sig.dao.ZacDao;
import rm.tabou2.storage.sig.entity.SecteurEntity;
import rm.tabou2.storage.sig.entity.ZaEntity;
import rm.tabou2.storage.sig.entity.ZacEntity;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class OperationEmpriseHelperTest extends DatabaseInitializerTest {

    @Autowired
    private OperationEmpriseHelper operationEmpriseHelper;

    @Autowired
    private SecteurDao secteurDao;

    @Autowired
    private ZacDao zacDao;

    @Autowired
    private ZaDao zaDao;

    @AfterEach
    public void afterTest() {
        secteurDao.deleteAll();
        zacDao.deleteAll();
        zaDao.deleteAll();
    }

    @Test
    void testGetAvailableEmprises() {
        SecteurEntity secteurEntity1 = new SecteurEntity();
        secteurEntity1.setId(1);
        secteurEntity1.setIdTabou(1);

        SecteurEntity secteurEntity2 = new SecteurEntity();
        secteurEntity2.setId(1);

        secteurDao.save(secteurEntity1);
        secteurDao.save(secteurEntity2);

        Assertions.assertEquals(1, operationEmpriseHelper.getAvailableEmprises(3L, true).size());
        Assertions.assertEquals(0, operationEmpriseHelper.getAvailableEmprises(2L, false).size());

        ZacEntity zacEntity1 = new ZacEntity();
        zacEntity1.setId(1);

        ZacEntity zacEntity2 = new ZacEntity();
        zacEntity2.setId(2);

        zacDao.save(zacEntity1);
        zacDao.save(zacEntity2);

        Assertions.assertEquals(2, operationEmpriseHelper.getAvailableEmprises(1L, false).size());

        ZaEntity zaEntity1 = new ZaEntity();
        zaEntity1.setId(1);

        ZaEntity zaEntity2 = new ZaEntity();
        zaEntity2.setId(2);

        ZaEntity zaEntity3 = new ZaEntity();
        zaEntity3.setId(3);

        ZaEntity zaEntity4 = new ZaEntity();
        zaEntity4.setId(4);
        zaEntity4.setIdTabou(4);

        zaDao.save(zaEntity1);
        zaDao.save(zaEntity2);
        zaDao.save(zaEntity3);
        zaDao.save(zaEntity4);

        Assertions.assertEquals(3, operationEmpriseHelper.getAvailableEmprises(2L, false).size());

    }
}

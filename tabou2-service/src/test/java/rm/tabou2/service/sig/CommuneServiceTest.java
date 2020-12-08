package rm.tabou2.service.sig;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.storage.sig.dao.CommuneDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class CommuneServiceTest {

    @Autowired
    private CommuneDao communeDao;

    @Autowired
    private CommuneService communeService;

    @Test
    @Disabled
    void testSearchCommune() {

        // enregistrer une operation dans la base temporaire H2
        CommuneEntity commune = new CommuneEntity();
        commune.setNom("rennes");
        communeDao.save(commune);


        OperationsCriteria operationsCriteria = new OperationsCriteria();
        operationsCriteria.setNom("rennes");
        operationsCriteria.setDiffusionRestreinte(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));

        List<Commune> page = null;
        try {
            page = communeService.searchCommunes("rennes", 0, 10, "nom", true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Assertions.assertEquals(1, page.size());
        Assertions.assertEquals("rennes", page.get(0).getNom());
    }

}

package rm.tabou2.service.sig;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.storage.sig.dao.IrisDao;
import rm.tabou2.storage.sig.entity.IrisEntity;
import rm.tabou2.storage.sig.item.IrisCriteria;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class IrisServiceTest {

    private static final String NMIRIS = "Albert de Mun";

    @Autowired
    private IrisDao irisDao;

    @Autowired
    private IrisService irisService;

    @DisplayName("testSearchIris: test de recherche d'une iris")
    @Test
    public void testSearchIris() {

        IrisEntity iris = new IrisEntity();
        iris.setNmiris(NMIRIS);
        iris.setId(1);
        irisDao.save(iris);

        IrisCriteria irisCriteria = new IrisCriteria();
        irisCriteria.setNom(NMIRIS);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nmiris"));

        Page<Iris> page = null;
        try {
            page = irisService.searchIris(irisCriteria, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(NMIRIS, page.getContent().get(0).getNmiris());
    }

}

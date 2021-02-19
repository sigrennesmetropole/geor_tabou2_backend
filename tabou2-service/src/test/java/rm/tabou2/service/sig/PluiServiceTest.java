package rm.tabou2.service.sig;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.storage.sig.dao.PluiDao;
import rm.tabou2.storage.sig.entity.PluiEntity;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class PluiServiceTest {

    public static final String LIBELLE_PLUI = "1AU";
    @Autowired
    private PluiDao pluiDao;

    @Autowired
    private PluiService pluiService;

    @Test
    void testSearchPlui() {

        // enregistrer une operation dans la base temporaire H2
        PluiEntity plui = new PluiEntity();
        plui.setLibelle(LIBELLE_PLUI);
        plui.setId(1);
        pluiDao.save(plui);


        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "libelle"));

        Page<PluiZonage> page = null;
        try {
            page = pluiService.searchPlui(LIBELLE_PLUI, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(LIBELLE_PLUI, page.getContent().get(0).getLibelle());
    }

}

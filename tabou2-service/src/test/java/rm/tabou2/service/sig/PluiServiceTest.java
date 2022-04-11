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

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.storage.sig.dao.PluiDao;
import rm.tabou2.storage.sig.entity.PluiEntity;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class PluiServiceTest {

    public static final String LIBELLE_PLUI = "1AU";
    @Autowired
    private PluiDao pluiDao;

    @Autowired
    private PluiService pluiService;

    @DisplayName("testSearchPlui : test de recherche d'un plui")
    @Test
    public void testSearchPlui() {

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

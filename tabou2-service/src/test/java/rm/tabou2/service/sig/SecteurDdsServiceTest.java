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
import rm.tabou2.service.dto.SecteurDds;
import rm.tabou2.storage.sig.dao.SecteurDdsDao;
import rm.tabou2.storage.sig.entity.SecteurDdsEntity;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class SecteurDdsServiceTest {

    @Autowired
    private SecteurDdsDao secteurDdsDao;

    @Autowired
    private SecteurDdsService secteurDdsService;

    private static final String SECTEUR_NAME = "Est";

    @DisplayName("testSearchSecteurDds : test de recherche d'un secteur DDS")
    @Test
    void testSearchSecteurDds() {

        SecteurDdsEntity secteurDds = new SecteurDdsEntity();
        secteurDds.setId(1);
        secteurDds.setSecteur(SECTEUR_NAME);
        secteurDdsDao.save(secteurDds);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "secteur"));

        Page<SecteurDds> page = null;
        try {
            page = secteurDdsService.searchSecteursDds(SECTEUR_NAME, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(SECTEUR_NAME, page.getContent().get(0).getSecteur());

    }


}

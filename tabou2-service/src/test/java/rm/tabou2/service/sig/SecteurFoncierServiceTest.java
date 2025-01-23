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
import rm.tabou2.service.dto.SecteurFoncier;
import rm.tabou2.storage.sig.dao.SecteurFoncierDao;
import rm.tabou2.storage.sig.entity.SecteurFoncierEntity;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class SecteurFoncierServiceTest {

    @Autowired
    private SecteurFoncierDao secteurFoncierDao;

    @Autowired
    private SecteurFoncierService secteurFoncierService;

    private static final String NEGOCIATEUR = "David Verlingue";

    @DisplayName("testSearchSecteurFoncier : test de recherche d'un secteur foncier")
    @Test
    void testSearchSecteurFoncier() {

        SecteurFoncierEntity secteurFoncier = new SecteurFoncierEntity();
        secteurFoncier.setId(1L);
        secteurFoncier.setNegociateur(NEGOCIATEUR);
        secteurFoncierDao.save(secteurFoncier);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "negociateur"));

        Page<SecteurFoncier> page = null;
        try {
            page = secteurFoncierService.searchSecteursFonciers(NEGOCIATEUR, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(NEGOCIATEUR, page.getContent().get(0).getNegociateur());

    }


}

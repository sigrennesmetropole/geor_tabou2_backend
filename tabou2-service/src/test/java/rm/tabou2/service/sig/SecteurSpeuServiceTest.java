package rm.tabou2.service.sig;


import org.junit.jupiter.api.AfterEach;
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
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.storage.sig.dao.SecteurSpeuDao;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class SecteurSpeuServiceTest {

    @Autowired
    private SecteurSpeuDao secteurSpeuDao;

    @Autowired
    private SecteurSpeuService secteurSpeuService;

    @AfterEach
    public void afterTest() {
        secteurSpeuDao.deleteAll();
    }

    private static final String SECTEUR_NAME = "Est";

    @DisplayName("testSearchSecteurSpeu : test de recherche d'un secteur SPEU")
    @Test
    public void testSearchSecteurSpeu() {

        SecteurSpeuEntity secteurSpeu = new SecteurSpeuEntity();
        secteurSpeu.setNumSecteur(2);
        secteurSpeu.setNomSecteur(SECTEUR_NAME);
        secteurSpeuDao.save(secteurSpeu);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nomSecteur"));

        Page<SecteurSpeu> page = null;
        try {
            page = secteurSpeuService.searchSecteursSpeu(null, SECTEUR_NAME, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(SECTEUR_NAME, page.getContent().get(0).getNomSecteur());

    }

}

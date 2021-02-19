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
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.storage.sig.dao.SecteurSpeuDao;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class SecteurSpeuServiceTest {

    @Autowired
    private SecteurSpeuDao secteurSpeuDao;

    @Autowired
    private SecteurSpeuService secteurSpeuService;

    private static final String SECTEUR_NAME = "Est";

    @Test
    void testSearchSecteurSpeu() {

        // enregistrer une operation dans la base temporaire H2
        SecteurSpeuEntity secteurSpeu = new SecteurSpeuEntity();
        secteurSpeu.setNumSecteur(2);
        secteurSpeu.setNomSecteur(SECTEUR_NAME);
        secteurSpeuDao.save(secteurSpeu);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));

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

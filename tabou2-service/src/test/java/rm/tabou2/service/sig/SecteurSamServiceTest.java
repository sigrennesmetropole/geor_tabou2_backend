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
import rm.tabou2.service.dto.SecteurSam;
import rm.tabou2.storage.sig.dao.SecteurSamDao;
import rm.tabou2.storage.sig.entity.SecteurSamEntity;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class SecteurSamServiceTest {


    @Autowired
    private SecteurSamDao secteurSamDao;

    @Autowired
    private SecteurSamService secteurSamService;

    private static final String SECTEUR_NAME = "Est";

    @DisplayName("testSearchSecteurSam : test de recherche d'un secteur SAM")
    @Test
    void testSearchSecteurSam() {

        SecteurSamEntity secteurSam = new SecteurSamEntity();
        secteurSam.setId(1);
        secteurSam.setNomSecteur(SECTEUR_NAME);
        secteurSamDao.save(secteurSam);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nomSecteur"));

        Page<SecteurSam> page = null;
        try {
            page = secteurSamService.searchSecteursSam(SECTEUR_NAME, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(SECTEUR_NAME, page.getContent().get(0).getNomSecteur());

    }
    
}

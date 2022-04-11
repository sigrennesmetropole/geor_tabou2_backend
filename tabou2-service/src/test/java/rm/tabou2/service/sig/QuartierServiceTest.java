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
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.storage.sig.dao.QuartierDao;
import rm.tabou2.storage.sig.entity.QuartierEntity;
import rm.tabou2.storage.sig.item.QuartierCriteria;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class QuartierServiceTest {


    public static final String NOM_QUARTIER = "Bourg l'Evesque - La Touche - Moulin du Comte";

    @Autowired
    private QuartierDao quartierDao;

    @Autowired
    private QuartierService quartierService;

    @DisplayName("testSearchQuartier : test de recherche d'un quartier")
    @Test
    public void testSearchQuartier() {

        QuartierEntity quartier = new QuartierEntity();
        quartier.setNom(NOM_QUARTIER);
        quartier.setId(1);
        quartierDao.save(quartier);


        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));

        QuartierCriteria quartierCriteria = new QuartierCriteria();
        quartierCriteria.setNom(NOM_QUARTIER);

        Page<Quartier> page = null;
        try {
            page = quartierService.searchQuartiers(quartierCriteria, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(NOM_QUARTIER, page.getContent().get(0).getNom());
    }


}

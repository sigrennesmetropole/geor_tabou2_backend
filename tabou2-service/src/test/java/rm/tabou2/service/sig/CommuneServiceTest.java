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
import rm.tabou2.service.dto.Commune;
import rm.tabou2.storage.sig.dao.CommuneDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class CommuneServiceTest {

    @Autowired
    private CommuneDao communeDao;

    @Autowired
    private CommuneService communeService;

    @DisplayName("testSearchCommune: test de recherche d'une commune")
    @Test
    public void testSearchCommune() {

        CommuneEntity commune = new CommuneEntity();
        commune.setNom("rennes");
        commune.setId(1);
        commune.setCommuneAgglo((short) 1);
        communeDao.save(commune);


        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));

        Page<Commune> page = null;
        try {
            page = communeService.searchCommunes("rennes", null, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals("rennes", page.getContent().get(0).getNom());
    }

}

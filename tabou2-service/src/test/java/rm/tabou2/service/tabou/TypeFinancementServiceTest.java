package rm.tabou2.service.tabou;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.TypeFinancement;
import rm.tabou2.service.tabou.financement.TypeFinancementService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.financement.TypeFinancementDao;
import rm.tabou2.storage.tabou.entity.financement.TypeFinancementEntity;
import rm.tabou2.storage.tabou.item.TypeFinancementCriteria;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class TypeFinancementServiceTest {
    @Autowired
    TypeFinancementService typeFinancementService;

    @Autowired
    TypeFinancementDao typeFinancementDao;

    TypeFinancementEntity typeFinancementEntity1;
    TypeFinancementEntity typeFinancementEntity2;
    TypeFinancementEntity typeFinancementEntity3;
    TypeFinancementEntity typeFinancementEntity4;
    TypeFinancementEntity typeFinancementEntity5;
    TypeFinancementEntity typeFinancementEntity6;


    @Before
    public void init() {

        typeFinancementEntity1 = new TypeFinancementEntity();
        typeFinancementEntity1.setLibelle("Accession aidée");
        typeFinancementEntity1.setCode("ACC_AIDEE");
        typeFinancementEntity1.setEstAide(true);

        typeFinancementEntity2 = new TypeFinancementEntity();
        typeFinancementEntity2.setLibelle("Accession libre");
        typeFinancementEntity2.setCode("ACC_LIBRE");
        typeFinancementEntity2.setEstAide(false);

        typeFinancementEntity3 = new TypeFinancementEntity();
        typeFinancementEntity3.setLibelle("Accession maitrisée");
        typeFinancementEntity3.setCode("ACC_MAITRISEE");
        typeFinancementEntity3.setEstAide(false);

        typeFinancementEntity4 = new TypeFinancementEntity();
        typeFinancementEntity4.setLibelle("Locatif aidé");
        typeFinancementEntity4.setCode("LOC_AIDE");
        typeFinancementEntity4.setEstAide(true);

        typeFinancementEntity5 = new TypeFinancementEntity();
        typeFinancementEntity5.setLibelle("Locatif régulé privé");
        typeFinancementEntity5.setCode("LOC_REGULE_PRIVE");
        typeFinancementEntity5.setEstAide(false);

        typeFinancementEntity6 = new TypeFinancementEntity();
        typeFinancementEntity6.setLibelle("Locatif régulé HLM");
        typeFinancementEntity6.setCode("LOC_REGULE_HLM");
        typeFinancementEntity6.setEstAide(false);

        typeFinancementDao.save(typeFinancementEntity1);
        typeFinancementDao.save(typeFinancementEntity2);
        typeFinancementDao.save(typeFinancementEntity3);
        typeFinancementDao.save(typeFinancementEntity4);
        typeFinancementDao.save(typeFinancementEntity5);
        typeFinancementDao.save(typeFinancementEntity6);


    }

    @After
    public void after() {
        typeFinancementDao.deleteAll();
    }

    @Test
    public void testRechercheTypeFinancement() {
        TypeFinancementCriteria typeFinancementCriteria = new TypeFinancementCriteria();
        Pageable pageable = PaginationUtils.buildPageable(0, 10, "code", true, TypeFinancementEntity.class);

        // Recherche de la liste de tous les types de financement
        typeFinancementCriteria.setLibelle(null);
        Page<TypeFinancement> page = typeFinancementService.searchTypeFinancement(typeFinancementCriteria, pageable);

        Assert.assertEquals("Il doit y avoir au total tous les 6 types de financement", 6, page.getContent().size());


        // Recherche du type de financement 5
        typeFinancementCriteria.setLibelle("Locatif régulé privé");
        page = typeFinancementService.searchTypeFinancement(typeFinancementCriteria, pageable);

        Assert.assertEquals("Un seul élément contient dans son libelle 'Locatif régulé privé'", 1, page.getContent().size());
        Assert.assertEquals("Le code doit être LOC_REGULE_PRIVE", typeFinancementEntity5.getCode(), page.getContent().get(0).getCode());

        // Recherche du type de financement 6
        typeFinancementCriteria.setLibelle("*HLM");
        page = typeFinancementService.searchTypeFinancement(typeFinancementCriteria, pageable);

        Assert.assertEquals("Un seul élément contient dans son libelle '*HLM'", 1, page.getContent().size());
        Assert.assertEquals("Le code doit être LOC_REGULE_HLM", typeFinancementEntity6.getCode(), page.getContent().get(0).getCode());

    }
}

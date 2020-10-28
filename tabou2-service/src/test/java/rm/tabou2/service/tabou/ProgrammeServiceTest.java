package rm.tabou2.service.tabou;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.mapper.tabou.programme.EtapeProgrammeMapper;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import javax.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class ProgrammeServiceTest extends DatabaseInitializerTest {

    @Autowired
    private EtapeProgrammeDao etapeProgrammeDao;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private EtapeProgrammeMapper etapeProgrammeMapper;

    @Autowired
    private ProgrammeService programmeService;

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.getConnectedUsername()).thenReturn("ProgrammeServiceTest");
    }

    @AfterEach
    public void afterTest() {
        programmeDao.deleteAll();
    }

    @DisplayName("testSearchProgramme: Test de la recherche de programmes")
    @Test
    void testSearchProgramme() {

        Programme programme1 = new Programme();
        programme1.setNom("nom1");
        programme1.setDiffusionRestreinte(false);
        programme1.setCode("code1");
        programme1.setNumAds("numads1");

        Programme programme2 = new Programme();
        programme2.setNom("nom2");
        programme2.setDiffusionRestreinte(true);
        programme2.setCode("code2");
        programme2.setNumAds("numads2");

        Programme programme3 = new Programme();
        programme3.setNom("nom3");
        programme3.setDiffusionRestreinte(false);
        programme3.setCode("code3");
        programme3.setNumAds("numads3");

        programmeService.createProgramme(programme1);
        programmeService.createProgramme(programme2);
        programmeService.createProgramme(programme3);

        ProgrammeCriteria programmeCriteria = new ProgrammeCriteria();
        programmeCriteria.setNom("nom*");
        programmeCriteria.setDiffusionRestreinte(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));
        Page<Programme> page = programmeService.searchProgrammes(programmeCriteria, pageable);

        Assertions.assertNotNull(page.getContent());
        Assertions.assertEquals(2, page.getTotalElements());
        Assertions.assertEquals( "nom3", page.getContent().get(1).getNom());
    }

    @DisplayName("testUpdateProgrammeWithInaccessibleEtape: Test de l'édition d'un programme avec une étape inadéquate'")
    @Test
    @Transactional
    void testUpdateProgrammeWithInaccessibleEtape() {

        Programme programme4 = new Programme();
        programme4.setNom("nom4");
        programme4.setDiffusionRestreinte(false);
        programme4.setCode("code4");
        programme4.setNumAds("numads4");

        Programme programme5 = programmeService.createProgramme(programme4);

        Etape etape = etapeProgrammeMapper.entityToDto(etapeProgrammeDao.findByCode("ACHEVE_PUBLIC"));
        programme5.setEtape(etape);
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> programmeService.updateProgramme(programme5)
        );
    }
}
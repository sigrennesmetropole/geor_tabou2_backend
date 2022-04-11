package rm.tabou2.service.tabou;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.tabou.programme.EtapeProgrammeService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

@TestPropertySource(value = { "classpath:application.properties" })
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class EtapeProgrammeServiceTest {

	@Autowired
	private EtapeProgrammeService etapeProgrammeService;

	@MockBean
	private AuthentificationHelper authentificationHelper;

	@Autowired
	private EtapeProgrammeDao etapeProgrammeDao;

	private EtapeProgrammeEntity etape1;
	private EtapeProgrammeEntity etape2;
	private EtapeProgrammeEntity etape3;
	private EtapeProgrammeEntity etape4;
	private EtapeProgrammeEntity etape5;
	private EtapeProgrammeEntity etape6;
	private EtapeProgrammeEntity etape7;
	private EtapeProgrammeEntity etape8;
	private EtapeProgrammeEntity etape9;

	@BeforeEach
	public void init() {

		etape1 = new EtapeProgrammeEntity();
		etape1.setCode("EN_PROJET_OFF");
		etape1.setLibelle("En projet");
		etape1.setMode("OFF");
		etape1.setType("START");
		etape1.setRemoveRestriction(false);

		etape2 = new EtapeProgrammeEntity();
		etape2.setCode("EN_ETUDE_OFF");
		etape2.setLibelle("En étude");
		etape2.setMode("OFF");
		etape2.setType("NORMAL");
		etape2.setRemoveRestriction(false);

		etape3 = new EtapeProgrammeEntity();
		etape3.setCode("ANNULE_OFF");
		etape3.setLibelle("Annulé");
		etape3.setMode("OFF");
		etape3.setType("END");
		etape3.setRemoveRestriction(false);

		etape4 = new EtapeProgrammeEntity();
		etape4.setCode("EN_PROJET_PUBLIC");
		etape4.setLibelle("En projet");
		etape4.setMode("PUBLIC");
		etape4.setType("START");
		etape4.setRemoveRestriction(true);

		etape5 = new EtapeProgrammeEntity();
		etape5.setCode("EN_ETUDE_PUBLIC");
		etape5.setLibelle("En étude");
		etape5.setMode("PUBLIC");
		etape5.setType("NORMAL");
		etape5.setRemoveRestriction(true);

		etape6 = new EtapeProgrammeEntity();
		etape6.setCode("EN_CHANTIER_PUBLIC");
		etape6.setLibelle("En chantier");
		etape6.setMode("PUBLIC");
		etape6.setType("NORMAL");
		etape6.setRemoveRestriction(false);

		etape7 = new EtapeProgrammeEntity();
		etape7.setCode("ACHEVE_PUBLIC");
		etape7.setLibelle("Achevé");
		etape7.setMode("PUBLIC");
		etape7.setType("NORMAL");
		etape7.setRemoveRestriction(false);

		etape8 = new EtapeProgrammeEntity();
		etape8.setCode("CLOTURE_PUBLIC");
		etape8.setLibelle("Clôturé");
		etape8.setMode("PUBLIC");
		etape8.setType("END");
		etape8.setRemoveRestriction(false);

		etape9 = new EtapeProgrammeEntity();
		etape9.setCode("ANNULE_PUBLIC");
		etape9.setLibelle("Annulé");
		etape9.setMode("PUBLIC");
		etape9.setType("NORMAL");
		etape9.setRemoveRestriction(false);

		etape1 = etapeProgrammeDao.save(etape1);
		etape2 = etapeProgrammeDao.save(etape2);
		etape3 = etapeProgrammeDao.save(etape3);
		etape4 = etapeProgrammeDao.save(etape4);
		etape5 = etapeProgrammeDao.save(etape5);
		etape6 = etapeProgrammeDao.save(etape6);
		etape7 = etapeProgrammeDao.save(etape7);
		etape8 = etapeProgrammeDao.save(etape8);
		etape9 = etapeProgrammeDao.save(etape9);
	}

	@AfterEach
	public void after() {
		etapeProgrammeDao.deleteAll();
	}

	@DisplayName("searchEtapeOperationsWithNotReferenceUserTest: Recherche d'etapes d'operations par un user non référent")
	@Test
	public void searchEtapeOperationsWithNotReferenceUserTest() {

		// C'est un non-référent qui qui effectue la recherche
		Mockito.when(authentificationHelper.hasReferentRole()).thenReturn(false);

		// recherche par code
		EtapeCriteria etapeCriteria = new EtapeCriteria();

		etapeCriteria.setCode("EN_PROJET_OFF"); // pas accessible au non référent

		Pageable pageable = PaginationUtils.buildPageable(0, 10, "code", true, EtapeOperationEntity.class);

		Page<EtapeRestricted> page = etapeProgrammeService.searchEtapesProgramme(etapeCriteria, pageable);

		assertEquals(0, page.getNumberOfElements(), "Aucun élément ne devrait être retourné");
		assertTrue(page.getContent().isEmpty(), "Aucun élément ne devrait être retourné");

		// Recherche par libelle
		etapeCriteria.setCode(null);
		etapeCriteria.setLibelle("En*");
		page = etapeProgrammeService.searchEtapesProgramme(etapeCriteria, pageable);
		assertEquals(3, page.getNumberOfElements(),
				"Il n'y a que 2 etapes avec le libelle 'En*' qui sont visibles aux users non réferents");

		// Sans filtre
		etapeCriteria.setLibelle(null);
		page = etapeProgrammeService.searchEtapesProgramme(etapeCriteria, pageable);
		assertEquals(6, page.getNumberOfElements(), "Il n' ya que 5 etapes  qui sont visibles aux users non referents");

	}

	@DisplayName("searchEtapeOperationsWithReferenceUserTest: Recherche d'etapes d'operations par un référent")
	@Test
	public void searchEtapeOperationsWithReferenceUserTest() {

		// C'est un référent qui qui effectue la recherche
		Mockito.when(authentificationHelper.hasReferentRole()).thenReturn(true);
		EtapeCriteria etapeCriteria = new EtapeCriteria();

		Pageable pageable = PaginationUtils.buildPageable(0, 10, "code", true, EtapeOperationEntity.class);

		// recherche par code
		etapeCriteria.setCode("EN_PROJET_OFF"); // accessible au référent
		Page<EtapeRestricted> page = etapeProgrammeService.searchEtapesProgramme(etapeCriteria, pageable);

		assertEquals(1, page.getNumberOfElements(), "Un élément devrait être retourné");
		assertFalse(page.getContent().isEmpty(), "Un élément doit être retourné");

		// Recherche par libelle
		etapeCriteria.setCode(null);
		etapeCriteria.setLibelle("En*");
		page = etapeProgrammeService.searchEtapesProgramme(etapeCriteria, pageable);
		assertEquals(5, page.getNumberOfElements(),
				"Toutes les 4 etapes avec le libelle 'En*' sont visibles aux users réferents");

		// Sans filtre
		etapeCriteria.setLibelle(null);
		page = etapeProgrammeService.searchEtapesProgramme(etapeCriteria, pageable);
		assertEquals(page.getTotalElements(), page.getNumberOfElements(),
				"Toutes les etapes sont visibles aux users referents");

	}

}

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
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.tabou.operation.EtapeOperationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

@TestPropertySource(value = { "classpath:application.properties" })
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class EtapeOperationServiceTest {

	@Autowired
	private EtapeOperationService etapeOperationService;

	@MockBean
	private AuthentificationHelper authentificationHelper;

	@Autowired
	private EtapeOperationDao etapeOperationDao;

	private EtapeOperationEntity etape1;
	private EtapeOperationEntity etape2;
	private EtapeOperationEntity etape3;
	private EtapeOperationEntity etape4;
	private EtapeOperationEntity etape5;
	private EtapeOperationEntity etape6;
	private EtapeOperationEntity etape7;
	private EtapeOperationEntity etape8;

	@BeforeEach
	public void init() {

		etape1 = new EtapeOperationEntity();
		etape1.setCode("EN_PROJET_OFF");
		etape1.setLibelle("En projet");
		etape1.setMode("OFF");
		etape1.setType("START");
		etape1.setRemoveRestriction(false);

		etape2 = new EtapeOperationEntity();
		etape2.setCode("EN_ETUDE_OFF");
		etape2.setLibelle("En étude");
		etape2.setMode("OFF");
		etape2.setType("NORMAL");
		etape2.setRemoveRestriction(false);

		etape3 = new EtapeOperationEntity();
		etape3.setCode("ANNULE_OFF");
		etape3.setLibelle("Annulé");
		etape3.setMode("OFF");
		etape3.setType("END");
		etape3.setRemoveRestriction(false);

		etape4 = new EtapeOperationEntity();
		etape4.setCode("EN_PROJET_PUBLIC");
		etape4.setLibelle("En projet");
		etape4.setMode(Etape.ModeEnum.PUBLIC.toString());
		etape4.setType("START");
		etape4.setRemoveRestriction(true);

		etape5 = new EtapeOperationEntity();
		etape5.setCode("EN_ETUDE_PUBLIC");
		etape5.setLibelle("En étude");
		etape5.setMode(Etape.ModeEnum.PUBLIC.toString());
		etape5.setType("NORMAL");
		etape5.setRemoveRestriction(true);

		etape6 = new EtapeOperationEntity();
		etape6.setCode("OPERATIONNEL_PUBLIC");
		etape6.setLibelle("Opérationnel");
		etape6.setMode(Etape.ModeEnum.PUBLIC.toString());
		etape6.setType("NORMAL");
		etape6.setRemoveRestriction(false);

		etape7 = new EtapeOperationEntity();
		etape7.setCode("CLOTURE_PUBLIC");
		etape7.setLibelle("Clôturé");
		etape7.setMode(Etape.ModeEnum.PUBLIC.toString());
		etape7.setType("END");
		etape7.setRemoveRestriction(false);

		etape8 = new EtapeOperationEntity();
		etape8.setCode("ANNULE_PUBLIC");
		etape8.setLibelle("Annulé");
		etape8.setMode(Etape.ModeEnum.PUBLIC.toString());
		etape8.setType("NORMAL");
		etape8.setRemoveRestriction(false);

		etape1 = etapeOperationDao.save(etape1);
		etape2 = etapeOperationDao.save(etape2);
		etape3 = etapeOperationDao.save(etape3);
		etape4 = etapeOperationDao.save(etape4);
		etape5 = etapeOperationDao.save(etape5);
		etape6 = etapeOperationDao.save(etape6);
		etape7 = etapeOperationDao.save(etape7);
		etape8 = etapeOperationDao.save(etape8);
	}

	@AfterEach
	public void after() {
		etapeOperationDao.deleteAll();
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

		Page<EtapeRestricted> page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);

		assertEquals(0, page.getNumberOfElements(), "Aucun élément ne devrait être retourné");
		assertTrue(page.getContent().isEmpty(), "Aucun élément ne devrait être retourné");

		// Recherche par libelle
		etapeCriteria.setCode(null);
		etapeCriteria.setLibelle("En*");
		page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);
		assertEquals(2, page.getNumberOfElements(),
				"Il n'y a que 2 etapes avec le libelle 'En*' qui sont visibles aux users non réferents");

		// Sans filtre
		etapeCriteria.setLibelle(null);
		page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);
		assertEquals(5, page.getNumberOfElements(), "Il n' ya que 5 etapes  qui sont visibles aux users non referents");

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
		Page<EtapeRestricted> page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);

		assertEquals(1, page.getNumberOfElements(), "Un élément devrait être retourné");
		assertFalse(page.getContent().isEmpty(), "Un élément doit être retourné");

		// Recherche par libelle
		etapeCriteria.setCode(null);
		etapeCriteria.setLibelle("En*");
		page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);
		assertEquals(4, page.getNumberOfElements(),
				"Toutes les 4 etapes avec le libelle 'En*' sont visibles aux users réferents");

		// Sans filtre
		etapeCriteria.setLibelle(null);
		page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);
		assertEquals( page.getTotalElements(),
				page.getNumberOfElements(), "Toutes les etapes sont visibles aux users referents");

	}
}

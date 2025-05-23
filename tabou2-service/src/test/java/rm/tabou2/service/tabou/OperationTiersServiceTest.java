package rm.tabou2.service.tabou;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.transaction.annotation.Transactional;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.constant.ConsommationEspaceCode;
import rm.tabou2.service.constant.DecisionCode;
import rm.tabou2.service.constant.MaitriseOuvrageCode;
import rm.tabou2.service.constant.ModeAmenagementCode;
import rm.tabou2.service.constant.VocationCode;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.storage.tabou.dao.operation.ConsommationEspaceDao;
import rm.tabou2.storage.tabou.dao.operation.DecisionDao;
import rm.tabou2.storage.tabou.dao.operation.MaitriseOuvrageDao;
import rm.tabou2.storage.tabou.dao.operation.ModeAmenagementDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.operation.VocationDao;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.operation.ConsommationEspaceEntity;
import rm.tabou2.storage.tabou.entity.operation.DecisionEntity;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;
import rm.tabou2.storage.tabou.entity.operation.ModeAmenagementEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

@TestPropertySource(value = { "classpath:application.properties" })
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class OperationTiersServiceTest {

	private static final String LIBELLE_TYPE_TIERS = "libelle";

	private static final String NOM_TIERS = "nom";

	@Autowired
	private OperationDao operationDao;

	@Autowired
	private OperationTiersService operationTiersService;

	@Autowired
	private DecisionDao decisionDao;

	@Autowired
	private VocationDao vocationDao;

	@Autowired
	private MaitriseOuvrageDao maitriseOuvrageDao;

	@Autowired
	private ModeAmenagementDao modeAmenagementDao;

	@Autowired
	private ConsommationEspaceDao consommationEspaceDao;

	@Autowired
	private TypeTiersDao typeTiersDao;

	@Autowired
	private TiersDao tiersDao;

	@MockitoBean
	private AuthentificationHelper authentificationHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.getConnectedUsername()).thenReturn("username");
        Mockito.when(authentificationHelper.hasReferentRole()).thenReturn(true);
    }

    @AfterTestExecution
    public void afterTest() {
        operationDao.deleteAll();
        tiersDao.deleteAll();
        typeTiersDao.deleteAll();
    }

    public void testSearchTiers() throws AppServiceException {


        DecisionEntity decisionEntity = decisionDao.findByCode(DecisionCode.DELIBERATION_CONSEIL_M);
        VocationEntity vocationEntity = vocationDao.findByCode(VocationCode.ESPACE_VERT);
        MaitriseOuvrageEntity maitriseOuvrageEntity = maitriseOuvrageDao.findByCode(MaitriseOuvrageCode.INTERCOMMUNALE);
        ModeAmenagementEntity modeAmenagementEntity = modeAmenagementDao.findByCode(ModeAmenagementCode.REGIE);
        ConsommationEspaceEntity consommationEspaceEntity = consommationEspaceDao.findByCode(ConsommationEspaceCode.EXTENSION);

        TiersEntity tiers = new TiersEntity();
        tiers.setNom(NOM_TIERS);
        tiersDao.save(tiers);

		TypeTiersEntity typeTiers = new TypeTiersEntity();
		typeTiers.setLibelle(LIBELLE_TYPE_TIERS);
		typeTiersDao.save(typeTiers);

		OperationEntity operationEntity = new OperationEntity();
		operationEntity.setNom("test");
		operationEntity.setDiffusionRestreinte(false);
		operationEntity.setDecision(decisionEntity);
		operationEntity.setVocation(vocationEntity);
		operationEntity.setMaitriseOuvrage(maitriseOuvrageEntity);
		operationEntity.setModeAmenagement(modeAmenagementEntity);
		operationEntity.setConsommationEspace(consommationEspaceEntity);
		operationDao.save(operationEntity);

        try {
            operationTiersService.associateTiersToOperation(operationEntity.getId(), tiers.getId(), typeTiers.getId());
        } catch (AppServiceException e) {
            e.printStackTrace();
        }


        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));
        TiersAmenagementCriteria criteria = new TiersAmenagementCriteria();
        criteria.setLibelle(LIBELLE_TYPE_TIERS);
        criteria.setOperationId(operationEntity.getId());
        criteria.setAsc(true);
        criteria.setOrderBy("nom");

        Page<AssociationTiersTypeTiers> page = null;
        try {
            page = operationTiersService.searchOperationTiers(criteria, pageable);
        } catch (AppServiceException e) {
            throw new AppServiceException("Erreur lors de la recherche de tiers d'operations", e);
        }


        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(NOM_TIERS, page.getContent().get(0).getTiers().getNom());
        Assertions.assertEquals(LIBELLE_TYPE_TIERS, page.getContent().get(0).getTypeTiers().getLibelle());

    }

    @Test
    @Transactional
    public void testSearchTiersDiffusionRestreinte() throws AppServiceException {

        DecisionEntity decisionEntity = decisionDao.findByCode(DecisionCode.DELIBERATION_CONSEIL_M);
        VocationEntity vocationEntity = vocationDao.findByCode(VocationCode.ESPACE_VERT);
        MaitriseOuvrageEntity maitriseOuvrageEntity = maitriseOuvrageDao.findByCode(MaitriseOuvrageCode.INTERCOMMUNALE);
        ModeAmenagementEntity modeAmenagementEntity = modeAmenagementDao.findByCode(ModeAmenagementCode.REGIE);
        ConsommationEspaceEntity consommationEspaceEntity = consommationEspaceDao.findByCode(ConsommationEspaceCode.EXTENSION);

		TiersEntity tiers = new TiersEntity();
		tiers.setNom(NOM_TIERS);
		tiersDao.save(tiers);

		TypeTiersEntity typeTiers = new TypeTiersEntity();
		typeTiers.setLibelle(LIBELLE_TYPE_TIERS);
		typeTiersDao.save(typeTiers);

		OperationEntity operationEntity = new OperationEntity();
		operationEntity.setNom("test");
		operationEntity.setDiffusionRestreinte(true);
		operationEntity.setDecision(decisionEntity);
		operationEntity.setVocation(vocationEntity);
		operationEntity.setMaitriseOuvrage(maitriseOuvrageEntity);
		operationEntity.setModeAmenagement(modeAmenagementEntity);
		operationEntity.setConsommationEspace(consommationEspaceEntity);
		operationDao.save(operationEntity);


        try {
            Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(true);
            operationTiersService.associateTiersToOperation(operationEntity.getId(), tiers.getId(), typeTiers.getId());
        } catch (AppServiceException e) {
            throw new AppServiceException("Erreur lors de la recherche de tiers d'operations", e);
        } finally {
            Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);
        }


        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "libelle"));
        TiersAmenagementCriteria criteria = new TiersAmenagementCriteria();
        criteria.setLibelle(LIBELLE_TYPE_TIERS);
        criteria.setOperationId(operationEntity.getId());
        criteria.setAsc(true);
        criteria.setOrderBy("libelle");


        Page<AssociationTiersTypeTiers> page = null;
        try {
            // l'utilisateur n'as pas le role referent
            Mockito.when(authentificationHelper.hasReferentRole()).thenReturn(false);
            Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);
            page = operationTiersService.searchOperationTiers(criteria, pageable);
        } catch (AppServiceException e) {
            throw new AppServiceException("Erreur lors de la recherche de tiers d'operations", e);
        }


        Assertions.assertEquals(0, page.getTotalElements());


        try {
            // Avec le role referent
            Mockito.when(authentificationHelper.hasReferentRole()).thenReturn(true);
            Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);
            page = operationTiersService.searchOperationTiers(criteria, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(NOM_TIERS, page.getContent().get(0).getTiers().getNom());
        Assertions.assertEquals(LIBELLE_TYPE_TIERS, page.getContent().get(0).getTypeTiers().getLibelle());
    }

}

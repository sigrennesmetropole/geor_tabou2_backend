package rm.tabou2.service.tabou;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.constant.*;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.TiersAmenagement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.storage.tabou.dao.operation.*;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersDao;
import rm.tabou2.storage.tabou.entity.operation.*;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
public class OperationTiersServiceTest {


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

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.getConnectedUsername()).thenReturn("username");
    }

    private static final String LIBELLE_TYPE_TIERS = "libelle";

    private static final String NOM_TIERS = "nom";



    @Test
    public void testSearchTiers() {


        DecisionEntity decisionEntity = decisionDao.findByCode(DecisionCode.DELIBERATION_CONSEIL_M);
        VocationEntity vocationEntity = vocationDao.findByCode(VocationCode.ESPACE_VERT);
        MaitriseOuvrageEntity maitriseOuvrageEntity = maitriseOuvrageDao.findByCode(MaitriseOuvrageCode.COMMUNAUTAIRE);
        ModeAmenagementEntity modeAmenagementEntity = modeAmenagementDao.findByCode(ModeAmenagementCode.REGIE);
        ConsommationEspaceEntity consommationEspaceEntity = consommationEspaceDao.findByCode(ConsommationEspaceCode.EXTENSION);

        TiersEntity tiers = new TiersEntity();
        tiers.setNom(NOM_TIERS);
        tiers.setId(1L);
        tiersDao.save(tiers);

        TypeTiersEntity typeTiers = new TypeTiersEntity();
        typeTiers.setLibelle(LIBELLE_TYPE_TIERS);
        typeTiers.setId(1L);
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


        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        Page<TiersAmenagement> page = null;
        try {
            page = operationTiersService.searchOperationTiers(operationEntity.getId(), LIBELLE_TYPE_TIERS, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals(NOM_TIERS, page.getContent().get(0).getNom());
        Assertions.assertEquals(LIBELLE_TYPE_TIERS, page.getContent().get(0).getLibelle());
    }

}

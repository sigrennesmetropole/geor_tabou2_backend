package rm.tabou2.service.tabou;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.constant.NatureLibelle;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.helper.operation.OperationValidator;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.NatureMapper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.dao.operation.NatureDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@Transactional
class OperationRightsHelperTest extends DatabaseInitializerTest {

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private EtapeOperationDao etapeOperationDao;

    @Autowired
    private NatureDao natureDao;

    @Autowired
    private NatureMapper natureMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private EtapeOperationMapper etapeOperationMapper;

    @MockitoBean
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationValidator operationValidator;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(true);
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        operationDao.deleteAll();
    }

    @DisplayName("testCanCreateOperation: Test de la possibilité de création d'une opération avec le rôle referent")
    @Test
    void testCanCreateOperation() {

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setNom("nom1");
        operation.setDiffusionRestreinte(false);
        operation.setCode("code1");
        operation.setNumAds("numads1");

        Assertions.assertTrue(operationRightsHelper.checkCanCreateOperation(operation));
    }

    @DisplayName("testCannotCreateOperation: Test de l'interdiction de création d'une opération en diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotCreateOperation() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setNom("nom1");
        operation.setDiffusionRestreinte(true);
        operation.setCode("code1");
        operation.setNumAds("numads1");

        Assertions.assertFalse(operationRightsHelper.checkCanCreateOperation(operation));
    }

    @DisplayName("testCanUpdateOperation: Test de la possibilité de modification d'une opération avec le rôle referent, avec diffusion restreinte à true " +
            "et en affectant une étape en non diffusion restreinte")
    @Test
    void testCanUpdateOperation() {

        EtapeOperationEntity etapeOperationEntityRestreint = etapeOperationDao.findByCode("EN_PROJET_OFF");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");
        operationEntity.setSecteur(true);
        operationEntity.setEtapeOperation(etapeOperationEntityRestreint);

        operationDao.save(operationEntity);

        EtapeOperationEntity etapeOperationEntityNonRestreint = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setId(operationEntity.getId());
        operation.setNom(operationEntity.getNom());
        operation.setCode(operationEntity.getCode());
        operation.setNumAds(operationEntity.getNumAds());
        operation.setSecteur(true);
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntityNonRestreint));

        Assertions.assertTrue(operationRightsHelper.checkCanUpdateOperation(operation, operationMapper.entityToDto(operationEntity)));
    }

    @DisplayName("testCannotUpdateOperationWithRoleConsultation: Test de l'interdiction de modification " +
            "d'une opération avec le rôle consultation")
    @Test
    void testCannotUpdateOperationWithRoleConsultation() {

        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(false);
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        EtapeOperationEntity etapeOperationEntityPublic = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");
        operationEntity.setSecteur(true);
        operationEntity.setEtapeOperation(etapeOperationEntityPublic);

        operationDao.save(operationEntity);

        EtapeOperationEntity etapeOperationEntityEtude = etapeOperationDao.findByCode("EN_ETUDE_PUBLIC");

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setId(operationEntity.getId());
        operation.setNom(operationEntity.getNom());
        operation.setCode(operationEntity.getCode());
        operation.setNumAds(operationEntity.getNumAds());
        operation.setSecteur(operationEntity.getSecteur());
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntityEtude));

        Assertions.assertFalse(operationRightsHelper.checkCanUpdateOperation(operation, operationMapper.entityToDto(operationEntity)));
    }

    @DisplayName("testCanUpdateOperation: Test de l'interdiction de modification d'une opération avec le rôle referent, " +
            "et en modifiant le paramètre esecteur")
    @Test
    void testCannotUpdateOperationWithSecteurChanged() {

        EtapeOperationEntity etapeOperationEntityRestreint = etapeOperationDao.findByCode("EN_PROJET_OFF");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");
        operationEntity.setSecteur(true);
        operationEntity.setEtapeOperation(etapeOperationEntityRestreint);

        operationDao.save(operationEntity);

        EtapeOperationEntity etapeOperationEntityNonRestreint = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setId(operationEntity.getId());
        operation.setNom(operationEntity.getNom());
        operation.setCode(operationEntity.getCode());
        operation.setNumAds(operationEntity.getNumAds());
        operation.setSecteur(false);
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntityNonRestreint));

        Assertions.assertThrows(AppServiceException.class, () -> operationValidator.validateUpdateOperation(operation, operationMapper.entityToDto(operationEntity)));
    }

    @DisplayName("testCanUpdateOperation: Test de l'interdiction de modification d'une opération avec le rôle referent, " +
            "et en modifiant la nature")
    @Test
    void testCannotUpdateOperationWithNatureChanged() {

        EtapeOperationEntity etapeOperationEntityRestreint = etapeOperationDao.findByCode("EN_PROJET_OFF");
        NatureEntity natureEntityZAC = natureDao.findByLibelle(NatureLibelle.ZAC);

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");
        operationEntity.setNature(natureEntityZAC);
        operationEntity.setSecteur(true);
        operationEntity.setEtapeOperation(etapeOperationEntityRestreint);

        operationDao.save(operationEntity);

        EtapeOperationEntity etapeOperationEntityNonRestreint = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");
        NatureEntity natureEntityZA = natureDao.findByLibelle(NatureLibelle.ZA);

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setId(operationEntity.getId());
        operation.setNom(operationEntity.getNom());
        operation.setCode(operationEntity.getCode());
        operation.setNumAds(operationEntity.getNumAds());
        operation.setNature(natureMapper.entityToDto(natureEntityZA));
        operation.setSecteur(true);
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntityNonRestreint));

        Assertions.assertThrows(AppServiceException.class, () -> operationValidator.validateUpdateOperation(operation, operationMapper.entityToDto(operationEntity)));
    }

    @DisplayName("testCannotUpdateOperationWithUpdateDiffusionRestreinte: Test de l'interdiction de modification d'une opération avec le rôle contributeur " +
            "dont la diffusion restreinte est true")
    @Test
    void testCannotUpdateOperationWithUpdateDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        EtapeOperationEntity etapeOperationEntityRestreint = etapeOperationDao.findByCode("EN_PROJET_OFF");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");
        operationEntity.setSecteur(true);
        operationEntity.setEtapeOperation(etapeOperationEntityRestreint);

        operationDao.save(operationEntity);

        EtapeOperationEntity etapeOperationEntityNonRestreint = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setId(operationEntity.getId());
        operation.setNom(operationEntity.getNom());
        operation.setCode(operationEntity.getCode());
        operation.setNumAds(operationEntity.getNumAds());
        operation.setSecteur(true);
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntityNonRestreint));

        Assertions.assertFalse(operationRightsHelper.checkCanUpdateOperation(operation, operationMapper.entityToDto(operationEntity)));
    }

    @DisplayName("testCannotUpdateOperationWithInnaccessibleEtape: Test de l'interdiction de modification d'une opération " +
            "sans diffusion restreinte avec le rôle contributeur et une étape innacessible")
    @Test
    void testCannotUpdateOperationWithInnaccessibleEtape() {

        EtapeOperationEntity etapeOperationEntityEnProjet = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");
        operationEntity.setSecteur(true);
        operationEntity.setEtapeOperation(etapeOperationEntityEnProjet);

        operationDao.save(operationEntity);

        EtapeOperationEntity etapeOperationEntityEnChantier = etapeOperationDao.findByCode("OPERATIONNEL_PUBLIC");

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setId(operationEntity.getId());
        operation.setNom(operationEntity.getNom());
        operation.setCode(operationEntity.getCode());
        operation.setNumAds(operationEntity.getNumAds());
        operation.setSecteur(true);
        operation.setEtape(etapeOperationMapper.entityToDto(etapeOperationEntityEnChantier));

        Assertions.assertThrows(AppServiceException.class, () -> operationValidator.validateUpdateOperation(operation, operationMapper.entityToDto(operationEntity)));
    }

    @DisplayName("testCanGetEtapesForOperationDiffusionRestreinte: Test de la possibilité de récupérer la liste des étapes possibles pour une opération " +
            "en diffusion restreinte avec le rôle referent")
    @Test
    void testCanGetEtapesForOperationDiffusionRestreinte() {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");

        operationDao.save(operationEntity);

        Assertions.assertTrue(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }

    @DisplayName("testCanGetEtapesForOperationNonDiffusionRestreinte: Test de la possibilité de récupérer la liste " +
            "des étapes possibles pour une opération non diffusion restreinte avec le rôle contributeur")
    @Test
    void testCanGetEtapesForOperationNonDiffusionRestreinte() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom1");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity.setCode("code1");
        operationEntity.setNumAds("numads1");

        operationDao.save(operationEntity);

        Assertions.assertTrue(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }

    @DisplayName("testCannotGetEtapesForOperation: Test de l'interdiction de récupérer la liste des étapes possibles pour une opération " +
            "en diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotGetEtapesForOperation() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom4");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code4");
        operationEntity.setNumAds("numads4");

        operationDao.save(operationEntity);

        Assertions.assertFalse(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }

    @DisplayName("testCanGetOperation: Test de la possibilité de récupérer une opération " +
            "en diffusion restreinte avec le rôle restreint")
    @Test
    void testCanGetOperation() {

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom4");
        operationEntity.setDiffusionRestreinte(false);
        operationEntity.setCode("code4");
        operationEntity.setNumAds("numads4");

        operationDao.save(operationEntity);

        Assertions.assertTrue(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }

    @DisplayName("testCannotGetEtapesForOperation: Test de l'interdiction de récupérer une opération " +
            "en diffusion restreinte avec le rôle contributeur")
    @Test
    void testCannotGetOperation() {

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom4");
        operationEntity.setDiffusionRestreinte(true);
        operationEntity.setCode("code4");
        operationEntity.setNumAds("numads4");

        operationDao.save(operationEntity);

        Assertions.assertFalse(operationRightsHelper.checkCanGetEtapesForOperation(operationEntity.getId()));
    }

}

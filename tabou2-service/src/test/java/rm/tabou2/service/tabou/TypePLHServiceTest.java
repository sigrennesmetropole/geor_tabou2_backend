package rm.tabou2.service.tabou;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.tabou.plh.PLHService;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.entity.plh.TypeAttributPLH;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import java.time.Instant;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class TypePLHServiceTest extends DatabaseInitializerTest implements ExceptionTest {

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private TypePLHDao typePLHDao;

    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private PLHService plhService;

    @MockitoBean
    private ProgrammeRightsHelper programmeRightsHelper;

    @MockitoBean
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private PermisConstruireDao permisConstruireDao;

    @BeforeEach
    public void initTest() {
        Mockito.when(programmeRightsHelper.checkCanGetProgramme(Mockito.any(Programme.class))).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanGetProgramme(Mockito.any(ProgrammeEntity.class))).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanCreateProgramme(Mockito.any())).thenReturn(true);
        Mockito.when(programmeRightsHelper.checkCanUpdateProgramme(Mockito.any(), Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(true);
    }

    @AfterTestExecution
    public void afterTest() {
        typePLHDao.deleteAll();
        programmeDao.deleteAll();
    }

    @DisplayName("testCreateTypePLH: test de création de types PLH")
    @Test
    @Transactional
    void testCreateTypePLH() throws AppServiceException {
        TypePLH typePLH1 = new TypePLH();
        typePLH1.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.CATEGORY);
        typePLH1.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLH1.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLH1.setSelectionnable(true);
        typePLH1.setOrder(0);
        typePLH1.setLibelle("num1");

        TypePLH typePLH2 = new TypePLH();
        typePLH2.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.VALUE);
        typePLH2.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLH2.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLH2.setSelectionnable(true);
        typePLH2.setOrder(0);
        typePLH2.setLibelle("num2");

        TypePLH plh1 = plhService.createTypePLH(typePLH1);
        TypePLH plh2 = plhService.createTypePLHWithParent(typePLH2, plh1.getId());
        TypePLH plh1ApresFils = plhService.getTypePLH(plh1.getId());

        Assertions.assertEquals("num1", plh1.getLibelle());
        Assertions.assertEquals(TypePLH.TypeAttributPLHEnum.CATEGORY, plh1.getTypeAttributPLH());
        Assertions.assertEquals(TypePLH.TypeAttributPLHEnum.VALUE, plh2.getTypeAttributPLH());
        Assertions.assertEquals(1, plh1ApresFils.getFils().size());
        Assertions.assertEquals(plh2.getId(), plh1ApresFils.getFils().get(0).getId());
    }

    @DisplayName("testCreateTypePLHEnCascade : test de création de types PLH avec ses fils")
    @Test
    @Transactional
    void testCreateTypePLHEnCascade() throws AppServiceException {
        TypePLH typePLH1 = new TypePLH();
        typePLH1.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.CATEGORY);
        typePLH1.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLH1.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLH1.setSelectionnable(true);
        typePLH1.setOrder(0);
        typePLH1.setLibelle("num1");

        TypePLH typePLH2 = new TypePLH();
        typePLH2.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.VALUE);
        typePLH2.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLH2.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLH2.setSelectionnable(true);
        typePLH2.setOrder(0);
        typePLH2.setLibelle("num2");

        typePLH1.addFilsItem(typePLH2);

        TypePLH plh1 = plhService.createTypePLH(typePLH1);
        TypePLH plh1ApresFils = plhService.getTypePLH(plh1.getId());

        Assertions.assertEquals(TypePLH.TypeAttributPLHEnum.CATEGORY, plh1.getTypeAttributPLH());
        Assertions.assertEquals(1, plh1ApresFils.getFils().size());
        Assertions.assertEquals(typePLH2.getLibelle(), plhService.getTypePLH(plh1.getId()).getFils().get(0).getLibelle());
    }

    @DisplayName("testGetListPLHProgramme: test de récupération de types PLH d'un programme")
    @Test
    @Transactional
    void testGetTypePLHProgramme() throws AppServiceException {
        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");
        programmeDao.save(programmeEntity);

        PermisConstruireEntity pc = new PermisConstruireEntity();
        pc.setDatDate(Date.from(Instant.now()));
        pc.setDocDate(Date.from(Instant.now()));
        pc.setNumAds("numads1");
        pc.setDecision("Accordé");
        permisConstruireDao.save(pc);

        TypePLHEntity typePLHEntity1 = new TypePLHEntity();
        typePLHEntity1.setTypeAttributPLH(TypeAttributPLH.CATEGORY);
        typePLHEntity1.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLHEntity1.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLHEntity1.setSelectionnable(true);
        typePLHEntity1.setOrder(0);
        typePLHEntity1.setLibelle("num1");
        typePLHDao.save(typePLHEntity1);

        TypePLHEntity typePLHEntity2 = new TypePLHEntity();
        typePLHEntity2.setTypeAttributPLH(TypeAttributPLH.VALUE);
        typePLHEntity2.setDateDebut(Date.from(Instant.now().minus(2, DAYS)));
        typePLHEntity2.setDateFin(Date.from(Instant.now().minus(1, DAYS)));
        typePLHEntity2.setSelectionnable(true);
        typePLHEntity2.setOrder(0);
        typePLHEntity2.setLibelle("num2");
        typePLHDao.save(typePLHEntity2);

        TypePLH plh1 = programmeService.addPLHProgrammeById(programmeEntity.getId(), typePLHEntity1.getId());
        Assertions.assertThrows(AppServiceException.class,
                () -> programmeService.addPLHProgrammeById(programmeEntity.getId(), typePLHEntity2.getId()));

        Assertions.assertEquals(plh1, programmeService.getPLHProgramme(programmeEntity.getId(), plh1.getId()));
        Assertions.assertEquals(TypePLH.TypeAttributPLHEnum.CATEGORY, plh1.getTypeAttributPLH());
    }

    @DisplayName("testUpdatePLHProgramme: Test de la mise à jour d'un type plh dans un programme")
    @Test
    @Transactional
    void testUpdatePLHProgramme() throws AppServiceException {
        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");

        programmeEntity = programmeDao.save(programmeEntity);

        TypePLHEntity typePLHEntity1 = new TypePLHEntity();
        typePLHEntity1.setTypeAttributPLH(TypeAttributPLH.VALUE);
        typePLHEntity1.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLHEntity1.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLHEntity1.setSelectionnable(true);
        typePLHEntity1.setOrder(0);
        typePLHEntity1.setLibelle("num1");
        typePLHEntity1 = typePLHDao.save(typePLHEntity1);
        TypePLH plh1 = programmeService.addPLHProgrammeById(programmeEntity.getId(), typePLHEntity1.getId());

        plh1.setValue("value1");
        plh1 = programmeService.updatePLHProgramme(programmeEntity.getId(), plh1);
        TypePLH plh2 = programmeService.getPLHProgramme(programmeEntity.getId(), plh1.getId());

        Assertions.assertEquals("value1", plh2.getValue());
    }

    @DisplayName("testRemovePLHProgramme: Test de la suppression d'un type plh dans un programme")
    @Test
    @Transactional
    void testRemovePLHProgramme() throws AppServiceException {
        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setNom("nom1");
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity.setCode("code1");
        programmeEntity.setNumAds("numads1");
        programmeEntity = programmeDao.save(programmeEntity);
        final long programmeId = programmeEntity.getId();

        TypePLHEntity typePLHEntity1 = new TypePLHEntity();
        typePLHEntity1.setTypeAttributPLH(TypeAttributPLH.CATEGORY);
        typePLHEntity1.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLHEntity1.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLHEntity1.setLibelle("num1");
        typePLHEntity1.setSelectionnable(true);
        typePLHEntity1.setOrder(0);
        typePLHDao.save(typePLHEntity1);
        TypePLH plh1 = programmeService.addPLHProgrammeById(programmeId, typePLHEntity1.getId());

        programmeService.removePLHProgrammeById(programmeEntity.getId(), typePLHEntity1.getId());

        Assertions.assertThrows(AppServiceException.class, () -> programmeService.getPLHProgramme(programmeId, plh1.getId()));
    }

    @DisplayName("testCannotCreatePLHWithInvalidParameters: Test de l'interdiction de la création" +
            " d'un type PLH avec des paramètres obligatoires non présents")
    @Test
    @Transactional
    void testCannotCreatePLHWithInvalidParameters() {
        final TypePLH typePLH = new TypePLH();
        Assertions.assertThrows(AppServiceException.class, () -> plhService.createTypePLH(typePLH));
    }

    @DisplayName("testSearchParentById: Test de la recherche d'un parent à partir de l'id de son fils" +
            " avec un cas de réussite et un cas d'erreur")
    @Test
    void searchParentById() throws AppServiceException {
        TypePLH typePLH1 = new TypePLH();
        typePLH1.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.CATEGORY);
        typePLH1.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLH1.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLH1.setSelectionnable(true);
        typePLH1.setOrder(0);
        typePLH1.setLibelle("num1");

        TypePLH typePLH2 = new TypePLH();
        typePLH2.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.VALUE);
        typePLH2.setDateDebut(Date.from(Instant.now().minus(1, DAYS)));
        typePLH2.setDateFin(Date.from(Instant.now().plus(1, DAYS)));
        typePLH2.setSelectionnable(true);
        typePLH2.setOrder(0);
        typePLH2.setLibelle("num2");

        TypePLH plh1 = plhService.createTypePLH(typePLH1);
        TypePLH plh2 = plhService.createTypePLHWithParent(typePLH2, plh1.getId());
        TypePLH plh1AvecFils = plhService.getTypePLH(plh1.getId());

        TypePLH parent = plhService.searchTypePLHParent(plh2.getId());
        Assertions.assertNotNull(parent);
        Assertions.assertEquals("num1", parent.getLibelle());

        long id = plh1AvecFils.getId();
        Assertions.assertThrows(NullPointerException.class, () ->  plhService.searchTypePLHParent(id));
    }
}

package rm.tabou2.service.tabou;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.dto.TypePLHBean;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.plh.TypeAttributPLH;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

@TestPropertySource(value = { "classpath:application.properties" })
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class OperationPLHServiceTest extends DatabaseInitializerTest implements ExceptionTest {

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private TypePLHDao typePLHDao;

    @Autowired
    private OperationService operationService;

    @MockitoBean
    private OperationRightsHelper operationRightsHelper;

    @BeforeEach
    void initialiserTest() {
        Mockito.when(operationRightsHelper.checkCanGetOperation(Mockito.any(OperationEntity.class))).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanGetOperation(Mockito.any(OperationIntermediaire.class)))
                .thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanUpdateOperation(Mockito.any(), Mockito.any())).thenReturn(true);
    }

    @DisplayName("testGetPLHOperation: Récupération d'un type PLH rattaché à une opération")
    @Test
    void testGetPLHOperation() throws AppServiceException {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom1");
        TypePLHEntity typePLHEntity = construireEtSauvegarderTypePLHEntity("libellePLH", TypeAttributPLH.VALUE, null);

        rattacherTypePLHAOperation(operationEntity, typePLHEntity);

        TypePLH typePLHRecupere = operationService.getPLHOperation(operationEntity.getId(), typePLHEntity.getId());

        Assertions.assertNotNull(typePLHRecupere);
        Assertions.assertEquals(typePLHEntity.getId(), typePLHRecupere.getId());
        Assertions.assertEquals("libellePLH", typePLHRecupere.getLibelle());
    }

    @DisplayName("testGetPLHOperationSansPLHRattache: Récupération d'un type PLH sur une opération sans PLH doit lever une exception")
    @Test
    void testGetPLHOperationSansPLHRattache() {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom2");

        long operationId = operationEntity.getId();
        Assertions.assertThrows(AppServiceException.class,
                () -> operationService.getPLHOperation(operationId, 999L));
    }

    @DisplayName("testUpdatePLHOperation: Mise à jour d'une valeur PLH sur une opération")
    @Test
    void testUpdatePLHOperation() throws AppServiceException {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom3");
        TypePLHEntity typePLHEntity = construireEtSauvegarderTypePLHEntity("libellePLH2", TypeAttributPLH.VALUE, null);

        rattacherTypePLHAOperation(operationEntity, typePLHEntity);

        TypePLH typePLHAMettreAJour = new TypePLH();
        typePLHAMettreAJour.setId(typePLHEntity.getId());
        typePLHAMettreAJour.setValue("nouvelleValeur");
        typePLHAMettreAJour.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.VALUE);

        TypePLH typePLHMisAJour = operationService.updatePLHOperation(
                operationEntity.getId(), typePLHEntity.getId(), typePLHAMettreAJour);

        Assertions.assertNotNull(typePLHMisAJour);
        Assertions.assertEquals("nouvelleValeur", typePLHMisAJour.getValue());
    }

    @DisplayName("testUpdatePLHOperationAvecSyncField: Mise à jour d'une valeur PLH avec syncField doit reporter la valeur sur le champ de l'opération")
    @Test
    void testUpdatePLHOperationAvecSyncField() throws AppServiceException {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nomOriginal");
        TypePLHEntity typePLHEntity = construireEtSauvegarderTypePLHEntity("libellePLH3", TypeAttributPLH.VALUE, "nom");

        rattacherTypePLHAOperation(operationEntity, typePLHEntity);

        TypePLH typePLHAMettreAJour = new TypePLH();
        typePLHAMettreAJour.setId(typePLHEntity.getId());
        typePLHAMettreAJour.setValue("nomMisAJourViaPLH");
        typePLHAMettreAJour.setTypeAttributPLH(TypePLH.TypeAttributPLHEnum.VALUE);

        operationService.updatePLHOperation(
                operationEntity.getId(), typePLHEntity.getId(), typePLHAMettreAJour);

        OperationEntity operationEntityMiseAJour = operationDao.findOneById(operationEntity.getId());
        Assertions.assertEquals("nomMisAJourViaPLH", operationEntityMiseAJour.getNom());
    }

    @DisplayName("testUpdatePLHOperationSansPLHRattache: Mise à jour d'un type PLH sur une opération sans PLH doit lever une exception")
    @Test
    void testUpdatePLHOperationSansPLHRattache() {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom4");

        TypePLH typePLHAMettreAJour = new TypePLH();
        typePLHAMettreAJour.setValue("valeur");

        long operationId = operationEntity.getId();
        Assertions.assertThrows(AppServiceException.class,
                () -> operationService.updatePLHOperation(operationId, 999L, typePLHAMettreAJour));
    }

    @DisplayName("testRemovePLHOperationById: Suppression du lien entre une opération et un type PLH")
    @Test
    void testRemovePLHOperationById() throws AppServiceException {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom5");
        TypePLHEntity typePLHEntity = construireEtSauvegarderTypePLHEntity("libellePLH4", TypeAttributPLH.VALUE, null);
        rattacherTypePLHAOperation(operationEntity, typePLHEntity);

        operationService.removePLHOperationById(operationEntity.getId(), typePLHEntity.getId());

        // ✅ On vérifie via le service (reste dans une transaction)
        // au lieu d'accéder directement à la collection lazy
        Assertions.assertThrows(AppServiceException.class,
                () -> operationService.getPLHOperation(operationEntity.getId(), typePLHEntity.getId()));
    }

    @DisplayName("testRemovePLHOperationByIdTypePLHAbsent: Suppression d'un type PLH absent doit lever une exception")
    @Test
    void testRemovePLHOperationByIdTypePLHAbsent() {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom6");

        long operationId = operationEntity.getId();
        Assertions.assertThrows(AppServiceException.class,
                () -> operationService.removePLHOperationById(operationId, 999L));
    }

    @DisplayName("testAddPLHOperationById: Ajout d'un type PLH à une opération")
    @Test
    void testAddPLHOperationById() throws AppServiceException {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom7");
        TypePLHEntity typePLHEntity = construireEtSauvegarderTypePLHEntity("libellePLH5", TypeAttributPLH.VALUE, null);

        TypePLH result = operationService.addPLHOperationById(operationEntity.getId(), typePLHEntity.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(typePLHEntity.getId(), result.getId());
        Assertions.assertEquals("libellePLH5", result.getLibelle());
    }

    @DisplayName("testAddPLHOperationByIdDejaPresent: Ajout d'un type PLH déjà rattaché doit lever une exception")
    @Test
    void testAddPLHOperationByIdDejaPresent() {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom8");
        TypePLHEntity typePLHEntity = construireEtSauvegarderTypePLHEntity("libellePLH6", TypeAttributPLH.VALUE, null);
        rattacherTypePLHAOperation(operationEntity, typePLHEntity);

        long operationId = operationEntity.getId();
        long typePLHId = typePLHEntity.getId();
        Assertions.assertThrows(AppServiceException.class,
                () -> operationService.addPLHOperationById(operationId, typePLHId));
    }

    @DisplayName("testAddPLHOperationByIdNonSelectionnable: Ajout d'un type PLH non sélectionnable doit lever une exception")
    @Test
    void testAddPLHOperationByIdNonSelectionnable() {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom9");
        TypePLHEntity typePLHEntity = construireEtSauvegarderTypePLHEntity("libellePLH7", TypeAttributPLH.VALUE, null);
        typePLHEntity.setSelectionnable(false);
        typePLHDao.save(typePLHEntity);

        long operationId = operationEntity.getId();
        long typePLHId = typePLHEntity.getId();
        Assertions.assertThrows(AppServiceException.class,
                () -> operationService.addPLHOperationById(operationId, typePLHId));
    }

    @DisplayName("testGetPLHsOperation: Récupération de la liste des types PLH d'une opération")
    @Test
    void testGetPLHsOperation() throws AppServiceException {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom10");
        TypePLHEntity typePLH1 = construireEtSauvegarderTypePLHEntity("plhA", TypeAttributPLH.VALUE, null);
        TypePLHEntity typePLH2 = construireEtSauvegarderTypePLHEntity("plhB", TypeAttributPLH.VALUE, null);
        rattacherTypePLHAOperation(operationEntity, typePLH1);
        rattacherTypePLHAOperation(operationEntity, typePLH2);

        List<TypePLHBean> result = operationService.getPLHsOperation(operationEntity.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @DisplayName("testGetPLHsOperationVide: Récupération d'une liste vide si aucun PLH rattaché")
    @Test
    void testGetPLHsOperationVide() throws AppServiceException {
        OperationEntity operationEntity = construireEtSauvegarderOperationEntity("nom11");

        List<TypePLHBean> result = operationService.getPLHsOperation(operationEntity.getId());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    // ==================== méthodes utilitaires ====================

    private OperationEntity construireEtSauvegarderOperationEntity(String nom) {
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom(nom);
        operationEntity.setDiffusionRestreinte(false);
        operationEntity.setPlhs(new HashSet<>());
        return operationDao.save(operationEntity);
    }

    private TypePLHEntity construireEtSauvegarderTypePLHEntity(String libelle, TypeAttributPLH typeAttributPLH,
            String syncField) {
        TypePLHEntity typePLHEntity = new TypePLHEntity();
        typePLHEntity.setLibelle(libelle);
        typePLHEntity.setTypeAttributPLH(typeAttributPLH);
        typePLHEntity.setDateDebut(LocalDateTime.ofInstant(Instant.now().minus(1, DAYS), ZoneOffset.UTC));
        typePLHEntity.setDateFin(LocalDateTime.ofInstant(Instant.now().plus(1, DAYS), ZoneOffset.UTC));
        typePLHEntity.setSelectionnable(true);
        typePLHEntity.setOrder(0);
        typePLHEntity.setSynchronizedField(syncField);
        return typePLHDao.save(typePLHEntity);
    }

    private void rattacherTypePLHAOperation(OperationEntity operationEntity, TypePLHEntity typePLHEntity) {
        try {
            operationService.addPLHOperationById(operationEntity.getId(), typePLHEntity.getId());
        } catch (AppServiceException e) {
            throw new IllegalStateException("Impossible de rattacher le type PLH à l'opération", e);
        }
    }
}
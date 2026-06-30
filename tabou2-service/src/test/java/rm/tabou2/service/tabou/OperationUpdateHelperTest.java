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
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.factory.LogementDataFactory;
import rm.tabou2.service.common.factory.OperationDataFactory;
import rm.tabou2.service.dto.*;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.sig.dao.SecteurDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.logement.LogementsSpecifiquesDao;
import rm.tabou2.storage.tabou.dao.logement.TypeAccessionLogementDao;
import rm.tabou2.storage.tabou.dao.logement.TypeLogementDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class OperationUpdateHelperTest extends DatabaseInitializerTest {

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationDataFactory operationDataFactory;

    @Autowired
    private LogementDataFactory logementDataFactory;

    @Autowired
    private LogementsSpecifiquesDao logementsSpecifiquesDao;

    @Autowired
    private TypeAccessionLogementDao typeAccessionLogementDao;

    @Autowired
    private TypeLogementDao typeLogementDao;

    @Autowired
    private SecteurDao secteurDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @MockitoBean
    private OperationRightsHelper operationRightsHelper;

    @BeforeEach
    void initTest() {
        Mockito.when(operationRightsHelper.checkCanGetOperation(Mockito.any(OperationEntity.class))).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanGetOperation(Mockito.any(OperationIntermediaire.class))).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanCreateOperation(Mockito.any())).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanUpdateOperation(Mockito.any(), Mockito.any())).thenReturn(true);
    }

    @AfterEach
    void afterTest() {
        operationDao.deleteAll();
        logementsSpecifiquesDao.deleteAll();
        typeAccessionLogementDao.deleteAll();
        typeLogementDao.deleteAll();
        secteurDao.deleteAll();
    }

    @DisplayName("testUpdateOperationWithLogementsSpecifiques: Test de la mise à jour d'une opération avec des logements spécifiques")
    @Test
    void testUpdateOperationWithLogementsSpecifiques() throws AppServiceException {

        // Créer l'opération de base
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        operation = operationService.createOperation(operation);
        Assertions.assertNotNull(operation.getId());

        // Créer un type d'accession logement
        TypeAccessionLogement typeAccession = logementDataFactory.createDefaultTypeAccessionLogementForOperation();

        // Créer un type logement
        TypeLogement typeLogement = logementDataFactory.createDefaultTypeLogement();

        // Construire les logements spécifiques
        LogementSpecifique logement = new LogementSpecifique();
        logement.setTypeLogement(typeLogement);
        logement.setValeurPrevue(50);
        logement.setValeurRealisee(30);

        LogementsSpecifiques logementsSpec = new LogementsSpecifiques();
        logementsSpec.setTypeAccessionLogement(typeAccession);
        logementsSpec.setValeur(75);
        logementsSpec.setLogements(List.of(logement));

        // Mettre à jour l'opération avec les logements spécifiques
        operation.setLogementsSpecifiques(List.of(logementsSpec));
        OperationIntermediaire updated = operationService.updateOperation(operation);

        // Vérifications
        Assertions.assertNotNull(updated.getLogementsSpecifiques());
        Assertions.assertEquals(1, updated.getLogementsSpecifiques().size());

        LogementsSpecifiques savedLS = updated.getLogementsSpecifiques().get(0);
        Assertions.assertNotNull(savedLS.getTypeAccessionLogement());
        Assertions.assertEquals(typeAccession.getId(), savedLS.getTypeAccessionLogement().getId());
        Assertions.assertEquals(75, savedLS.getValeur());
        Assertions.assertEquals(1, savedLS.getLogements().size());
        Assertions.assertEquals(50, savedLS.getLogements().get(0).getValeurPrevue());
        Assertions.assertEquals(30, savedLS.getLogements().get(0).getValeurRealisee());
    }

    @DisplayName("testUpdateOperationRemoveLogementsSpecifiques: Test de la suppression de logements spécifiques d'une opération")
    @Test
    void testUpdateOperationRemoveLogementsSpecifiques() throws AppServiceException {

        // Créer l'opération avec un logement spécifique
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        operation = operationService.createOperation(operation);

        TypeAccessionLogement typeAccession = logementDataFactory.createDefaultTypeAccessionLogementForOperation();

        LogementsSpecifiques logementsSpec = new LogementsSpecifiques();
        logementsSpec.setTypeAccessionLogement(typeAccession);
        logementsSpec.setValeur(50);
        logementsSpec.setLogements(new ArrayList<>());

        operation.setLogementsSpecifiques(List.of(logementsSpec));
        operation = operationService.updateOperation(operation);

        Assertions.assertEquals(1, operation.getLogementsSpecifiques().size());

        // Supprimer les logements spécifiques (liste vide)
        operation.setLogementsSpecifiques(new ArrayList<>());
        OperationIntermediaire updated = operationService.updateOperation(operation);

        // Vérifications
        Assertions.assertNotNull(updated.getLogementsSpecifiques());
        Assertions.assertTrue(updated.getLogementsSpecifiques().isEmpty());
    }

    @DisplayName("testUpdateOperationModifyLogementsSpecifiques: Test de la modification d'un logement spécifique existant")
    @Test
    void testUpdateOperationModifyLogementsSpecifiques() throws AppServiceException {

        // Créer l'opération avec un logement spécifique
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        operation = operationService.createOperation(operation);

        TypeAccessionLogement typeAccession = logementDataFactory.createDefaultTypeAccessionLogementForOperation();

        LogementsSpecifiques logementsSpec = new LogementsSpecifiques();
        logementsSpec.setTypeAccessionLogement(typeAccession);
        logementsSpec.setValeur(50);
        logementsSpec.setLogements(new ArrayList<>());

        operation.setLogementsSpecifiques(List.of(logementsSpec));
        operation = operationService.updateOperation(operation);

        // Modifier la valeur du logement spécifique existant
        LogementsSpecifiques existingLS = operation.getLogementsSpecifiques().get(0);
        existingLS.setValeur(100);
        operation.setLogementsSpecifiques(List.of(existingLS));

        OperationIntermediaire updated = operationService.updateOperation(operation);

        // Vérifications
        Assertions.assertEquals(1, updated.getLogementsSpecifiques().size());
        Assertions.assertEquals(100, updated.getLogementsSpecifiques().get(0).getValeur());
    }

    @DisplayName("testUpdateOperationAddLogementSpecifiqueChild: Test de l'ajout d'un enfant LogementSpecifique à un LogementsSpecifiques existant")
    @Test
    void testUpdateOperationAddLogementSpecifiqueChild() throws AppServiceException {

        // Créer l'opération avec un logement spécifique sans enfant
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        operation = operationService.createOperation(operation);

        TypeAccessionLogement typeAccession = logementDataFactory.createDefaultTypeAccessionLogementForOperation();
        TypeLogement typeLogement = logementDataFactory.createDefaultTypeLogement();

        LogementsSpecifiques logementsSpec = new LogementsSpecifiques();
        logementsSpec.setTypeAccessionLogement(typeAccession);
        logementsSpec.setValeur(50);
        logementsSpec.setLogements(new ArrayList<>());

        operation.setLogementsSpecifiques(List.of(logementsSpec));
        operation = operationService.updateOperation(operation);

        Assertions.assertTrue(operation.getLogementsSpecifiques().get(0).getLogements().isEmpty());

        // Ajouter un enfant LogementSpecifique au LogementsSpecifiques existant
        LogementsSpecifiques existingLS = operation.getLogementsSpecifiques().get(0);
        LogementSpecifique newChild = new LogementSpecifique();
        newChild.setTypeLogement(typeLogement);
        newChild.setValeurPrevue(40);
        newChild.setValeurRealisee(20);
        existingLS.setLogements(List.of(newChild));

        operation.setLogementsSpecifiques(List.of(existingLS));
        OperationIntermediaire updated = operationService.updateOperation(operation);

        // Vérifications
        Assertions.assertEquals(1, updated.getLogementsSpecifiques().size());
        List<LogementSpecifique> children = updated.getLogementsSpecifiques().get(0).getLogements();
        Assertions.assertEquals(1, children.size());
        Assertions.assertEquals(40, children.get(0).getValeurPrevue());
        Assertions.assertEquals(20, children.get(0).getValeurRealisee());
        Assertions.assertEquals(typeLogement.getId(), children.get(0).getTypeLogement().getId());
    }

    @DisplayName("testUpdateOperationModifyLogementSpecifiqueChild: Test de la modification d'un enfant LogementSpecifique existant")
    @Test
    void testUpdateOperationModifyLogementSpecifiqueChild() throws AppServiceException {

        // Créer l'opération avec un logement spécifique contenant un enfant
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        operation = operationService.createOperation(operation);

        TypeAccessionLogement typeAccession = logementDataFactory.createDefaultTypeAccessionLogementForOperation();
        TypeLogement typeLogement = logementDataFactory.createDefaultTypeLogement();

        LogementSpecifique child = new LogementSpecifique();
        child.setTypeLogement(typeLogement);
        child.setValeurPrevue(50);
        child.setValeurRealisee(30);

        LogementsSpecifiques logementsSpec = new LogementsSpecifiques();
        logementsSpec.setTypeAccessionLogement(typeAccession);
        logementsSpec.setValeur(75);
        logementsSpec.setLogements(List.of(child));

        operation.setLogementsSpecifiques(List.of(logementsSpec));
        operation = operationService.updateOperation(operation);

        // Modifier les valeurs de l'enfant existant
        LogementsSpecifiques existingLS = operation.getLogementsSpecifiques().get(0);
        LogementSpecifique existingChild = existingLS.getLogements().get(0);
        existingChild.setValeurPrevue(80);
        existingChild.setValeurRealisee(60);
        existingLS.setLogements(List.of(existingChild));

        operation.setLogementsSpecifiques(List.of(existingLS));
        OperationIntermediaire updated = operationService.updateOperation(operation);

        // Vérifications
        List<LogementSpecifique> updatedChildren = updated.getLogementsSpecifiques().get(0).getLogements();
        Assertions.assertEquals(1, updatedChildren.size());
        Assertions.assertEquals(80, updatedChildren.get(0).getValeurPrevue());
        Assertions.assertEquals(60, updatedChildren.get(0).getValeurRealisee());
    }

    @DisplayName("testUpdateOperationRemoveLogementSpecifiqueChild: Test de la suppression d'un enfant LogementSpecifique")
    @Test
    void testUpdateOperationRemoveLogementSpecifiqueChild() throws AppServiceException {

        // Créer l'opération avec un logement spécifique contenant un enfant
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        operation = operationService.createOperation(operation);

        TypeAccessionLogement typeAccession = logementDataFactory.createDefaultTypeAccessionLogementForOperation();
        TypeLogement typeLogement = logementDataFactory.createDefaultTypeLogement();

        LogementSpecifique child = new LogementSpecifique();
        child.setTypeLogement(typeLogement);
        child.setValeurPrevue(50);
        child.setValeurRealisee(30);

        LogementsSpecifiques logementsSpec = new LogementsSpecifiques();
        logementsSpec.setTypeAccessionLogement(typeAccession);
        logementsSpec.setValeur(75);
        logementsSpec.setLogements(List.of(child));

        operation.setLogementsSpecifiques(List.of(logementsSpec));
        operation = operationService.updateOperation(operation);

        Assertions.assertEquals(1, operation.getLogementsSpecifiques().get(0).getLogements().size());

        // Supprimer l'enfant en envoyant une liste vide
        LogementsSpecifiques existingLS = operation.getLogementsSpecifiques().get(0);
        existingLS.setLogements(new ArrayList<>());

        operation.setLogementsSpecifiques(List.of(existingLS));
        OperationIntermediaire updated = operationService.updateOperation(operation);

        // Vérifications
        Assertions.assertEquals(1, updated.getLogementsSpecifiques().size());
        Assertions.assertTrue(updated.getLogementsSpecifiques().get(0).getLogements().isEmpty(),
                "Les enfants LogementSpecifique doivent être supprimés");
    }

    // ===================== Tests auto-initialisation LogementsSpecifiques à la création =====================

    @DisplayName("testCreateOperationAutoInitLogements: à la création d'une opération, les logements spécifiques sont auto-initialisés avec les types actifs")
    @Test
    void testCreateOperationAutoInitLogements() throws AppServiceException {

        // Créer des types actifs en base avec portée OPERATION
        logementDataFactory.createTypeAccessionLogement("LOC_AIDE", "Locatif aidé",
                List.of(TypeAccessionLogement.PorteesEnum.OPERATION));
        logementDataFactory.createTypeAccessionLogement("ACC_SOCIAL", "Accession sociale",
                List.of(TypeAccessionLogement.PorteesEnum.OPERATION));
        // Type avec portée PROGRAMME uniquement (ne doit pas être inclus)
        logementDataFactory.createTypeAccessionLogement("PROG_ONLY", "Programme seulement",
                List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));

        logementDataFactory.createTypeLogement("T2", "T2");
        logementDataFactory.createTypeLogement("T3", "T3");

        // Créer une opération SANS logements spécifiques
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        OperationIntermediaire result = operationService.createOperation(operation);

        // Vérifications : les logements spécifiques doivent avoir été auto-initialisés
        Assertions.assertNotNull(result.getLogementsSpecifiques(),
                "Les logements spécifiques devraient être auto-initialisés");
        Assertions.assertFalse(result.getLogementsSpecifiques().isEmpty(),
                "La liste ne devrait pas être vide");

        // On doit avoir 2 LogementsSpecifiques (un par TypeAccessionLogement actif avec portée OPERATION)
        Assertions.assertEquals(2, result.getLogementsSpecifiques().size(),
                "Il devrait y avoir un LogementsSpecifiques par TypeAccessionLogement actif avec portée OPERATION");

        // Chaque LogementsSpecifiques doit avoir 2 enfants (un par TypeLogement actif)
        for (LogementsSpecifiques ls : result.getLogementsSpecifiques()) {
            Assertions.assertNotNull(ls.getTypeAccessionLogement());
            Assertions.assertEquals(2, ls.getLogements().size(),
                    "Chaque LogementsSpecifiques devrait avoir un LogementSpecifique par TypeLogement actif");
        }

        // Vérifier que le type PROGRAMME_ONLY n'est pas présent
        List<String> codesAccession = result.getLogementsSpecifiques().stream()
                .map(ls -> ls.getTypeAccessionLogement().getCode())
                .toList();
        Assertions.assertFalse(codesAccession.contains("PROG_ONLY"),
                "Le type avec portée PROGRAMME ne devrait pas être inclus dans une opération");
    }

    @DisplayName("testCreateOperationConsultationAfficheInactifs: à la consultation, les éléments inactifs sont aussi affichés")
    @Test
    void testCreateOperationConsultationAfficheInactifs() throws AppServiceException {

        // Créer un type actif
        TypeAccessionLogement typeAccActif = logementDataFactory.createTypeAccessionLogement("ACTIF", "Type actif",
                List.of(TypeAccessionLogement.PorteesEnum.OPERATION));
        logementDataFactory.createTypeLogement("LGT_ACTIF", "Logement actif");

        // Créer une opération (auto-init avec les types actifs)
        OperationIntermediaire operation = operationDataFactory.createBaseOperation();
        final OperationIntermediaire createdOperation = operationService.createOperation(operation);

        Assertions.assertFalse(createdOperation.getLogementsSpecifiques().isEmpty());
        int nbInitial = createdOperation.getLogementsSpecifiques().size();

        // Désactiver le type d'accession logement après la création
        Long typeAccActifId = java.util.Objects.requireNonNull(typeAccActif.getId());
        typeAccessionLogementDao.findById(typeAccActifId).ifPresent(entity -> {
            entity.setDateFin(java.time.LocalDateTime.now());
            typeAccessionLogementDao.save(entity);
        });

        // Vérifier via l'entité en base que les logements spécifiques sont toujours présents
        // (on évite getOperationById qui déclenche computeOperationMos avec st_intersects, indisponible en H2)
        // La lecture se fait dans une transaction pour éviter LazyInitializationException
        Integer nbLogementsEnBase = transactionTemplate.execute(status -> {
            OperationEntity entity = operationDao.findOneById(createdOperation.getId());
            Assertions.assertNotNull(entity);
            Assertions.assertNotNull(entity.getLogementsSpecifiques());
            return entity.getLogementsSpecifiques().size();
        });
        Assertions.assertNotNull(nbLogementsEnBase);
        Assertions.assertEquals(nbInitial, nbLogementsEnBase.intValue(),
                "La consultation doit afficher tous les éléments, y compris les inactifs");
    }
}




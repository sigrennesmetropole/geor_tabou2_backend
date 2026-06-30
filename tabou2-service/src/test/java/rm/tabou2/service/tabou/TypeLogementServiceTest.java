package rm.tabou2.service.tabou;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.factory.LogementDataFactory;
import rm.tabou2.service.dto.TypeLogement;
import rm.tabou2.service.tabou.logement.TypeLogementService;
import rm.tabou2.storage.tabou.dao.logement.TypeLogementDao;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class TypeLogementServiceTest extends DatabaseInitializerTest {

    @Autowired
    private TypeLogementService typeLogementService;

    @Autowired
    private TypeLogementDao typeLogementDao;

    @Autowired
    private LogementDataFactory logementDataFactory;

    @AfterEach
    void afterTest() {
        typeLogementDao.deleteAll();
    }

    @DisplayName("testCreateTypeLogement: création d'un type de logement")
    @Test
    void testCreateTypeLogement() {
        TypeLogement dto = logementDataFactory.buildTypeLogement("SOCIAL", "Social", 1);

        TypeLogement result = typeLogementService.createTypeLogement(dto);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("SOCIAL", result.getCode());
        Assertions.assertEquals("Social", result.getLibelle());
        Assertions.assertEquals(1, result.getOrdre());
    }

    @DisplayName("testGetById: récupération d'un type de logement par identifiant")
    @Test
    void testGetById() {
        TypeLogement dto = logementDataFactory.buildTypeLogement("LIBRE", "Libre", 2);
        TypeLogement created = typeLogementService.createTypeLogement(dto);
        Assertions.assertNotNull(created.getId());

        TypeLogement result = typeLogementService.getById(created.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getId(), result.getId());
        Assertions.assertEquals("LIBRE", result.getCode());
        Assertions.assertEquals("Libre", result.getLibelle());
    }

    @DisplayName("testGetByIdNotFound: récupération d'un type de logement inexistant lève une exception")
    @Test
    void testGetByIdNotFound() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> typeLogementService.getById(999999L)
        );
    }

    @DisplayName("testUpdateTypeLogement: modification d'un type de logement")
    @Test
    void testUpdateTypeLogement() {
        TypeLogement dto = logementDataFactory.buildTypeLogement("INTER", "Intermédiaire", 3);
        TypeLogement created = typeLogementService.createTypeLogement(dto);

        TypeLogement toUpdate = new TypeLogement();
        toUpdate.setId(created.getId());
        toUpdate.setCode("INTER_V2");
        toUpdate.setLibelle("Intermédiaire V2");
        toUpdate.setOrdre(5);
        toUpdate.setDateDebut(OffsetDateTime.now());

        TypeLogement result = typeLogementService.updateTypeLogement(toUpdate);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getId(), result.getId());
        Assertions.assertEquals("INTER_V2", result.getCode());
        Assertions.assertEquals("Intermédiaire V2", result.getLibelle());
        Assertions.assertEquals(5, result.getOrdre());
    }

    @DisplayName("testUpdateTypeLogementWithoutId: mise à jour sans id lève une IllegalArgumentException")
    @Test
    void testUpdateTypeLogementWithoutId() {
        TypeLogement dto = new TypeLogement();
        dto.setCode("CODE");
        dto.setLibelle("Libelle");
        dto.setOrdre(1);
        dto.setDateDebut(OffsetDateTime.now());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> typeLogementService.updateTypeLogement(dto)
        );
    }

    @DisplayName("testUpdateTypeLogementNotFound: mise à jour d'un type inexistant lève une exception")
    @Test
    void testUpdateTypeLogementNotFound() {
        TypeLogement dto = new TypeLogement();
        dto.setId(999999L);
        dto.setCode("CODE");
        dto.setLibelle("Libelle");
        dto.setOrdre(1);
        dto.setDateDebut(OffsetDateTime.now());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> typeLogementService.updateTypeLogement(dto)
        );
    }

    @DisplayName("testInactivateTypeLogement: désactivation d'un type de logement")
    @Test
    void testInactivateTypeLogement() {
        TypeLogement dto = logementDataFactory.buildTypeLogement("AIDE", "Aidé", 4);
        TypeLogement created = typeLogementService.createTypeLogement(dto);

        Assertions.assertNull(created.getDateFin(), "La date de fin devrait être null avant inactivation");

        TypeLogement result = typeLogementService.inactivateTypeLogement(created.getId());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getDateFin(), "La date de fin devrait être renseignée après inactivation");
    }

    @DisplayName("testInactivateTypeLogementNotFound: désactivation d'un type inexistant lève une exception")
    @Test
    void testInactivateTypeLogementNotFound() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> typeLogementService.inactivateTypeLogement(999999L)
        );
    }

    @DisplayName("testSearchTypeLogements: recherche de types de logement")
    @Test
    void testSearchTypeLogements() {
        typeLogementService.createTypeLogement(logementDataFactory.buildTypeLogement("T1", "Logement social", 1));
        typeLogementService.createTypeLogement(logementDataFactory.buildTypeLogement("T2", "Logement libre", 2));
        typeLogementService.createTypeLogement(logementDataFactory.buildTypeLogement("T3", "Logement intermédiaire", 3));

        Pageable pageable = PageRequest.of(0, 10);

        Page<TypeLogement> result = typeLogementService.searchTypeLogements(null, null, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTotalElements() >= 3, "Au moins 3 types de logement devraient être trouvés");
    }

    @DisplayName("testSearchTypeLogementsWithLibelle: recherche par libellé")
    @Test
    void testSearchTypeLogementsWithLibelle() {
        typeLogementService.createTypeLogement(logementDataFactory.buildTypeLogement("SOC", "Logement social", 1));
        typeLogementService.createTypeLogement(logementDataFactory.buildTypeLogement("LIB", "Logement libre", 2));

        Pageable pageable = PageRequest.of(0, 10);

        Page<TypeLogement> result = typeLogementService.searchTypeLogements("*social*", null, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTotalElements() >= 1);
    }

    @DisplayName("testSearchTypeLogementsOnlyActif: recherche uniquement les types actifs via le service")
    @Test
    void testSearchTypeLogementsOnlyActif() {
        Pageable pageable = PageRequest.of(0, 100);

        // Compter avant
        long totalTousAvant = typeLogementService.searchTypeLogements(null, null, pageable).getTotalElements();
        long totalActifsAvant = typeLogementService.searchTypeLogements(null, true, pageable).getTotalElements();

        // Type actif (dateDebut dans le passe)
        TypeLogement actif = logementDataFactory.buildTypeLogement("ACTIF", "Type actif", 1);
        actif.setDateDebut(OffsetDateTime.now().minusDays(1));
        typeLogementService.createTypeLogement(actif);

        // Type inactif (dateFin dans le passe)
        TypeLogement inactifDto = logementDataFactory.buildTypeLogement("INACTIF", "Type inactif", 2);
        inactifDto.setDateDebut(OffsetDateTime.now().minusDays(2));
        inactifDto.setDateFin(OffsetDateTime.now().minusDays(1));
        typeLogementService.createTypeLogement(inactifDto);

        // Compter apres
        long totalTousApres = typeLogementService.searchTypeLogements(null, null, pageable).getTotalElements();
        long totalActifsApres = typeLogementService.searchTypeLogements(null, true, pageable).getTotalElements();

        // Verifications
        Assertions.assertEquals(totalTousAvant + 2, totalTousApres,
                "On devrait avoir 2 types de plus au total");
        Assertions.assertEquals(totalActifsAvant + 1, totalActifsApres,
                "On devrait avoir 1 type actif de plus");
    }
}


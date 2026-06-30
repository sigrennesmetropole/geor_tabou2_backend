package rm.tabou2.service.tabou;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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
import rm.tabou2.service.dto.TypeAccessionLogement;
import rm.tabou2.service.tabou.logement.TypeAccessionLogementService;
import rm.tabou2.storage.tabou.dao.logement.TypeAccessionLogementDao;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class TypeAccessionLogementServiceTest extends DatabaseInitializerTest {

    @Autowired
    private TypeAccessionLogementService typeAccessionLogementService;

    @Autowired
    private TypeAccessionLogementDao typeAccessionLogementDao;

    @Autowired
    private LogementDataFactory logementDataFactory;

    @AfterEach
    void afterTest() {
        typeAccessionLogementDao.deleteAll();
    }

    @DisplayName("testCreateTypeAccessionLogement: création d'un type d'accession logement")
    @Test
    void testCreateTypeAccessionLogement() {
        TypeAccessionLogement dto = logementDataFactory.buildTypeAccessionLogement("SOCIAL", "Social", 1);

        TypeAccessionLogement result = typeAccessionLogementService.createTypeAccessionLogement(dto);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("SOCIAL", result.getCode());
        Assertions.assertEquals("Social", result.getLibelle());
        Assertions.assertEquals(1, result.getOrdre());
    }

    @DisplayName("testGetById: récupération d'un type d'accession logement par identifiant")
    @Test
    void testGetById() {
        TypeAccessionLogement dto = logementDataFactory.buildTypeAccessionLogement("LIBRE", "Libre", 2);
        TypeAccessionLogement created = typeAccessionLogementService.createTypeAccessionLogement(dto);

        TypeAccessionLogement result = typeAccessionLogementService.getById(Objects.requireNonNull(created.getId()));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getId(), result.getId());
        Assertions.assertEquals("LIBRE", result.getCode());
        Assertions.assertEquals("Libre", result.getLibelle());
    }

    @DisplayName("testGetByIdNotFound: récupération d'un type d'accession logement inexistant lève une exception")
    @Test
    void testGetByIdNotFound() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> typeAccessionLogementService.getById(999999L)
        );
    }

    @DisplayName("testUpdateTypeAccessionLogement: modification d'un type d'accession logement")
    @Test
    void testUpdateTypeAccessionLogement() {
        TypeAccessionLogement dto = logementDataFactory.buildTypeAccessionLogement("INTER", "Intermédiaire", 3);
        TypeAccessionLogement created = typeAccessionLogementService.createTypeAccessionLogement(dto);

        TypeAccessionLogement toUpdate = new TypeAccessionLogement();
        toUpdate.setId(created.getId());
        toUpdate.setCode("INTER_V2");
        toUpdate.setLibelle("Intermédiaire V2");
        toUpdate.setOrdre(5);
        toUpdate.setDateDebut(OffsetDateTime.now());
        toUpdate.setPortees(List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));

        TypeAccessionLogement result = typeAccessionLogementService.updateTypeAccessionLogement(toUpdate);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getId(), result.getId());
        Assertions.assertEquals("INTER_V2", result.getCode());
        Assertions.assertEquals("Intermédiaire V2", result.getLibelle());
        Assertions.assertEquals(5, result.getOrdre());
    }

    @DisplayName("testUpdateTypeAccessionLogementWithoutId: mise à jour sans id lève une IllegalArgumentException")
    @Test
    void testUpdateTypeAccessionLogementWithoutId() {
        TypeAccessionLogement dto = new TypeAccessionLogement();
        dto.setCode("CODE");
        dto.setLibelle("Libelle");
        dto.setOrdre(1);
        dto.setDateDebut(OffsetDateTime.now());
        dto.setPortees(List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> typeAccessionLogementService.updateTypeAccessionLogement(dto)
        );
    }

    @DisplayName("testUpdateTypeAccessionLogementNotFound: mise à jour d'un type inexistant lève une exception")
    @Test
    void testUpdateTypeAccessionLogementNotFound() {
        TypeAccessionLogement dto = new TypeAccessionLogement();
        dto.setId(999999L);
        dto.setCode("CODE");
        dto.setLibelle("Libelle");
        dto.setOrdre(1);
        dto.setDateDebut(OffsetDateTime.now());
        dto.setPortees(List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));

        Assertions.assertThrows(NoSuchElementException.class,
                () -> typeAccessionLogementService.updateTypeAccessionLogement(dto)
        );
    }

    @DisplayName("testInactivateTypeAccessionLogement: désactivation d'un type d'accession logement")
    @Test
    void testInactivateTypeAccessionLogement() {
         TypeAccessionLogement dto = logementDataFactory.buildTypeAccessionLogement("AIDE", "Aidé", 4);
        TypeAccessionLogement created = typeAccessionLogementService.createTypeAccessionLogement(dto);

        Assertions.assertNull(created.getDateFin(), "La date de fin devrait être null avant inactivation");

        TypeAccessionLogement result = typeAccessionLogementService.inactivateTypeAccessionLogement(Objects.requireNonNull(created.getId()));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getDateFin(), "La date de fin devrait être renseignée après inactivation");
    }

    @DisplayName("testInactivateTypeAccessionLogementNotFound: désactivation d'un type inexistant lève une exception")
    @Test
    void testInactivateTypeAccessionLogementNotFound() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> typeAccessionLogementService.inactivateTypeAccessionLogement(999999L)
        );
    }

    @DisplayName("testSearchTypeAccessionLogements: recherche de types d'accession logement")
    @Test
    void testSearchTypeAccessionLogements() {
        typeAccessionLogementService.createTypeAccessionLogement(logementDataFactory.buildTypeAccessionLogement("LOC_AIDE", "Locatif aidé", 1));
        typeAccessionLogementService.createTypeAccessionLogement(logementDataFactory.buildTypeAccessionLogement("LOC_LIBRE", "Locatif libre", 2));
        typeAccessionLogementService.createTypeAccessionLogement(logementDataFactory.buildTypeAccessionLogement("ACC_SOCIAL", "Accession sociale", 3));

        Pageable pageable = PageRequest.of(0, 10);

        Page<TypeAccessionLogement> result = typeAccessionLogementService.searchTypeAccessionLogements(null, null, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTotalElements() >= 3, "Au moins 3 types d'accession logement devraient être trouvés");
    }

    @DisplayName("testSearchTypeAccessionLogementsWithLibelle: recherche par libellé")
    @Test
    void testSearchTypeAccessionLogementsWithLibelle() {
        typeAccessionLogementService.createTypeAccessionLogement(logementDataFactory.buildTypeAccessionLogement("LOC_AIDE", "Locatif aidé", 1));
        typeAccessionLogementService.createTypeAccessionLogement(logementDataFactory.buildTypeAccessionLogement("ACC_SOCIAL", "Accession sociale", 2));

        Pageable pageable = PageRequest.of(0, 10);

        Page<TypeAccessionLogement> result = typeAccessionLogementService.searchTypeAccessionLogements("*Locatif*", null, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTotalElements() >= 1);
        Assertions.assertTrue(result.getContent().stream().allMatch(t -> Objects.toString(t.getLibelle(), "").contains("Locatif")));
    }

    @DisplayName("testSearchTypeAccessionLogementsOnlyActif: recherche uniquement les types actifs via le service")
    @Test
    void testSearchTypeAccessionLogementsOnlyActif() {
        Pageable pageable = PageRequest.of(0, 100);

        // Compter avant
        long totalTousAvant = typeAccessionLogementService.searchTypeAccessionLogements(null, null, pageable).getTotalElements();
        long totalActifsAvant = typeAccessionLogementService.searchTypeAccessionLogements(null, true, pageable).getTotalElements();

        // Type actif (dateDebut dans le passe)
        TypeAccessionLogement actif = logementDataFactory.buildTypeAccessionLogement("ACTIF", "Type actif", 1);
        actif.setDateDebut(OffsetDateTime.now().minusDays(1));
        typeAccessionLogementService.createTypeAccessionLogement(actif);

        // Type inactif (dateFin dans le passe)
        TypeAccessionLogement inactifDto = logementDataFactory.buildTypeAccessionLogement("INACTIF", "Type inactif", 2);
        inactifDto.setDateDebut(OffsetDateTime.now().minusDays(2));
        inactifDto.setDateFin(OffsetDateTime.now().minusDays(1));
        typeAccessionLogementService.createTypeAccessionLogement(inactifDto);

        // Compter apres
        long totalTousApres = typeAccessionLogementService.searchTypeAccessionLogements(null, null, pageable).getTotalElements();
        long totalActifsApres = typeAccessionLogementService.searchTypeAccessionLogements(null, true, pageable).getTotalElements();

        // Verifications
        Assertions.assertEquals(totalTousAvant + 2, totalTousApres,
                "On devrait avoir 2 types de plus au total");
        Assertions.assertEquals(totalActifsAvant + 1, totalActifsApres,
                "On devrait avoir 1 type actif de plus");
    }
}


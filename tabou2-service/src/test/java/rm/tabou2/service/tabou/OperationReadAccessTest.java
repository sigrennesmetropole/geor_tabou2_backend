package rm.tabou2.service.tabou;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.OperationReadAccess;
import rm.tabou2.service.sig.CommuneResolver;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.dao.operation.OperationCustomDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationCommuneViewEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationVisibilityFilter;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@Transactional
class OperationReadAccessTest extends DatabaseInitializerTest {

    private static final BigDecimal BRUZ = new BigDecimal(35047);

    private static final BigDecimal RENNES = new BigDecimal(35238);

    @Autowired
    private OperationReadAccess operationReadAccess;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationCustomDao operationCustomDao;
    @Autowired
    private EtapeOperationDao etapeOperationDao;

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @MockitoBean
    private AuthentificationHelper authentificationHelper;

    @MockitoBean
    private CommuneResolver communeResolver;

    private long publicId;
    private long offBruzId;
    private long offRennesId;
    private long offProspectifBruzId;
    private long offSansEmpriseId;
    private long offSecteurId;

    @BeforeEach
    void initData() {
        publicId = createOperation("PUB", "EN_ETUDE_PUBLIC", false, BRUZ);
        offBruzId = createOperation("OFF_BRUZ", "EN_ETUDE_OFF", false, BRUZ);
        offRennesId = createOperation("OFF_RENNES", "EN_ETUDE_OFF", false, RENNES);
        offProspectifBruzId = createOperation("OFF_PROSP", "EN_PROJET_OFF", false, BRUZ);
        offSansEmpriseId = createOperation("OFF_SANS", "EN_ETUDE_OFF", false, null);
        offSecteurId = createOperation("OFF_SECT", "EN_ETUDE_OFF", true, RENNES);
        entityManager.flush();
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);
    }

    @DisplayName("Référent / Administrateur : toutes les opérations sont visibles")
    @Test
    void testGlobal() {
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(true);
        assertEquals(6, countVisible());
        assertVisible(publicId, offBruzId, offRennesId, offProspectifBruzId, offSansEmpriseId, offSecteurId);
    }

    @DisplayName("Utilisateur de Bruz : public et Off non prospectif de Bruz uniquement")
    @Test
    void testCommune() {
        Mockito.when(authentificationHelper.getOrganisation()).thenReturn("Bruz");
        Mockito.when(communeResolver.resolveCodeInsee("Bruz")).thenReturn(Optional.of(BRUZ));

        assertEquals(2, countVisible());
        assertVisible(publicId, offBruzId);
        assertHidden(offRennesId, offProspectifBruzId, offSansEmpriseId, offSecteurId);
    }

    @DisplayName("Utilisateur sans commune reconnue : public uniquement")
    @Test
    void testSansCommune() {
        Mockito.when(authentificationHelper.getOrganisation()).thenReturn("Autre organisation");
        Mockito.when(communeResolver.resolveCodeInsee("Autre organisation")).thenReturn(Optional.empty());

        assertEquals(1, countVisible());
        assertVisible(publicId);
        assertHidden(offBruzId, offRennesId, offProspectifBruzId, offSansEmpriseId, offSecteurId);
    }

    @DisplayName("Opération inexistante : pas de 403 (le 404 reste à la charge du service)")
    @Test
    void testInexistante() {
        assertDoesNotThrow(() -> operationReadAccess.checkCanRead(-1L));
    }

    @DisplayName("Profil sélectionné selon les rôles et l'organisation")
    @Test
    void testProfil() {
        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(true);
        assertTrue(operationReadAccess.getVisibilityFilter().isGlobal());

        Mockito.when(authentificationHelper.hasRestreintAccess()).thenReturn(false);
        Mockito.when(authentificationHelper.getOrganisation()).thenReturn("Bruz");
        Mockito.when(communeResolver.resolveCodeInsee("Bruz")).thenReturn(Optional.of(BRUZ));
        OperationVisibilityFilter commune = operationReadAccess.getVisibilityFilter();
        assertFalse(commune.isGlobal());
        assertEquals(BRUZ, commune.getCodeInsee());

        Mockito.when(communeResolver.resolveCodeInsee("Bruz")).thenReturn(Optional.empty());
        OperationVisibilityFilter aucune = operationReadAccess.getVisibilityFilter();
        assertFalse(aucune.isGlobal());
        assertNull(aucune.getCodeInsee());
    }

    @DisplayName("Recherche : le comptage et la pagination respectent la visibilité, sans dépendre de diffusionRestreinte")
    @Test
    void testSearch() {
        Mockito.when(authentificationHelper.getOrganisation()).thenReturn("Bruz");
        Mockito.when(communeResolver.resolveCodeInsee("Bruz")).thenReturn(Optional.of(BRUZ));
        OperationVisibilityFilter commune = operationReadAccess.getVisibilityFilter();

        OperationEntity offBruz = operationDao.findById(offBruzId).orElseThrow();
        offBruz.setDiffusionRestreinte(true);
        entityManager.flush();

        Page<OperationEntity> page = operationCustomDao.searchOperations(new OperationsCriteria(), commune, PageRequest.of(0, 2));
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getContent().size());

        OperationsCriteria demandeOff = new OperationsCriteria();
        demandeOff.setDiffusionRestreinte(true);
        page = operationCustomDao.searchOperations(demandeOff, commune, PageRequest.of(0, 10));
        assertEquals(Set.of(offBruzId), idsOf(page));

        page = operationCustomDao.searchOperations(new OperationsCriteria(), OperationVisibilityFilter.global(), PageRequest.of(0, 10));
        assertEquals(6, page.getTotalElements());
    }

    private Set<Long> idsOf(Page<OperationEntity> page) {
        return page.getContent().stream().map(OperationEntity::getId).collect(Collectors.toSet());
    }

    private void assertVisible(long... ids) {
        for (long id : ids) {
            assertDoesNotThrow(() -> operationReadAccess.checkCanRead(id), "visible attendu : " + id);
        }
    }

    private void assertHidden(long... ids) {
        for (long id : ids) {
            assertThrows(AccessDeniedException.class, () -> operationReadAccess.checkCanRead(id), "403 attendu : " + id);
        }
    }

    private long countVisible() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<OperationEntity> root = query.from(OperationEntity.class);
        query.select(builder.countDistinct(root)).where(operationReadAccess.toPredicate(root, query, builder));
        return entityManager.createQuery(query).getSingleResult();
    }

    private long createOperation(String code, String codeEtape, boolean secteur, BigDecimal codeInsee) {
        OperationEntity operation = new OperationEntity();
        operation.setNom(code);
        operation.setCode(code);
        operation.setNumAds(code);
        operation.setDiffusionRestreinte(false);
        operation.setSecteur(secteur);
        operation.setEtapeOperation(etapeOperationDao.findByCode(codeEtape));
        operationDao.save(operation);
        if (codeInsee != null) {
            OperationCommuneViewEntity commune = new OperationCommuneViewEntity();
            commune.setIdTabou((int) operation.getId());
            commune.setCodeInsee(codeInsee);
            entityManager.persist(commune);
        }
        return operation.getId();
    }
}

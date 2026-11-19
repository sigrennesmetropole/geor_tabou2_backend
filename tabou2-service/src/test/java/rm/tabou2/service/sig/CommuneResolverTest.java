package rm.tabou2.service.sig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rm.tabou2.storage.sig.dao.CommuneDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommuneResolverTest {

    private CommuneDao communeDao;

    private CommuneResolver communeResolver;

    @BeforeEach
    void init() {
        communeDao = Mockito.mock(CommuneDao.class);
        Mockito.when(communeDao.findByCommuneAgglo((short) 1)).thenReturn(List.of(
                commune("Rennes", 35238),
                commune("L'Hermitage", 35133),
                commune("Saint-Grégoire", 35281),
                commune("Bruz", 35047)));
        communeResolver = new CommuneResolver(communeDao);
    }

    @DisplayName("testNormalize: accents, casse, apostrophes et tirets")
    @Test
    void testNormalize() {
        assertEquals("l hermitage", CommuneResolver.normalize("L'Hermitage"));
        assertEquals("l hermitage", CommuneResolver.normalize("  L’HERMITAGE "));
        assertEquals("saint gregoire", CommuneResolver.normalize("Saint-Grégoire"));
        assertEquals("saint gregoire", CommuneResolver.normalize("saint  gregoire"));
        assertEquals("", CommuneResolver.normalize(null));
        assertEquals("", CommuneResolver.normalize(" - "));
    }

    @DisplayName("testResolveCodeInsee: l'organisation est rapprochée de la commune après normalisation")
    @Test
    void testResolveCodeInsee() {
        assertEquals(Optional.of(new BigDecimal(35133)), communeResolver.resolveCodeInsee("L’Hermitage"));
        assertEquals(Optional.of(new BigDecimal(35281)), communeResolver.resolveCodeInsee("SAINT GREGOIRE"));
        assertEquals(Optional.of(new BigDecimal(35047)), communeResolver.resolveCodeInsee("bruz"));
    }

    @DisplayName("testResolveUnknown: organisation inconnue, absente ou métropole : aucune commune")
    @Test
    void testResolveUnknown() {
        assertTrue(communeResolver.resolveCodeInsee("Rennes Métropole").isEmpty());
        assertTrue(communeResolver.resolveCodeInsee("").isEmpty());
        assertTrue(communeResolver.resolveCodeInsee(null).isEmpty());
    }

    @DisplayName("testCache: les communes ne sont lues qu'une fois")
    @Test
    void testCache() {
        communeResolver.resolveCodeInsee("Bruz");
        communeResolver.resolveCodeInsee("Rennes");
        communeResolver.resolveCodeInsee("Inconnue");
        Mockito.verify(communeDao, Mockito.times(1)).findByCommuneAgglo((short) 1);
    }

    @DisplayName("testDaoFailure: une erreur de lecture ne bloque pas et n'accorde aucun accès")
    @Test
    void testDaoFailure() {
        Mockito.when(communeDao.findByCommuneAgglo((short) 1)).thenThrow(new IllegalStateException("bdd"));
        assertTrue(communeResolver.resolveCodeInsee("Bruz").isEmpty());
    }

    private static CommuneEntity commune(String nom, int codeInsee) {
        CommuneEntity entity = new CommuneEntity();
        entity.setNom(nom);
        entity.setCodeInsee(new BigDecimal(codeInsee));
        entity.setCommuneAgglo((short) 1);
        return entity;
    }
}

package rm.tabou2.service.sig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rm.tabou2.storage.sig.dao.CommuneDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Résout le code INSEE d'une commune de l'agglomération à partir du nom de l'organisation geOrchestra.
 */
@Component
public class CommuneResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommuneResolver.class);

    private static final Short COMMUNE_AGGLO = 1;

    private static final long CACHE_TTL_NANOS = Duration.ofHours(1).toNanos();

    private final CommuneDao communeDao;

    private volatile Map<String, BigDecimal> codesByName;

    private volatile long loadedAt;

    public CommuneResolver(CommuneDao communeDao) {
        this.communeDao = communeDao;
    }

    /**
     * Normalise un nom : sans accents, en minuscules, toute suite de caractères non alphanumériques
     * remplacée par une espace unique, espaces de bord retirés.
     *
     * @param name nom brut
     * @return le nom normalisé, vide si name est null
     */
    public static String normalize(String name) {
        if (name == null) {
            return "";
        }
        String decomposed = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return decomposed.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
    }

    /**
     * @param organisation nom de l'organisation geOrchestra
     * @return le code INSEE de la commune correspondante, vide si l'organisation est inconnue
     */
    public Optional<BigDecimal> resolveCodeInsee(String organisation) {
        String key = normalize(organisation);
        if (key.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal code = getCodesByName().get(key);
        if (code == null) {
            LOGGER.warn("Aucune commune de l'agglomération ne correspond à l'organisation '{}'", organisation);
        }
        return Optional.ofNullable(code);
    }

    private Map<String, BigDecimal> getCodesByName() {
        Map<String, BigDecimal> current = codesByName;
        if (current == null || System.nanoTime() - loadedAt > CACHE_TTL_NANOS) {
            synchronized (this) {
                current = codesByName;
                if (current == null || System.nanoTime() - loadedAt > CACHE_TTL_NANOS) {
                    try {
                        current = load();
                    } catch (RuntimeException e) {
                        LOGGER.error("Chargement des communes impossible, aucun accès territorial accordé", e);
                        return Map.of();
                    }
                    codesByName = current;
                    loadedAt = System.nanoTime();
                }
            }
        }
        return current;
    }

    private Map<String, BigDecimal> load() {
        Map<String, BigDecimal> map = new HashMap<>();
        for (CommuneEntity commune : communeDao.findByCommuneAgglo(COMMUNE_AGGLO)) {
            String key = normalize(commune.getNom());
            if (!key.isEmpty() && commune.getCodeInsee() != null
                    && map.putIfAbsent(key, commune.getCodeInsee()) != null) {
                LOGGER.warn("Plusieurs communes portent le nom normalisé '{}', la première est conservée", key);
            }
        }
        return map;
    }
}

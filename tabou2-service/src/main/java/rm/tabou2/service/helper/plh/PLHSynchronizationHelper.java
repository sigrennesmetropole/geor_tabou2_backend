package rm.tabou2.service.helper.plh;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.plh.AttributPLHEntity;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

@Component
@Slf4j
public class PLHSynchronizationHelper {

    /**
     * Synchronise récursivement les valeurs des attributs PLH vers les champs de l'opération.
     * Si le typePLH possède un synchronizedField, la valeur de l'attribut PLH correspondant
     * est reportée sur le champ de l'opération via introspection.
     *
     * @param typePLH         le nœud PLH à traiter
     * @param operationEntity l'opération à mettre à jour
     */
    public void synchronizePLHToOperation(TypePLHEntity typePLH, OperationEntity operationEntity) {
        if (StringUtils.isNotBlank(typePLH.getSynchronizedField())) {
            Method setter = lookupSetterMethod(OperationEntity.class, typePLH.getSynchronizedField());
            if (setter == null) {
                log.warn("Setter introuvable pour '{}' sur OperationEntity", typePLH.getSynchronizedField());
            } else {
                Optional<AttributPLHEntity> attributPLH = lookupAttributPLH(operationEntity.getAttributsPLH(), typePLH);
                String valeur = attributPLH.map(AttributPLHEntity::getValue).orElse(null);
                try {
                    Class<?> typeCible = setter.getParameterTypes()[0];
                    Object valeurConvertie = convertFromString(valeur, typeCible);
                    // setAccessible nécessaire car Lombok génère les setters avec accès package-private
                    setter.setAccessible(true);
                    setter.invoke(operationEntity, valeurConvertie);
                } catch (Exception e) {
                    log.error("Erreur synchronisation PLH→Operation champ='{}'", typePLH.getSynchronizedField(), e);
                }
            }
        }
        // Récursion sur les fils
        if (!CollectionUtils.isEmpty(typePLH.getFils())) {
            for (TypePLHEntity fils : typePLH.getFils()) {
                synchronizePLHToOperation(fils, operationEntity);
            }
        }
    }

    /**
     * Synchronise récursivement les champs de l'opération vers les valeurs des attributs PLH.
     * Si le typePLH possède un synchronizedField, la valeur du champ de l'opération
     * est reportée sur l'attribut PLH correspondant via introspection.
     *
     * @param typePLH         le nœud PLH à traiter
     * @param operationEntity l'opération source
     */
    public void synchronizeOperationToPLH(TypePLHEntity typePLH, OperationEntity operationEntity) {
        if (StringUtils.isNotBlank(typePLH.getSynchronizedField())) {
            Method getter = lookupGetterMethod(OperationEntity.class, typePLH.getSynchronizedField());
            if (getter == null) {
                log.warn("Getter introuvable pour '{}' sur OperationEntity", typePLH.getSynchronizedField());
            } else {
                try {
                    // setAccessible nécessaire car Lombok génère les getters avec accès package-private
                    getter.setAccessible(true);
                    Object valeur = getter.invoke(operationEntity);
                    Optional<AttributPLHEntity> attributPLH = lookupAttributPLH(operationEntity.getAttributsPLH(), typePLH);
                    attributPLH.ifPresent(a -> a.setValue(valeur != null ? valeur.toString() : null));
                } catch (Exception e) {
                    log.error("Erreur synchronisation Operation→PLH champ='{}'", typePLH.getSynchronizedField(), e);
                }
            }
        }
        // Récursion sur les fils
        if (!CollectionUtils.isEmpty(typePLH.getFils())) {
            for (TypePLHEntity fils : typePLH.getFils()) {
                synchronizeOperationToPLH(fils, operationEntity);
            }
        }
    }

    /**
     * Recherche l'AttributPLHEntity correspondant au TypePLH donné dans l'ensemble des attributs.
     *
     * @param attributsPLH ensemble des attributs PLH de l'opération
     * @param typePLH      le type PLH recherché
     * @return l'attribut PLH correspondant, ou empty si absent
     */
    private Optional<AttributPLHEntity> lookupAttributPLH(Set<AttributPLHEntity> attributsPLH, TypePLHEntity typePLH) {
        if (CollectionUtils.isEmpty(attributsPLH)) {
            return Optional.empty();
        }
        // getId() retourne un long primitif → comparaison avec == obligatoire
        return attributsPLH.stream()
                .filter(a -> a.getType() != null && a.getType().getId() == typePLH.getId())
                .findFirst();
    }

    /**
     * Recherche le setter correspondant au champ donné dans la hiérarchie de classes.
     * Ex: synchronizedField="nom" → cherche setNom(...)
     *
     * @param classe   la classe cible
     * @param nomChamp le nom du champ
     * @return la méthode setter, ou null si introuvable
     */
    private Method lookupSetterMethod(Class<?> classe, String nomChamp) {
        String nomSetter = "set" + Character.toUpperCase(nomChamp.charAt(0)) + nomChamp.substring(1);
        Class<?> classeCourante = classe;
        while (classeCourante != null) {
            for (Method methode : classeCourante.getDeclaredMethods()) {
                if (methode.getName().equals(nomSetter)
                        && methode.getParameterCount() == 1
                        && isTypeSupported(methode.getParameterTypes()[0])) {
                    return methode;
                }
            }
            classeCourante = classeCourante.getSuperclass();
        }
        return null;
    }

    /**
     * Recherche le getter correspondant au champ donné dans la hiérarchie de classes.
     * Ex: synchronizedField="nom" → cherche getNom() ou isNom() pour les booléens.
     *
     * @param classe   la classe cible
     * @param nomChamp le nom du champ
     * @return la méthode getter, ou null si introuvable
     */
    private Method lookupGetterMethod(Class<?> classe, String nomChamp) {
        String prefix = Character.toUpperCase(nomChamp.charAt(0)) + nomChamp.substring(1);
        String nomGetter = "get" + prefix;
        String nomIsGetter = "is" + prefix; // pour les booléens
        Class<?> classeCourante = classe;
        while (classeCourante != null) {
            for (Method methode : classeCourante.getDeclaredMethods()) {
                if ((methode.getName().equals(nomGetter) || methode.getName().equals(nomIsGetter))
                        && methode.getParameterCount() == 0
                        && isTypeSupported(methode.getReturnType())) {
                    return methode;
                }
            }
            classeCourante = classeCourante.getSuperclass();
        }
        return null;
    }

    /**
     * Vérifie si le type est supporté pour la synchronisation.
     *
     * @param type le type à vérifier
     * @return true si supporté, false sinon
     */
    private boolean isTypeSupported(Class<?> type) {
        return type == String.class
                || type == Integer.class || type == int.class
                || type == Long.class    || type == long.class
                || type == Double.class  || type == double.class
                || type == Boolean.class || type == boolean.class
                || type == LocalDateTime.class
                || type == LocalDate.class
                || type == Instant.class;
    }

    /**
     * Convertit une valeur String vers le type cible.
     * Retourne null si la valeur est null ou ne peut pas être parsée.
     *
     * @param value      la valeur à convertir
     * @param targetType le type cible
     * @return la valeur convertie, ou null en cas d'erreur
     */
    private Object convertFromString(String value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        try {
            if (targetType == String.class) {
                return value;
            }
            if (targetType == LocalDateTime.class) {
                return LocalDateTime.parse(value);
            }
            if (targetType == LocalDate.class) {
                return LocalDate.parse(value);
            }
            if (targetType == Instant.class) {
                return Instant.parse(value);
            }
            if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(value);
            }
            if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(value);
            }
            if (targetType == Double.class || targetType == double.class) {
                return Double.parseDouble(value);
            }
            if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.parseBoolean(value);
            }
            log.warn("Type cible non supporté '{}' pour la valeur '{}'", targetType.getName(), value);
            return null;
        } catch (Exception e) {
            // En cas d'erreur de parsing, on retourne null plutôt que de propager l'exception
            log.warn("Impossible de convertir '{}' vers le type '{}': {}", value, targetType.getName(), e.getMessage());
            return null;
        }
    }
}
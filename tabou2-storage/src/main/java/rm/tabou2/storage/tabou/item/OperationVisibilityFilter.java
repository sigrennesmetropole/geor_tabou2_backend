package rm.tabou2.storage.tabou.item;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationCommuneViewEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CODE_INSEE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ETAPE_OPERATION;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID_TABOU;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_MODE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_PROSPECTIF;

/**
 * Règle de visibilité des opérations (US-323). Une opération est visible si :
 * <ul>
 * <li>l'accès est global ;</li>
 * <li>ou son étape courante n'est pas en mode OFF (étape absente ou PUBLIC) ;</li>
 * <li>ou elle est en OFF, non prospective et rattachée à la commune de l'utilisateur.</li>
 * </ul>
 */
public final class OperationVisibilityFilter {

    public static final String MODE_PUBLIC = "PUBLIC";

    public static final String MODE_OFF = "OFF";

    private static final OperationVisibilityFilter GLOBAL = new OperationVisibilityFilter(true, null);

    private static final OperationVisibilityFilter PUBLIC_ONLY = new OperationVisibilityFilter(false, null);

    private final boolean global;

    private final BigDecimal codeInsee;

    private OperationVisibilityFilter(boolean global, BigDecimal codeInsee) {
        this.global = global;
        this.codeInsee = codeInsee;
    }

    /** Accès à toutes les opérations (Référents, Administrateurs). */
    public static OperationVisibilityFilter global() {
        return GLOBAL;
    }

    /** Opérations publiques uniquement (utilisateur sans commune reconnue). */
    public static OperationVisibilityFilter publicOnly() {
        return PUBLIC_ONLY;
    }

    /** Opérations publiques et Off non prospectif de la commune. */
    public static OperationVisibilityFilter commune(BigDecimal codeInsee) {
        if (codeInsee == null) {
            return PUBLIC_ONLY;
        }
        return new OperationVisibilityFilter(false, codeInsee);
    }

    public boolean isGlobal() {
        return global;
    }

    public BigDecimal getCodeInsee() {
        return codeInsee;
    }

    /**
     * Expression ECQL équivalente à la règle de visibilité, à appliquer sur la couche cartographique
     * {@code v_oa_operation} (colonnes mode_etape, prospectif et code_insee).
     *
     * @return l'expression, ou null pour un accès global (aucune restriction)
     */
    public String toCql() {
        if (global) {
            return null;
        }
        StringBuilder cql = new StringBuilder("mode_etape IS NULL OR mode_etape = '").append(MODE_PUBLIC).append('\'');
        if (codeInsee != null) {
            cql.append(" OR (mode_etape = '").append(MODE_OFF).append("' AND prospectif = false AND code_insee = ")
                    .append(codeInsee.toBigInteger()).append(')');
        }
        return cql.toString();
    }

    /**
     * Construit le prédicat de visibilité sur une racine d'opérations.
     *
     * @return le prédicat, ou une conjonction vide pour un accès global
     */
    public Predicate toPredicate(Root<OperationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (global) {
            return builder.conjunction();
        }
        Join<OperationEntity, EtapeOperationEntity> etape = root.join(FIELD_ETAPE_OPERATION, JoinType.LEFT);
        List<Predicate> visible = new ArrayList<>();
        visible.add(builder.isNull(etape.get(FIELD_MODE)));
        visible.add(builder.equal(etape.get(FIELD_MODE), MODE_PUBLIC));
        if (codeInsee != null) {
            Subquery<Integer> commune = query.subquery(Integer.class);
            Root<OperationCommuneViewEntity> view = commune.from(OperationCommuneViewEntity.class);
            commune.select(builder.literal(1)).where(builder.equal(view.get(FIELD_ID_TABOU), root.get(FIELD_ID)),
                    builder.equal(view.get(FIELD_CODE_INSEE), codeInsee));
            visible.add(builder.and(builder.equal(etape.get(FIELD_MODE), MODE_OFF),
                    builder.isFalse(etape.get(FIELD_PROSPECTIF)), builder.exists(commune)));
        }
        return builder.or(visible.toArray(new Predicate[0]));
    }
}

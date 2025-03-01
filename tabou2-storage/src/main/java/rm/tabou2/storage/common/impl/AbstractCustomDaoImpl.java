package rm.tabou2.storage.common.impl;

import jakarta.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * Classe abstraite des customDao.
 */
public abstract class AbstractCustomDaoImpl {

    /**
     * Ajout d'un prédicat sur la requ
     *
     * @param criteria
     * @param type
     * @param predicates
     * @param builder
     * @param root
     */
    protected void predicateStringCriteria(String criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
       predicateStringCriteria(criteria, root.get(type), predicates, builder);
    }

    protected void predicateStringCriteria(String criteria, Expression<String> path, List<Predicate> predicates, CriteriaBuilder builder) {
        if (criteria != null) {
            if (criteria.indexOf('*') == -1) {
                predicates.add(builder.equal(builder.lower(path), criteria.toLowerCase()));
            } else {
                predicates.add(builder.like(builder.lower(path), criteria.replace("*", "%").toLowerCase()));
            }
        }
    }

    protected void predicateStringCriteriaForJoin(String criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Join<?, ?> join) {
        if (criteria != null) {
            if (criteria.indexOf('*') == -1) {
                predicates.add(builder.equal(join.get(type), criteria));
            } else {
                predicates.add(builder.like(join.get(type), criteria.replace("*", "%")));
            }
        }
    }

    protected void predicateLongCriteriaForJoin(Long criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Join<?, ?> join) {

        predicates.add(builder.equal(join.get(type), criteria));

    }

    protected void predicateDateCriteria(Date dateDebut, Date dateFin, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

        predicateBetweenCriteria(dateDebut, dateFin, type, predicates, builder, root);
    }

    protected void predicateBooleanCriteria(Boolean criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

        predicateBooleanCriteria(criteria, root.get(type), predicates, builder);

    }

    protected void predicateBooleanCriteria(Boolean criteria, Path<Boolean> path, List<Predicate> predicates, CriteriaBuilder builder) {
        if (Boolean.TRUE.equals(criteria)) {
            predicates.add(builder.isTrue(path));
        } else {
            predicates.add(builder.isFalse(path));
        }
    }

    protected void predicateIntegerCriteria(Integer criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

        if (criteria != null) {
            predicates.add(builder.equal(root.get(type), criteria));
        }

    }

    protected void predicateCriteriaNullOrNot(Boolean criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, From<?, ?> from) {

        if (Boolean.TRUE.equals(criteria)) {
            predicates.add(builder.isNull(from.get(type)));
        } else {
            predicates.add(builder.isNotNull(from.get(type)));
        }

    }

    protected void predicateBooleanOrGreaterThanIntegerCriteria(Boolean criteria, Integer lowerValue, List<String> types, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
        if (Boolean.TRUE.equals(criteria)) {
            Predicate[] predicatesGreater = types.stream()
                    .map(type -> builder.greaterThan(root.get(type), lowerValue))
                    .toArray(Predicate[]::new);
            predicates.add(builder.or(predicatesGreater));
        } else {
            Predicate[] predicatesLess = types.stream()
                    .map(type -> builder.lessThanOrEqualTo(root.get(type), lowerValue))
                    .toArray(Predicate[]::new);
            predicates.add(builder.and(predicatesLess));
        }
    }

    protected void predicateYearCriteria(Integer anneeDebut, Integer anneeFin, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

        predicateBetweenCriteria(anneeDebut, anneeFin, type, predicates, builder, root);

    }

    protected void predicateLongCriteria(Long criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

        if (criteria != null) {
            predicates.add(builder.equal(root.get(type), criteria));
        }

    }


    private <T extends Comparable<? super T>> void predicateBetweenCriteria(T lower, T upper, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

        if (lower != null && upper != null) {

            predicates.add(builder.between(root.get(type), lower, upper));

        } else if (lower != null) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(type), lower));

        } else if (upper != null) {

            predicates.add(builder.lessThanOrEqualTo(root.get(type), upper));

        }
    }


}

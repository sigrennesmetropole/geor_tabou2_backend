package rm.tabou2.storage.dao.impl;

import rm.tabou2.storage.tabou.entity.OperationEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Classe abstraite des customDao.
 */
public abstract class AbstractCustomDaoImpl {

    /**
     * Ajout d'un prédicat sur la requ
     * @param criteria
     * @param type
     * @param predicates
     * @param builder
     * @param root
     */
    protected void predicateStringCriteria(String criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
        if (criteria != null) {
            if (criteria.indexOf('*') == -1) {
                predicates.add(builder.equal(root.get(type), criteria));
            } else {
                predicates.add(builder.like(root.get(type), criteria.replace("*", "%")));
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

    protected void predicateDateCriteria(Date dateDebut, Date dateFin, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

        if (dateDebut != null && dateFin != null) {

            predicates.add(builder.between(root.get(type), dateDebut, dateFin));

        } else if (dateDebut != null && dateFin == null) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(type), dateDebut));

        } else if (dateDebut == null && dateFin != null) {

            predicates.add(builder.lessThanOrEqualTo(root.get(type), dateFin));

        }
    }

    protected void predicateBooleanCriteria(Boolean criteria, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<OperationEntity> root) {

        if (Boolean.TRUE.equals(criteria)) {
            predicates.add(builder.isTrue(root.get(type)));
        } else {
            predicates.add(builder.isFalse(root.get(type)));
        }

    }

}

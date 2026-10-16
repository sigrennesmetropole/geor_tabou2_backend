package rm.tabou2.storage.tabou.dao.operation.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.VocationPlaeCustomDao;
import rm.tabou2.storage.tabou.entity.operation.VocationPlaeEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VocationPlaeCustomDaoImpl extends AbstractCustomDaoImpl<VocationPlaeEntity> implements VocationPlaeCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<VocationPlaeEntity> searchVocationsPlae(Boolean actifUniquement, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<VocationPlaeEntity> countRoot = countQuery.from(VocationPlaeEntity.class);
        countQuery.select(builder.countDistinct(countRoot));
        applyActifPredicates(actifUniquement, builder, countQuery, countRoot);
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        CriteriaQuery<VocationPlaeEntity> searchQuery = builder.createQuery(VocationPlaeEntity.class);
        Root<VocationPlaeEntity> searchRoot = searchQuery.from(VocationPlaeEntity.class);
        applyActifPredicates(actifUniquement, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<VocationPlaeEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<VocationPlaeEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());
    }

    private void applyActifPredicates(Boolean actifUniquement, CriteriaBuilder builder, CriteriaQuery<?> query, Root<VocationPlaeEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicateActifCriteria(actifUniquement, predicates, builder, root);
        if (!predicates.isEmpty()) {
            query.where(builder.and(predicates.toArray(new Predicate[0])));
        }
    }

}

package rm.tabou2.storage.tabou.dao.operation.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.RenouvellementEconomiquePlaeCustomDao;
import rm.tabou2.storage.tabou.entity.operation.RenouvellementEconomiquePlaeEntity;

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
public class RenouvellementEconomiquePlaeCustomDaoImpl extends AbstractCustomDaoImpl<RenouvellementEconomiquePlaeEntity>
        implements RenouvellementEconomiquePlaeCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<RenouvellementEconomiquePlaeEntity> searchRenouvellementsEconomiquesPlae(Boolean actifUniquement, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<RenouvellementEconomiquePlaeEntity> countRoot = countQuery.from(RenouvellementEconomiquePlaeEntity.class);
        countQuery.select(builder.countDistinct(countRoot));
        applyActifPredicates(actifUniquement, builder, countQuery, countRoot);
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        CriteriaQuery<RenouvellementEconomiquePlaeEntity> searchQuery = builder.createQuery(RenouvellementEconomiquePlaeEntity.class);
        Root<RenouvellementEconomiquePlaeEntity> searchRoot = searchQuery.from(RenouvellementEconomiquePlaeEntity.class);
        applyActifPredicates(actifUniquement, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<RenouvellementEconomiquePlaeEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<RenouvellementEconomiquePlaeEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());
    }

    private void applyActifPredicates(Boolean actifUniquement, CriteriaBuilder builder, CriteriaQuery<?> query,
                                      Root<RenouvellementEconomiquePlaeEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicateActifCriteria(actifUniquement, predicates, builder, root);
        if (!predicates.isEmpty()) {
            query.where(builder.and(predicates.toArray(new Predicate[0])));
        }
    }

}

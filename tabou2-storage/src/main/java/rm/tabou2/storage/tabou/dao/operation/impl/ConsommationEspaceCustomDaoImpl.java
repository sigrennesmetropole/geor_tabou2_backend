package rm.tabou2.storage.tabou.dao.operation.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.ConsommationEspaceCustomDao;
import rm.tabou2.storage.tabou.entity.operation.ConsommationEspaceEntity;

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
public class ConsommationEspaceCustomDaoImpl extends AbstractCustomDaoImpl<ConsommationEspaceEntity> implements ConsommationEspaceCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<ConsommationEspaceEntity> searchConsommationEspace(Boolean actifUniquement, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<ConsommationEspaceEntity> countRoot = countQuery.from(ConsommationEspaceEntity.class);
        countQuery.select(builder.countDistinct(countRoot));
        applyActifPredicates(actifUniquement, builder, countQuery, countRoot);
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<ConsommationEspaceEntity> searchQuery = builder.createQuery(ConsommationEspaceEntity.class);
        Root<ConsommationEspaceEntity> searchRoot = searchQuery.from(ConsommationEspaceEntity.class);
        applyActifPredicates(actifUniquement, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<ConsommationEspaceEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<ConsommationEspaceEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());

    }

    /**
     * Restreint la requête aux consommations d'espace actives (date_debut <= now et date_fin nulle ou future) si demandé.
     *
     * @param actifUniquement si true, filtre sur les dates de validité
     * @param builder criteria builder
     * @param query   requête à filtrer
     * @param root    racine de la requête
     */
    private void applyActifPredicates(Boolean actifUniquement, CriteriaBuilder builder, CriteriaQuery<?> query, Root<ConsommationEspaceEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicateActifCriteria(actifUniquement, predicates, builder, root);
        if (!predicates.isEmpty()) {
            query.where(builder.and(predicates.toArray(new Predicate[0])));
        }
    }

}

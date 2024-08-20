package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.VocationZACustomDao;
import rm.tabou2.storage.tabou.entity.operation.VocationZAEntity;
import rm.tabou2.storage.tabou.item.VocationZACriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_INACTIF;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;

@Repository
public class VocationZACustomDaoImpl extends AbstractCustomDaoImpl implements VocationZACustomDao {

    @PersistenceContext(unitName = "tabouPU")
    EntityManager entityManager;

    @Override
    public Page<VocationZAEntity> searchVocationsZA(VocationZACriteria criteria, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<VocationZAEntity> countRoot = countQuery.from(VocationZAEntity.class);
        buildQuery(criteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<VocationZAEntity> searchQuery = builder.createQuery(VocationZAEntity.class);
        Root<VocationZAEntity> searchRoot = searchQuery.from(VocationZAEntity.class);
        buildQuery(criteria, builder, searchQuery, searchRoot);
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<VocationZAEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<VocationZAEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(VocationZACriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> query, Root<VocationZAEntity> root) {
        if (criteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(criteria.getLibelle())) {
                predicateStringCriteria(criteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
            }

            if (criteria.getInactif() != null) {
                predicateCriteriaNullOrNot(criteria.getInactif(), FIELD_INACTIF, predicates, builder, root);
            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }
    }
}

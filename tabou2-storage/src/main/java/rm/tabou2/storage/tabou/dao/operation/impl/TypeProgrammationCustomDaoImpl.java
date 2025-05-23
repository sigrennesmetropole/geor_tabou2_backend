package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringUtils;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.TypeProgrammationCustomDao;
import rm.tabou2.storage.tabou.entity.operation.TypeProgrammationEntity;
import rm.tabou2.storage.tabou.item.TypeProgrammationCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_INACTIF;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;

@Repository
public class TypeProgrammationCustomDaoImpl extends AbstractCustomDaoImpl implements TypeProgrammationCustomDao {
    @PersistenceContext(unitName = "tabouPU")
    EntityManager entityManager;

    @Override
    public Page<TypeProgrammationEntity> searchTypesProgrammations(TypeProgrammationCriteria criteria, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypeProgrammationEntity> countRoot = countQuery.from(TypeProgrammationEntity.class);
        buildQuery(criteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<TypeProgrammationEntity> searchQuery = builder.createQuery(TypeProgrammationEntity.class);
        Root<TypeProgrammationEntity> searchRoot = searchQuery.from(TypeProgrammationEntity.class);
        buildQuery(criteria, builder, searchQuery, searchRoot);
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<TypeProgrammationEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<TypeProgrammationEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(TypeProgrammationCriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> query, Root<TypeProgrammationEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //Libelle du type tiers associé
        if (!StringUtils.isEmpty(criteria.getLibelle())) {
            predicateStringCriteria(criteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
        }

        //inactif
        if (criteria.getInactif() != null) {
            predicateCriteriaNullOrNot(!criteria.getInactif(), FIELD_INACTIF, predicates, builder, root);
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
    }
}

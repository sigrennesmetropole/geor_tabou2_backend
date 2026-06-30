package rm.tabou2.storage.tabou.dao.logement.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.constants.FieldsConstants;
import rm.tabou2.storage.tabou.dao.logement.TypeLogementCustomDao;
import rm.tabou2.storage.tabou.entity.logement.TypeLogementEntity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TypeLogementCustomDaoImpl extends AbstractCustomDaoImpl<TypeLogementEntity> implements TypeLogementCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    public Page<TypeLogementEntity> searchTypeLogements(String libelle, Boolean actifUniquement, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Count query
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypeLogementEntity> countRoot = countQuery.from(TypeLogementEntity.class);
        buildQuery(libelle, actifUniquement, builder, countQuery, countRoot);
        countQuery.select(builder.count(countRoot));
        long totalRows = entityManager.createQuery(countQuery).getSingleResult();

        // Search query
        CriteriaQuery<TypeLogementEntity> searchQuery = builder.createQuery(TypeLogementEntity.class);
        Root<TypeLogementEntity> searchRoot = searchQuery.from(TypeLogementEntity.class);
        buildQuery(libelle, actifUniquement, builder, searchQuery, searchRoot);
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<TypeLogementEntity> typedQuery = entityManager.createQuery(searchQuery);
        if (pageable.isPaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());
        }

        List<TypeLogementEntity> result = typedQuery.getResultList();

        return new PageImpl<>(result, pageable, totalRows);
    }

    /**
     * Construction de la requête de recherche.
     *
     * @param libelle         libellé du type de logement
     * @param actifUniquement si true, ne retourne que les types actifs (filtre sur les dates de validité)
     * @param builder         criteria builder
     * @param criteriaQuery   criteria query
     * @param root            root
     */
    private void buildQuery(String libelle, Boolean actifUniquement, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<TypeLogementEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        // Filtre sur le libellé
        predicateStringCriteria(libelle, FieldsConstants.FIELD_LIBELLE, predicates, builder, root);

        // Filtre sur les dates de validité
        predicateActifCriteria(actifUniquement, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));
        }
    }
}


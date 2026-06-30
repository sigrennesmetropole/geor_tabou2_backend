package rm.tabou2.storage.tabou.dao.logement.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.constants.FieldsConstants;
import rm.tabou2.storage.tabou.dao.logement.TypeAccessionLogementCustomDao;
import rm.tabou2.storage.tabou.entity.logement.TypeAccessionLogementEntity;
import rm.tabou2.storage.tabou.item.TypeAccessionLogementCriteria;

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
public class TypeAccessionLogementCustomDaoImpl extends AbstractCustomDaoImpl<TypeAccessionLogementEntity> implements TypeAccessionLogementCustomDao {

    private static final String FIELD_PORTEES = "portees";

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    public Page<TypeAccessionLogementEntity> searchTypeAccessionLogements(TypeAccessionLogementCriteria criteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        // Count query
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypeAccessionLogementEntity> countRoot = countQuery.from(TypeAccessionLogementEntity.class);
        buildQuery(criteria, builder, countQuery, countRoot);
        countQuery.select(builder.count(countRoot));
        long totalRows = entityManager.createQuery(countQuery).getSingleResult();

        // Search query
        CriteriaQuery<TypeAccessionLogementEntity> searchQuery = builder.createQuery(TypeAccessionLogementEntity.class);
        Root<TypeAccessionLogementEntity> searchRoot = searchQuery.from(TypeAccessionLogementEntity.class);
        buildQuery(criteria, builder, searchQuery, searchRoot);
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<TypeAccessionLogementEntity> typedQuery = entityManager.createQuery(searchQuery);
        if (pageable.isPaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());
        }

        List<TypeAccessionLogementEntity> result = typedQuery.getResultList();

        return new PageImpl<>(result, pageable, totalRows);
    }

    /**
     * Construction de la requête de recherche.
     *
     * @param criteria      critères de recherche
     * @param builder       criteria builder
     * @param criteriaQuery criteria query
     * @param root          root
     */
    private void buildQuery(TypeAccessionLogementCriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<TypeAccessionLogementEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        if (criteria != null) {
            // Filtre sur le libellé
            predicateStringCriteria(criteria.getLibelle(), FieldsConstants.FIELD_LIBELLE, predicates, builder, root);

            // Filtre sur les dates de validité
            predicateActifCriteria(criteria.getActifUniquement(), predicates, builder, root);

            // Filtre sur la portée
            if (criteria.getPortee() != null) {
                predicates.add(builder.isMember(criteria.getPortee(), root.get(FIELD_PORTEES)));
            }
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));
        }
    }
}

package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.OperationTiersCustomDao;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NOM;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TYPE_TIERS;

@Repository
public class OperationTiersCustomDaoImpl extends AbstractCustomDaoImpl implements OperationTiersCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<OperationTiersEntity> searchOperationTiers(TiersAmenagementCriteria criteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<OperationTiersEntity> countRoot = countQuery.from(OperationTiersEntity.class);
        buildQuery(criteria, builder, countQuery, countRoot, false);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<OperationTiersEntity> searchQuery = builder.createQuery(OperationTiersEntity.class);
        Root<OperationTiersEntity> searchRoot = searchQuery.from(OperationTiersEntity.class);
        buildQuery(criteria, builder, searchQuery, searchRoot, true);

        TypedQuery<OperationTiersEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<OperationTiersEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());


    }


    private void buildQuery(TiersAmenagementCriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<OperationTiersEntity> root, boolean applyOrderBy) {


        List<Predicate> predicates = new ArrayList<>();

        //Libelle du type tiers associé
        if (criteria.getLibelle() != null && !criteria.getLibelle().isEmpty()) {
            Join<OperationTiersEntity, TypeTiersEntity> typeTiersJoin = root.join(FIELD_TYPE_TIERS);
            predicateStringCriteriaForJoin(criteria.getLibelle(), FIELD_LIBELLE, predicates, builder, typeTiersJoin);
        }

        predicateLongCriteriaForJoin(criteria.getOperationId(), "id", predicates, builder, root.join("operation"));


        //Définition de la clause Where
        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        if (applyOrderBy) {
            // Order by
            applyOrderBy(criteria, builder, criteriaQuery, root);
        }
    }


    private void applyOrderBy(TiersAmenagementCriteria criteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery, Root<OperationTiersEntity> root) {
        if (criteria.getOrderBy() == null || criteria.getOrderBy().equals(FIELD_LIBELLE)) {
            if (criteria.isAsc()) {
                criteriaQuery.orderBy(builder.asc(root.get(FIELD_TYPE_TIERS).get(FIELD_LIBELLE)));
            } else {
                criteriaQuery.orderBy(builder.desc(root.get(FIELD_TYPE_TIERS).get(FIELD_LIBELLE)));
            }
        } else if (criteria.getOrderBy().equals(FIELD_NOM)) {
            if (criteria.isAsc()) {
                criteriaQuery.orderBy(builder.asc(root.get(FIELD_TIERS).get(FIELD_NOM)));
            } else {
                criteriaQuery.orderBy(builder.desc(root.get(FIELD_TIERS).get(FIELD_NOM)));
            }
        } else {
            criteriaQuery.orderBy(builder.asc(root.get(FIELD_ID)));
        }
    }

}

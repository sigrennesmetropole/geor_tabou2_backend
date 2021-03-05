package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.OperationTiersCustomDao;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TYPE_TIERS;

@Repository
public class OperationTiersCustomDaoImpl extends AbstractCustomDaoImpl implements OperationTiersCustomDao {

    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<OperationTiersEntity> searchOperationTiers(String libelleType, Long operationId, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<OperationTiersEntity> countRoot = countQuery.from(OperationTiersEntity.class);
        buildQuery(libelleType, operationId, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<OperationTiersEntity> searchQuery = builder.createQuery(OperationTiersEntity.class);
        Root<OperationTiersEntity> searchRoot = searchQuery.from(OperationTiersEntity.class);
        buildQuery(libelleType, operationId, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<OperationTiersEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<OperationTiersEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());


    }


    private void buildQuery(String libelleType, Long operationId, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<OperationTiersEntity> root
    ) {


        List<Predicate> predicates = new ArrayList<>();

        //Libelle du type tiers associé
        if (libelleType != null && !libelleType.isEmpty()) {
            Join<OperationTiersEntity, TypeTiersEntity> typeTiersJoin = root.join(FIELD_TYPE_TIERS);
            predicateStringCriteriaForJoin(libelleType, FIELD_LIBELLE, predicates, builder, typeTiersJoin);
        }

        predicateLongCriteria(operationId, "operation", predicates, builder, root);


        //Définition de la clause Where
        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }


    }

}

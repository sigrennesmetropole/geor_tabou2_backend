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
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationCustomDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.*;

@Repository
public class EtapeOperationCustomDaoImpl extends AbstractCustomDaoImpl implements EtapeOperationCustomDao {

    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<EtapeOperationEntity> searchEtapeOperations(EtapeCriteria etapeCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<EtapeOperationEntity> countRoot = countQuery.from(EtapeOperationEntity.class);
        buildQuery(etapeCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<EtapeOperationEntity> searchQuery = builder.createQuery(EtapeOperationEntity.class);
        Root<EtapeOperationEntity> searchRoot = searchQuery.from(EtapeOperationEntity.class);
        searchQuery.multiselect(searchRoot.get(FIELD_ID), searchRoot.get(FIELD_CODE), searchRoot.get(FIELD_LIBELLE));
        buildQuery(etapeCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<EtapeOperationEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<EtapeOperationEntity> etapeOperationEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(etapeOperationEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(EtapeCriteria etapeCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<EtapeOperationEntity> root
    ) {
        if (etapeCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            if (etapeCriteria.getCode() != null) {
                predicateStringCriteria(etapeCriteria.getCode(), FIELD_CODE, predicates, builder, root);
            }

            if (etapeCriteria.getLibelle() != null) {
                predicateStringCriteria(etapeCriteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
            }

            if (etapeCriteria.getMode() != null) {
                predicateStringCriteria(etapeCriteria.getMode(), FIELD_MODE, predicates, builder, root);
            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
            }
        }
    }
}

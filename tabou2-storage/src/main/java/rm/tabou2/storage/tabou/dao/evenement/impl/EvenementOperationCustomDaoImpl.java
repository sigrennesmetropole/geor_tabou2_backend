package rm.tabou2.storage.tabou.dao.evenement.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.evenement.EvenementOperationCustomDao;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.*;

@Repository
public class EvenementOperationCustomDaoImpl extends AbstractCustomDaoImpl implements EvenementOperationCustomDao {


    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<EvenementOperationEntity> searchEvenementsOperation(Long operationId,
                                TypeEvenementCriteria typeEvenementCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<EvenementOperationEntity> countRoot = countQuery.from(EvenementOperationEntity.class);
        buildQuery(operationId, typeEvenementCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<EvenementOperationEntity> searchQuery = builder.createQuery(EvenementOperationEntity.class);
        Root<EvenementOperationEntity> searchRoot = searchQuery.from(EvenementOperationEntity.class);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));
        buildQuery(operationId, typeEvenementCriteria, builder, searchQuery, searchRoot);
        TypedQuery<EvenementOperationEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<EvenementOperationEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());

    }

    private void buildQuery(Long operationId, TypeEvenementCriteria typeEventCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<EvenementOperationEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //id de l'operation
        predicateLongCriteria(operationId, FIELD_OPERATION, predicates, builder, root);

        if(typeEventCriteria != null) {

            if (typeEventCriteria.getCode()!= null) {
                predicateStringCriteria(typeEventCriteria.getCode(), root.get(FIELD_TYPE_EVENEMENT)
                                .get(FIELD_CODE), predicates, builder);
            }

            if (typeEventCriteria.getLibelle()!= null) {
                predicateStringCriteria(typeEventCriteria.getCode(), root.get(FIELD_TYPE_EVENEMENT)
                        .get(FIELD_LIBELLE), predicates, builder);
            }

            if (typeEventCriteria.getSysteme() != null) {
                predicateBooleanCriteria(typeEventCriteria.getSysteme(), root.get(FIELD_TYPE_EVENEMENT)
                                .get(FIELD_SYSTEME), predicates, builder);
            }

            // TODO
            // getDateInactif et regarder par rapport à la date now si c'est devenu inactif ou ce n'est pas encore le cas
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }
    }


}

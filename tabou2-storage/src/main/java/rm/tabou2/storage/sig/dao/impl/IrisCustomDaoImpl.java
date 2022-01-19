package rm.tabou2.storage.sig.dao.impl;

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
import rm.tabou2.storage.sig.dao.IrisCustomDao;
import rm.tabou2.storage.sig.entity.IrisEntity;
import rm.tabou2.storage.sig.item.IrisCriteria;

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
public class IrisCustomDaoImpl extends AbstractCustomDaoImpl implements IrisCustomDao {

    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;

    @Override
    @Transactional(transactionManager = "sigTransactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public Page<IrisEntity> searchIris(IrisCriteria irisCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<IrisEntity> countRoot = countQuery.from(IrisEntity.class);
        buildQuery(irisCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<IrisEntity> searchQuery = builder.createQuery(IrisEntity.class);
        Root<IrisEntity> searchRoot = searchQuery.from(IrisEntity.class);
        buildQuery(irisCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<IrisEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<IrisEntity> irisEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(irisEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(IrisCriteria irisCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<IrisEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //nom
        predicateStringCriteria(irisCriteria.getNom(), FIELD_NOM_IRIS, predicates, builder, root);

        //Code iris
        predicateStringCriteria(irisCriteria.getCodeIris(), FIELD_CODE_IRIS, predicates, builder, root);

        //Code insee
        predicateStringCriteria(irisCriteria.getCcom(), FIELD_CCOM, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }
    }

}

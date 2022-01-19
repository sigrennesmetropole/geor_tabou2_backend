package rm.tabou2.storage.sig.dao.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.sig.dao.SecteurFoncierCustomDao;
import rm.tabou2.storage.sig.entity.SecteurFoncierEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NEGOCIATEUR;

@Repository
public class SecteurFoncierCustomDaoImpl extends AbstractCustomDaoImpl implements SecteurFoncierCustomDao {

    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;

    @Override
    @Transactional(transactionManager = "sigTransactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public Page<SecteurFoncierEntity> searchSecteursFoncier(String negociateur, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class).distinct(true);
        Root<SecteurFoncierEntity> countRoot = countQuery.from(SecteurFoncierEntity.class);
        buildQuery(negociateur, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot.get("negociateur")));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<SecteurFoncierEntity> searchQuery = builder.createQuery(SecteurFoncierEntity.class).distinct(true);
        Root<SecteurFoncierEntity> searchRoot = searchQuery.from(SecteurFoncierEntity.class);
        buildQuery(negociateur, builder, searchQuery, searchRoot);
        searchQuery.multiselect(builder.literal(0L), countRoot.get("negociateur"));
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<SecteurFoncierEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<SecteurFoncierEntity> secteurFoncierEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(secteurFoncierEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(String negociateur, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<SecteurFoncierEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //nom du négociateur
        predicateStringCriteria(negociateur, FIELD_NEGOCIATEUR, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }
    }


}

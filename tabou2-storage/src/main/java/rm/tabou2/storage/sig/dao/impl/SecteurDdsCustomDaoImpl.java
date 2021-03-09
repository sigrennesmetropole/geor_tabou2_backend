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
import rm.tabou2.storage.sig.dao.SecteurDdsCustomDao;
import rm.tabou2.storage.sig.entity.SecteurDdsEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_SECTEUR;

@Repository
public class SecteurDdsCustomDaoImpl extends AbstractCustomDaoImpl implements SecteurDdsCustomDao {


    @Qualifier("sigEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(transactionManager = "sigTransactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public Page<SecteurDdsEntity> searchSecteursDds(String secteur, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<SecteurDdsEntity> countRoot = countQuery.from(SecteurDdsEntity.class);
        buildQuery(secteur, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<SecteurDdsEntity> searchQuery = builder.createQuery(SecteurDdsEntity.class);
        Root<SecteurDdsEntity> searchRoot = searchQuery.from(SecteurDdsEntity.class);
        buildQuery(secteur, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<SecteurDdsEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<SecteurDdsEntity> secteurDdsEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(secteurDdsEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(String secteur, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<SecteurDdsEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //nom du secteur
        predicateStringCriteria(secteur, FIELD_SECTEUR, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }
    }


}

package rm.tabou2.storage.tabou.dao.agapeo.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoCustomDao;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NUM_ADS;

@Repository
public class AgapeoCustomDaoImpl extends AbstractCustomDaoImpl implements AgapeoCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<AgapeoEntity> searchAgapeo(String numAds, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<AgapeoEntity> countRoot = countQuery.from(AgapeoEntity.class);
        buildQuery(numAds, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<AgapeoEntity> searchQuery = builder.createQuery(AgapeoEntity.class);
        Root<AgapeoEntity> searchRoot = searchQuery.from(AgapeoEntity.class);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));
        buildQuery(numAds, builder, searchQuery, searchRoot);
        TypedQuery<AgapeoEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<AgapeoEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());

    }

    private void buildQuery(String numAds, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<AgapeoEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //num ads
        predicateStringCriteria(numAds, FIELD_NUM_ADS, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }

    }


}

package rm.tabou2.storage.tabou.dao.ddc.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireCustomDao;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NUM_ADS;

@Repository
public class PermisConstruireCustomDaoImpl extends AbstractCustomDaoImpl implements PermisConstruireCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<PermisConstruireEntity> searchPermisConstruire(String numAds, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<PermisConstruireEntity> countRoot = countQuery.from(PermisConstruireEntity.class);
        buildQuery(numAds, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<PermisConstruireEntity> searchQuery = builder.createQuery(PermisConstruireEntity.class);
        Root<PermisConstruireEntity> searchRoot = searchQuery.from(PermisConstruireEntity.class);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));
        buildQuery(numAds, builder, searchQuery, searchRoot);
        TypedQuery<PermisConstruireEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<PermisConstruireEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());

    }

    private void buildQuery(String numAds, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<PermisConstruireEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //Numero Ads
        predicateStringCriteria(numAds, FIELD_NUM_ADS, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }

    }

}

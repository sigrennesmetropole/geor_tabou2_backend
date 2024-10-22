package rm.tabou2.storage.tabou.dao.tiers.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.tiers.TypeTiersCustomDao;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

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
public class TypeTiersCustomDaoImpl extends AbstractCustomDaoImpl implements TypeTiersCustomDao {

    private static final String FIELD_LIBELLE = "libelle";
    private static final String FIELD_DATE_INACTIF = "dateInactif";


    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    public Page<TypeTiersEntity> searchTypeTiers(String libelle, Boolean inactif, Pageable pageable) {

        List<TypeTiersEntity> result;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TypeTiersEntity> searchQuery = builder.createQuery(TypeTiersEntity.class);
        Root<TypeTiersEntity> searchRoot = searchQuery.from(TypeTiersEntity.class);

        buildQuery(libelle, inactif, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<TypeTiersEntity> typedQuery = entityManager.createQuery(searchQuery);

        int totalRows = typedQuery.getResultList().size();

        result = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        return new PageImpl<>(result, pageable, totalRows);

    }

    /**
     * Construction de la requête de recherche
     *
     * @param libelle libellé du type tiers
     * @param inactif true si inactif
     * @param builder criteria build
     * @param criteriaQuery critria query
     * @param root root
     */
    private void buildQuery(String libelle, Boolean inactif, CriteriaBuilder builder,
                            CriteriaQuery<TypeTiersEntity> criteriaQuery, Root<TypeTiersEntity> root) {


        List<Predicate> predicates = new ArrayList<>();

        //libelle
        predicateStringCriteria(libelle, FIELD_LIBELLE, predicates, builder, root);

        //inactif
        if (inactif != null) {
            predicateCriteriaNullOrNot(!inactif, FIELD_DATE_INACTIF, predicates, builder, root);
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

    }


}

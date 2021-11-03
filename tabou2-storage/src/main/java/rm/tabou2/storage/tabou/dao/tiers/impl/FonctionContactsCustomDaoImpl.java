package rm.tabou2.storage.tabou.dao.tiers.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.tiers.FonctionContactsCustomDao;
import rm.tabou2.storage.tabou.entity.tiers.FonctionContactsEntity;
import rm.tabou2.storage.tabou.item.FonctionContactsCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FonctionContactsCustomDaoImpl extends AbstractCustomDaoImpl implements FonctionContactsCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;
    private static final String FIELD_LIBELLE = "libelle";

    @Override
    public Page<FonctionContactsEntity> searchTiers(FonctionContactsCriteria fonctionContactsCriteria, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<FonctionContactsEntity> countRoot = countQuery.from(FonctionContactsEntity.class);
        buildQuery(fonctionContactsCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<FonctionContactsEntity> searchQuery = builder.createQuery(FonctionContactsEntity.class);
        Root<FonctionContactsEntity> searchRoot = searchQuery.from(FonctionContactsEntity.class);
        buildQuery(fonctionContactsCriteria, builder, searchQuery, searchRoot);
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<FonctionContactsEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<FonctionContactsEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(FonctionContactsCriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> query, Root<FonctionContactsEntity> root){
        if(criteria != null){
            List<Predicate> predicates = new ArrayList<>();

            if(criteria.getLibelle() != null){
                predicateStringCriteria(criteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
            }
            //Définition de la clause Where
            if (CollectionUtils.isNotEmpty(predicates)) {
                query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }


    }
}

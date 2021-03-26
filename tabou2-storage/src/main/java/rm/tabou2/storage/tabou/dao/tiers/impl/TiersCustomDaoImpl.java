package rm.tabou2.storage.tabou.dao.tiers.impl;

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
import rm.tabou2.storage.tabou.dao.tiers.TiersCustomDao;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.TiersCriteria;

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
public class TiersCustomDaoImpl extends AbstractCustomDaoImpl implements TiersCustomDao {

    //Champs utilisés pour le filtrage
    public static final String FIELD_NOM = "nom";
    public static final String FIELD_ADRESSE_VILLE = "adresseVille";
    public static final String FIELD_EST_PRIVE = "estPrive";
    public static final String FIELD_INACTIF = "dateInactif";

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<TiersEntity> searchTiers(TiersCriteria tiersCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TiersEntity> countRoot = countQuery.from(TiersEntity.class);
        buildQuery(tiersCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<TiersEntity> searchQuery = builder.createQuery(TiersEntity.class);
        Root<TiersEntity> searchRoot = searchQuery.from(TiersEntity.class);
        buildQuery(tiersCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<TiersEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<TiersEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());


    }


    private void buildQuery(TiersCriteria tiersCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<TiersEntity> root
    ) {

        if (tiersCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            //nom
            predicateStringCriteria(tiersCriteria.getNom(), FIELD_NOM, predicates, builder, root);

            //ville
            predicateStringCriteria(tiersCriteria.getAdresseVille(), FIELD_ADRESSE_VILLE, predicates, builder, root);

            //tiers prive ou societe
            if (tiersCriteria.getTiersPrive() != null) {
                predicateBooleanCriteria(tiersCriteria.getTiersPrive(), FIELD_EST_PRIVE, predicates, builder, root);
            }

            //inactif
            if (tiersCriteria.getInactif() != null) {
                predicateCriteriaNullOrNot(!tiersCriteria.getInactif(), FIELD_INACTIF, predicates, builder, root);
            }


            //Définition de la clause Where
            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }


        }
    }


}

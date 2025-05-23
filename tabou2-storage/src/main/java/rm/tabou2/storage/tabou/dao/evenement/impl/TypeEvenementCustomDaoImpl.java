package rm.tabou2.storage.tabou.dao.evenement.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementCustomDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

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
public class TypeEvenementCustomDaoImpl extends AbstractCustomDaoImpl implements TypeEvenementCustomDao {

    public static final String FIELD_LIBELLE = "libelle";
    public static final String FIELD_SYSTEME = "systeme";
    public static final String FIELD_DATE_INACTIF = "dateInactif";

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    public Page<TypeEvenementEntity> searchTypeEvenement(TypeEvenementCriteria typeEvenementCriteria, Pageable pageable) {


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypeEvenementEntity> countRoot = countQuery.from(TypeEvenementEntity.class);
        buildQuery(typeEvenementCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<TypeEvenementEntity> searchQuery = builder.createQuery(TypeEvenementEntity.class);
        Root<TypeEvenementEntity> searchRoot = searchQuery.from(TypeEvenementEntity.class);

        buildQuery(typeEvenementCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<TypeEvenementEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<TypeEvenementEntity> typeEvenementEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(typeEvenementEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(TypeEvenementCriteria typeEvenementCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<TypeEvenementEntity> searchRoot) {

        List<Predicate> predicates = new ArrayList<>();


        if (typeEvenementCriteria.getLibelle() != null) {
            predicateStringCriteria(typeEvenementCriteria.getLibelle(), FIELD_LIBELLE, predicates, builder, searchRoot);

        }


        predicateBooleanCriteria(typeEvenementCriteria.getSysteme(), FIELD_SYSTEME, predicates, builder, searchRoot);


        //Si on veut les evenements inactifs
        if (typeEvenementCriteria.getInactif() != null) {
                predicateCriteriaNullOrNot(!typeEvenementCriteria.getInactif(), FIELD_DATE_INACTIF, predicates, builder, searchRoot);
        }


        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

    }

}

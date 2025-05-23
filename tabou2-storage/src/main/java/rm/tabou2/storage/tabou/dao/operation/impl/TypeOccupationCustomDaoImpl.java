package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringUtils;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.TypeOccupationCustomDao;
import rm.tabou2.storage.tabou.entity.operation.TypeOccupationEntity;
import rm.tabou2.storage.tabou.item.TypeOccupationCriteria;

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
public class TypeOccupationCustomDaoImpl extends AbstractCustomDaoImpl implements TypeOccupationCustomDao {
    private static final String FIELD_LIBELLE = "libelle";
    private static final String FIELD_CODE = "code";

    @PersistenceContext(unitName = "tabouPU")
    EntityManager entityManager;

    @Override
    public Page<TypeOccupationEntity> searchTypeOccupation(TypeOccupationCriteria criteria, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypeOccupationEntity> countRoot = countQuery.from(TypeOccupationEntity.class);
        buildQuery(criteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<TypeOccupationEntity> searchQuery = builder.createQuery(TypeOccupationEntity.class);
        Root<TypeOccupationEntity> searchRoot = searchQuery.from(TypeOccupationEntity.class);
        buildQuery(criteria, builder, searchQuery, searchRoot);
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<TypeOccupationEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<TypeOccupationEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(TypeOccupationCriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> query, Root<TypeOccupationEntity> root){
        if(criteria != null){
            List<Predicate> predicates = new ArrayList<>();

            if(!StringUtils.isEmpty(criteria.getLibelle())){
                predicateStringCriteria(criteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
            }

            if(!StringUtils.isEmpty(criteria.getCode())){
                predicateStringCriteria(criteria.getCode(), FIELD_CODE, predicates, builder, root);
            }

            if(CollectionUtils.isNotEmpty(predicates)){
                query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }
    }
}

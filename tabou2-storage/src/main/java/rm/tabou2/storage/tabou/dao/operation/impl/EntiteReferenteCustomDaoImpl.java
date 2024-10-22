package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringUtils;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.EntiteReferenteCustomDao;
import rm.tabou2.storage.tabou.entity.operation.EntiteReferenteEntity;
import rm.tabou2.storage.tabou.item.EntiteReferenteCriteria;

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
public class EntiteReferenteCustomDaoImpl extends AbstractCustomDaoImpl implements EntiteReferenteCustomDao {
    private static final String FIELD_LIBELLE = "libelle";
    private static final String FIELD_CODE = "code";

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    public Page<EntiteReferenteEntity> searchEntitesReferentes(EntiteReferenteCriteria criteria, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<EntiteReferenteEntity> countRoot = countQuery.from(EntiteReferenteEntity.class);
        buildQuery(criteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<EntiteReferenteEntity> searchQuery = builder.createQuery(EntiteReferenteEntity.class);
        Root<EntiteReferenteEntity> searchRoot = searchQuery.from(EntiteReferenteEntity.class);
        buildQuery(criteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<EntiteReferenteEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<EntiteReferenteEntity> operationEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(operationEntities, pageable, totalCount.intValue());

    }

    /**
     * Constructeur de la query de recherche des entités référentes
     * @param criteria Critères de recherche
     * @param builder Criteria build
     * @param criteriaQuery Criteria Query
     * @param root Root
     */
    private void buildQuery(EntiteReferenteCriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<EntiteReferenteEntity> root) {

        if(criteria != null){
            List<Predicate> predicates = new ArrayList<>();

            //libelle
            if(!StringUtils.isEmpty(criteria.getLibelle())){
                predicateStringCriteria(criteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
            }

            //code
            if(!StringUtils.isEmpty(criteria.getCode())){
                predicateStringCriteria(criteria.getCode(), FIELD_CODE, predicates, builder, root);
            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }

        }
    }
}

package rm.tabou2.storage.tabou.dao.document.impl;


import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.document.TypeDocumentCustomDao;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;
import rm.tabou2.storage.tabou.item.TypeDocumentCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Repository
public class TypeDocumentCustomDaoImpl extends AbstractCustomDaoImpl implements TypeDocumentCustomDao {

    public static final String FIELD_ID= "id";
    public static final String FIELD_LIBELLE = "libelle";
    public static final String FIELD_DATE_INACTIF = "dateInactif";


    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<TypeDocumentEntity> searchTypeDocument(TypeDocumentCriteria typeDocumentCriteria, Pageable pageable) {


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypeDocumentEntity> countRoot = countQuery.from(TypeDocumentEntity.class);
        buildQuery(typeDocumentCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche


        CriteriaQuery<TypeDocumentEntity> searchQuery = builder.createQuery(TypeDocumentEntity.class);
        Root<TypeDocumentEntity> searchRoot = searchQuery.from(TypeDocumentEntity.class);

        buildQuery(typeDocumentCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<TypeDocumentEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<TypeDocumentEntity> typeDocumentEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(typeDocumentEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(TypeDocumentCriteria typeDocumentCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<TypeDocumentEntity> searchRoot) {

        List<Predicate> predicates = new ArrayList<>();

        if( typeDocumentCriteria.getId() != null) {

            predicates.add(builder.equal(searchRoot.get(FIELD_ID), typeDocumentCriteria.getId()));

        }else{

            if(typeDocumentCriteria.getLibelle() != null){

                predicateStringCriteria(typeDocumentCriteria.getLibelle(), FIELD_LIBELLE, predicates, builder, searchRoot);

            }
            if(typeDocumentCriteria.getDateInactif() != null){
                predicates.add(builder.equal(searchRoot.get(FIELD_DATE_INACTIF), typeDocumentCriteria.getDateInactif()));
            }
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

    }
}

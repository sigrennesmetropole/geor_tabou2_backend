package rm.tabou2.storage.tabou.dao.impl;


import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.dao.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.TypeDocumentCustomDao;
import rm.tabou2.storage.tabou.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class TypeDocumentCustomDaoImpl extends AbstractCustomDaoImpl implements TypeDocumentCustomDao {

    
    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<TypeDocumentEntity> searchTypeDocument(Long id, String libelle, Date dateInactif, Pageable pageable) {


        List<TypeDocumentEntity> result;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TypeDocumentEntity> searchQuery = builder.createQuery(TypeDocumentEntity.class);
        Root<TypeDocumentEntity> searchRoot = searchQuery.from(TypeDocumentEntity.class);

        buildQuery(id, libelle, dateInactif, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<TypeDocumentEntity> typedQuery = entityManager.createQuery(searchQuery);

        int totalRows = typedQuery.getResultList().size();

        result = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        return new PageImpl<>(result, pageable, totalRows);
    }

    private void buildQuery(Long id, String libelle, Date dateInactif, CriteriaBuilder builder,
                            CriteriaQuery<TypeDocumentEntity> criteriaQuery, Root<TypeDocumentEntity> searchRoot) {

        List<Predicate> predicates = new ArrayList<>();

        if( id != null) {

            predicates.add(builder.equal(searchRoot.get("id"), id));

        }else{

            if(libelle != null){

                predicateStringCriteria(libelle, "libelle", predicates, builder, searchRoot);

            }
            if(dateInactif != null){
                predicates.add(builder.equal(searchRoot.get("dateInactif"), dateInactif));
            }
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

    }
}

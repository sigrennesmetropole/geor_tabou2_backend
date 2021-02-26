package rm.tabou2.storage.sig.dao.impl;

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
import rm.tabou2.storage.sig.dao.CommuneCustomDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.*;

@Repository
public class CommuneCustomDaoImpl extends AbstractCustomDaoImpl implements CommuneCustomDao {

    @Qualifier("sigEntityManager")
    @Autowired
    private EntityManager sigEntityManager;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<CommuneEntity> searchCommunes(String nom, String codeInsee, Pageable pageable) {

        CriteriaBuilder builder = sigEntityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<CommuneEntity> countRoot = countQuery.from(CommuneEntity.class);
        buildQuery(nom, codeInsee, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = sigEntityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<CommuneEntity> searchQuery = builder.createQuery(CommuneEntity.class);
        Root<CommuneEntity> searchRoot = searchQuery.from(CommuneEntity.class);
        buildQuery(nom, codeInsee, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<CommuneEntity> typedQuery = sigEntityManager.createQuery(searchQuery);
        List<CommuneEntity> communeEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(communeEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(String nom, String codeInsee, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<CommuneEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //nom
        predicateStringCriteria(nom, FIELD_NOM, predicates, builder, root);

        //Code insee
        predicateStringCriteria(codeInsee, FIELD_CODE_INSEE, predicates, builder, root);

        //Commune agglo : paramètre fixe
        predicateIntegerCriteria(1, FIELD_COMMUNE_AGGLO, predicates, builder, root);


        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }
    }

}

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
import rm.tabou2.storage.sig.dao.QuartierCustomDao;
import rm.tabou2.storage.sig.entity.QuartierEntity;
import rm.tabou2.storage.sig.item.QuartierCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.*;

@Repository
public class QuartierCustomDaoImpl  extends AbstractCustomDaoImpl implements QuartierCustomDao {

    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<QuartierEntity> searchQuartiers(QuartierCriteria quartierCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<QuartierEntity> countRoot = countQuery.from(QuartierEntity.class);
        buildQuery(quartierCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<QuartierEntity> searchQuery = builder.createQuery(QuartierEntity.class);
        Root<QuartierEntity> searchRoot = searchQuery.from(QuartierEntity.class);
        buildQuery(quartierCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<QuartierEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<QuartierEntity> quartierEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(quartierEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(QuartierCriteria quartierCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<QuartierEntity> root) {


        if (quartierCriteria != null) {

            List<Predicate> predicates = new ArrayList<>();

            //nom
            predicateStringCriteria(quartierCriteria.getNom(), FIELD_NOM, predicates, builder, root);

            //Code insee
            predicateIntegerCriteria(quartierCriteria.getCodeInsee(), FIELD_CODE_INSEE, predicates, builder, root);

            //Numéro de quartier
            predicateIntegerCriteria(quartierCriteria.getNuQuart(), FIELD_NU_QUART, predicates, builder, root);

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
            }
        }
    }

}

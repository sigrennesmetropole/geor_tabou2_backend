package rm.tabou2.storage.sig.dao.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.sig.dao.SecteurSamCustomDao;
import rm.tabou2.storage.sig.entity.SecteurSamEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NOM_SECTEUR;

@Repository
public class SecteurSamCustomDaoImpl extends AbstractCustomDaoImpl implements SecteurSamCustomDao {


    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;

    @Override
    @Transactional(transactionManager = "sigTransactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public Page<SecteurSamEntity> searchSecteursSam(String secteur, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class).distinct(true);
        Root<SecteurSamEntity> countRoot = countQuery.from(SecteurSamEntity.class);
        buildQuery(secteur, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot.get("nomSecteur")));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<SecteurSamEntity> searchQuery = builder.createQuery(SecteurSamEntity.class).distinct(true);
        Root<SecteurSamEntity> searchRoot = searchQuery.from(SecteurSamEntity.class);
        buildQuery(secteur, builder, searchQuery, searchRoot);
        searchQuery.multiselect(builder.literal(0), searchRoot.get("nomSecteur"));
        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<SecteurSamEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<SecteurSamEntity> secteurSamEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(secteurSamEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(String secteur, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<SecteurSamEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //nom du secteur
        predicateStringCriteria(secteur, FIELD_NOM_SECTEUR, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }
    }

}

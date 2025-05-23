package rm.tabou2.storage.tabou.dao.programme.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeCustomDao;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.*;

@Repository
public class EtapeProgrammeCustomDaoImpl extends AbstractCustomDaoImpl implements EtapeProgrammeCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<EtapeProgrammeEntity> searchEtapeProgrammes(EtapeCriteria etapeCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<EtapeProgrammeEntity> countRoot = countQuery.from(EtapeProgrammeEntity.class);
        buildQuery(etapeCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<EtapeProgrammeEntity> searchQuery = builder.createQuery(EtapeProgrammeEntity.class);
        Root<EtapeProgrammeEntity> searchRoot = searchQuery.from(EtapeProgrammeEntity.class);
        searchQuery.multiselect(searchRoot.get(FIELD_ID), searchRoot.get(FIELD_CODE), searchRoot.get(FIELD_LIBELLE));
        buildQuery(etapeCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<EtapeProgrammeEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<EtapeProgrammeEntity> etapeProgrammeEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(etapeProgrammeEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(EtapeCriteria etapeCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<EtapeProgrammeEntity> root
    ) {
        if (etapeCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            if (etapeCriteria.getCode() != null) {
                predicateStringCriteria(etapeCriteria.getCode(), FIELD_CODE, predicates, builder, root);
            }

            if (etapeCriteria.getLibelle() != null) {
                predicateStringCriteria(etapeCriteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
            }

            if (etapeCriteria.getMode() != null) {
                predicateStringCriteria(etapeCriteria.getMode(), FIELD_MODE, predicates, builder, root);
            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
            }
        }
    }
}

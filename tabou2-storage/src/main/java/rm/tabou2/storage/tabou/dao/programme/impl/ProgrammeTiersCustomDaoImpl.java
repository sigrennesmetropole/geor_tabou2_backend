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
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersCustomDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CODE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NOM;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_PROGRAMME;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TYPE_TIERS;

@Repository
public class ProgrammeTiersCustomDaoImpl extends AbstractCustomDaoImpl implements ProgrammeTiersCustomDao {


    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<ProgrammeTiersEntity> searchProgrammesTiers(TiersAmenagementCriteria criteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<ProgrammeTiersEntity> countRoot = countQuery.from(ProgrammeTiersEntity.class);
        buildQuery(criteria, builder, countQuery, countRoot, false);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<ProgrammeTiersEntity> searchQuery = builder.createQuery(ProgrammeTiersEntity.class);
        Root<ProgrammeTiersEntity> searchRoot = searchQuery.from(ProgrammeTiersEntity.class);
        buildQuery(criteria, builder, searchQuery, searchRoot, true);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<ProgrammeTiersEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<ProgrammeTiersEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());


    }


    private void buildQuery(TiersAmenagementCriteria criteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<ProgrammeTiersEntity> root, boolean applyOrderBy
    ) {


        List<Predicate> predicates = new ArrayList<>();

        //Libelle du type tiers associé
        if (criteria.getLibelle() != null && !criteria.getLibelle().isEmpty()) {
            Join<ProgrammeTiersEntity, TypeTiersEntity> typeTiersJoin = root.join(FIELD_TYPE_TIERS);
            predicateStringCriteriaForJoin(criteria.getLibelle(), FIELD_LIBELLE, predicates, builder, typeTiersJoin);
        }

        //Code du type de tiers associé
        if (criteria.getCodeTypeTiers() != null && !criteria.getCodeTypeTiers().isEmpty()) {
            Join<ProgrammeTiersEntity, TypeTiersEntity> typeTiersJoin = root.join(FIELD_TYPE_TIERS);
            predicateStringCriteriaForJoin(criteria.getCodeTypeTiers(), FIELD_CODE, predicates, builder, typeTiersJoin);
        }

        predicateLongCriteriaForJoin(criteria.getProgrammeId(), FIELD_ID , predicates, builder, root.join(FIELD_PROGRAMME));

        //Définition de la clause Where
        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        if (applyOrderBy) {
            // Order by
            applyOrderBy(criteria, builder, criteriaQuery, root);
        }


    }

    private void applyOrderBy(TiersAmenagementCriteria criteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery, Root<ProgrammeTiersEntity> root) {

        if (criteria.getOrderBy() == null || criteria.getOrderBy().equals(FIELD_LIBELLE)) {
            if (criteria.isAsc()) {
                criteriaQuery.orderBy(builder.asc(root.get(FIELD_TYPE_TIERS).get(FIELD_LIBELLE)));
            } else {
                criteriaQuery.orderBy(builder.desc(root.get(FIELD_TYPE_TIERS).get(FIELD_LIBELLE)));
            }
        } else if (criteria.getOrderBy().equals(FIELD_NOM)) {
            if (criteria.isAsc()) {
                criteriaQuery.orderBy(builder.asc(root.get(FIELD_TIERS).get(FIELD_NOM)));
            } else {
                criteriaQuery.orderBy(builder.desc(root.get(FIELD_TIERS).get(FIELD_NOM)));
            }
        } else {
            criteriaQuery.orderBy(builder.asc(root.get(FIELD_ID)));
        }
    }


}

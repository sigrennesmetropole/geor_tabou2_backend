package rm.tabou2.storage.tabou.dao.programme.impl;

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
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersCustomDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.*;

@Repository
public class ProgrammeTiersCustomDaoImpl extends AbstractCustomDaoImpl implements ProgrammeTiersCustomDao {


    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<ProgrammeTiersEntity> searchProgrammesTiers(String libelleType, String codeType, Long programmeId, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<ProgrammeTiersEntity> countRoot = countQuery.from(ProgrammeTiersEntity.class);
        buildQuery(libelleType, codeType, programmeId, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<ProgrammeTiersEntity> searchQuery = builder.createQuery(ProgrammeTiersEntity.class);
        Root<ProgrammeTiersEntity> searchRoot = searchQuery.from(ProgrammeTiersEntity.class);
        buildQuery(libelleType, codeType, programmeId, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<ProgrammeTiersEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<ProgrammeTiersEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());


    }


    private void buildQuery(String libelleType, String codeType, Long programmeId, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<ProgrammeTiersEntity> root
    ) {


        List<Predicate> predicates = new ArrayList<>();

        //Libelle du type tiers associé
        if (libelleType != null && !libelleType.isEmpty()) {
            Join<ProgrammeTiersEntity, TypeTiersEntity> typeTiersJoin = root.join(FIELD_TYPE_TIERS);
            predicateStringCriteriaForJoin(libelleType, FIELD_LIBELLE, predicates, builder, typeTiersJoin);
        }

        //Code du type de tiers associé
        if (codeType != null && !codeType.isEmpty()) {
            Join<ProgrammeTiersEntity, TypeTiersEntity> typeTiersJoin = root.join(FIELD_TYPE_TIERS);
            predicateStringCriteriaForJoin(codeType, FIELD_CODE, predicates, builder, typeTiersJoin);
        }

        predicateLongCriteria(programmeId, "programme", predicates, builder, root);


        //Définition de la clause Where
        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }


    }


}

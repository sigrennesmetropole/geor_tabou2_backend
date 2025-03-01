package rm.tabou2.storage.tabou.dao.evenement.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.evenement.EvenementProgrammeCustomDao;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CODE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_PROGRAMME;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_SYSTEME;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TYPE_EVENEMENT;

@Repository
public class EvenementProgrammeCustomDaoImpl extends AbstractCustomDaoImpl implements EvenementProgrammeCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<EvenementProgrammeEntity> searchEvenementsProgramme(Long programmeId, TypeEvenementCriteria typeEvenementCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<EvenementProgrammeEntity> countRoot = countQuery.from(EvenementProgrammeEntity.class);
        buildQuery(programmeId, builder, typeEvenementCriteria, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<EvenementProgrammeEntity> searchQuery = builder.createQuery(EvenementProgrammeEntity.class);
        Root<EvenementProgrammeEntity> searchRoot = searchQuery.from(EvenementProgrammeEntity.class);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));
        buildQuery(programmeId, builder, typeEvenementCriteria, searchQuery, searchRoot);
        TypedQuery<EvenementProgrammeEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<EvenementProgrammeEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());

    }

    private void buildQuery(Long programmeId, CriteriaBuilder builder, TypeEvenementCriteria typeEvenementCriteria,
                            CriteriaQuery<?> criteriaQuery, Root<EvenementProgrammeEntity> root
    ) {

        List<Predicate> predicates = new ArrayList<>();

        //id du programme
        predicateLongCriteriaForJoin(programmeId, FIELD_ID, predicates, builder, root.join(FIELD_PROGRAMME));

        if(typeEvenementCriteria != null) {

            if (typeEvenementCriteria.getCode()!= null) {
                predicateStringCriteria(typeEvenementCriteria.getCode(), root.get(FIELD_TYPE_EVENEMENT)
                        .get(FIELD_CODE), predicates, builder);
            }

            if (typeEvenementCriteria.getLibelle()!= null) {
                predicateStringCriteria(typeEvenementCriteria.getCode(), root.get(FIELD_TYPE_EVENEMENT)
                        .get(FIELD_LIBELLE), predicates, builder);
            }

            if (typeEvenementCriteria.getSysteme() != null) {
                predicateBooleanCriteria(typeEvenementCriteria.getSysteme(), root.get(FIELD_TYPE_EVENEMENT)
                        .get(FIELD_SYSTEME), predicates, builder);
            }

            // TODO
            // getDateInactif et regarder par rapport à la date now si c'est devenu inactif ou ce n'est pas encore le cas
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }

    }

}

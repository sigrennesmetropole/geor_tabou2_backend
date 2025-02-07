package rm.tabou2.storage.tabou.dao.programme.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.programme.TypePLHCustomDao;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.item.TypePLHCriteria;

import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CODE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_DATE_DEBUT;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_FILS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;

@Repository
public class TypePLHCustomDaoImpl extends AbstractCustomDaoImpl implements TypePLHCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public TypePLHEntity getParentById(long filsId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TypePLHEntity> query = builder.createQuery(TypePLHEntity.class);
        Root<TypePLHEntity> root = query.from(TypePLHEntity.class);

        query.where(
                builder.equal(root.join(FIELD_FILS).get(FIELD_ID), filsId));
        query.select(root);

        TypedQuery<TypePLHEntity> resultQuery = entityManager.createQuery(query);

        TypePLHEntity resultat;
        try {
            resultat = resultQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new NullPointerException("Aucun parent n'a été trouvé pour le typePLH" + filsId);
        }

        return resultat;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<TypePLHEntity> searchTypePLHs(TypePLHCriteria typePLHCriteria, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypePLHEntity> countRoot = countQuery.from(TypePLHEntity.class);
        buildQuery(typePLHCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<TypePLHEntity> searchQuery = builder.createQuery(TypePLHEntity.class);
        Root<TypePLHEntity> searchRoot = searchQuery.from(TypePLHEntity.class);
        searchQuery.multiselect(searchRoot.get(FIELD_ID), searchRoot.get(FIELD_CODE), searchRoot.get(FIELD_LIBELLE));
        buildQuery(typePLHCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<TypePLHEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<TypePLHEntity> etapeProgrammeEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(etapeProgrammeEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(TypePLHCriteria typePLHCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<TypePLHEntity> root
    ) {
        if (typePLHCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            if (typePLHCriteria.getLibelle() != null) {
                predicateStringCriteria(typePLHCriteria.getLibelle(), FIELD_LIBELLE, predicates, builder, root);
            }

            if (typePLHCriteria.getDateDebut() != null) {
                predicateDateCriteria(typePLHCriteria.getDateDebut(), typePLHCriteria.getDateDebut(), FIELD_DATE_DEBUT, predicates, builder, root);
            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
            }
        }
    }

}

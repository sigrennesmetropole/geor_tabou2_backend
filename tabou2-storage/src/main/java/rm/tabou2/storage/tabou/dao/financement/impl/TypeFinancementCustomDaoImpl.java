package rm.tabou2.storage.tabou.dao.financement.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.financement.TypeFinancementCustomDao;
import rm.tabou2.storage.tabou.entity.financement.TypeFinancementEntity;
import rm.tabou2.storage.tabou.item.TypeFinancementCriteria;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Repository
public class TypeFinancementCustomDaoImpl extends AbstractCustomDaoImpl implements TypeFinancementCustomDao {

    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<TypeFinancementEntity> searchTypeFinancement(TypeFinancementCriteria typeFinancementCriteria, Pageable pageable) {


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TypeFinancementEntity> countRoot = countQuery.from(TypeFinancementEntity.class);
        buildQuery(typeFinancementCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<TypeFinancementEntity> searchQuery = builder.createQuery(TypeFinancementEntity.class);
        Root<TypeFinancementEntity> searchRoot = searchQuery.from(TypeFinancementEntity.class);

        buildQuery(typeFinancementCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<TypeFinancementEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<TypeFinancementEntity> typeEvenementEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(typeEvenementEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(TypeFinancementCriteria typeFinancementCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<TypeFinancementEntity> searchRoot) {

        List<Predicate> predicates = new ArrayList<>();


        if (typeFinancementCriteria.getLibelle() != null) {
            predicateStringCriteria(typeFinancementCriteria.getLibelle(), FIELD_LIBELLE, predicates, builder, searchRoot);

        }


        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

    }

}

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
import rm.tabou2.storage.sig.dao.SecteurSpeuCustomDao;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

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
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NUM_SECTEUR;

@Repository
public class SecteurSpeuCustomDaoImpl extends AbstractCustomDaoImpl implements SecteurSpeuCustomDao {


    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;

    @Override
    @Transactional(transactionManager = "sigTransactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public Page<SecteurSpeuEntity> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<SecteurSpeuEntity> countRoot = countQuery.from(SecteurSpeuEntity.class);
        buildQuery(numSecteur, nomSecteur, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<SecteurSpeuEntity> searchQuery = builder.createQuery(SecteurSpeuEntity.class);
        Root<SecteurSpeuEntity> searchRoot = searchQuery.from(SecteurSpeuEntity.class);
        buildQuery(numSecteur, nomSecteur, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<SecteurSpeuEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<SecteurSpeuEntity> secteurSpeuEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(secteurSpeuEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(Integer numSecteur, String nomSecteur, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<SecteurSpeuEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //Numéro du secteur
        predicateIntegerCriteria(numSecteur, FIELD_NUM_SECTEUR, predicates, builder, root);

        //Nom du secteur
        predicateStringCriteria(nomSecteur, FIELD_NOM_SECTEUR, predicates, builder, root);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }
    }

}



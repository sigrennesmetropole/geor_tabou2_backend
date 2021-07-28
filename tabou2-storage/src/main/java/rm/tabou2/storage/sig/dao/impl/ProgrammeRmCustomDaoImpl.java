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
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.sig.dao.ProgrammeRmCustomDao;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du DAO d'accès aux Programme Rennes Metropole (oa_programme).
 */
@Repository
public class ProgrammeRmCustomDaoImpl extends AbstractCustomDaoImpl implements ProgrammeRmCustomDao {

    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<ProgrammeRmEntity> searchEmprisesNonSuivies(Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<ProgrammeRmEntity> countRoot = countQuery.from(ProgrammeRmEntity.class);
        buildQuery(builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<ProgrammeRmEntity> searchQuery = builder.createQuery(ProgrammeRmEntity.class);
        Root<ProgrammeRmEntity> searchRoot = searchQuery.from(ProgrammeRmEntity.class);
        buildQuery(builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<ProgrammeRmEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<ProgrammeRmEntity> tiersEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(tiersEntities, pageable, totalCount.intValue());


    }


    /**
     * Recherche les programmes d'une opération (de type secteur) par intersection spatiale.
     *
     * @param programmeCriteria paramètres de recherche.
     * @param pageable          paramètres de pagination
     * @return Programmes correspondants à la recherche
     */
    @Override
    public Page<ProgrammeRmEntity> searchProgrammesWithinOperation(ProgrammeCriteria programmeCriteria, Pageable pageable) {

        //Appel à la procédure stockée programmes_of_operation : retourne les programmesRm contenu dans l'opération

        String nom = programmeCriteria.getNom();
        if (nom != null && !nom.isEmpty()) {
            nom = nom.replace('*', '%');
        } else {
            nom = "%";
        }

        String baseQuery = "FROM urba_foncier.oa_secteur zs " +
                "          LEFT JOIN urba_foncier.oa_programme zp ON ST_Intersects(zp.shape, zs.shape) " +
                "          WHERE zs.id_tabou = :idOperationParam " +
                "          AND lower(zp.programme) like lower(:nomParam) " +
                "          AND zp.id_tabou is not null";

        //Requête pour compter le nombre de résultats
        final String countQuery = "select count(*) " + baseQuery;
        BigInteger totalCount = (BigInteger) entityManager.createNativeQuery(countQuery)
                .setParameter("idOperationParam", programmeCriteria.getOperationId())
                .setParameter("nomParam", nom)
                .getSingleResult();

        //Si aucun résultat
        if (totalCount.intValue() == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }



        String sqlQuery = "SELECT zp.objectid, zp.programme, zp.id_tabou " + baseQuery;

        List<ProgrammeRmEntity> results = entityManager.createNativeQuery(sqlQuery, ProgrammeRmEntity.class)
                .setParameter("idOperationParam", programmeCriteria.getOperationId())
                .setParameter("nomParam", nom)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();


        return new PageImpl<>(results, pageable, totalCount.intValue());
    }


    private void buildQuery(CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery, Root<ProgrammeRmEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //Id tabou null
        predicateCriteriaNullOrNot(true, "idTabou", predicates, builder, root);

        //Définition de la clause Where
        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }


    }
}

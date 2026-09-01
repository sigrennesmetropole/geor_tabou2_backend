package rm.tabou2.storage.sig.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.sig.dao.ProgrammeRmCustomDao;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.dao.constants.FieldsConstants;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du DAO d'accès aux Programme Rennes Metropole (oa_programme).
 */
@Repository
public class ProgrammeRmCustomDaoImpl extends AbstractCustomDaoImpl<ProgrammeRmEntity> implements ProgrammeRmCustomDao {

    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;


    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<ProgrammeRmEntity> searchEmprisesNonSuivies(Long operationId, String nom, Pageable pageable) {

        boolean hasOperationIdParam = (operationId != null && operationId > 0);
        boolean hasNomParam = nom != null && !nom.equals("");

        String baseQuery =
                "FROM " +
                        "urba_foncier.oa_programme PA " +
                        "INNER JOIN (" +
                        "SELECT " +
                        "OAe.shape " +
                        "FROM (" +
                        "SELECT zac.id_Tabou, zac.shape " +
                        "FROM urba_foncier.zac zac " +
                        "UNION ALL " +
                        "SELECT za.id_Tabou, za.shape " +
                        "FROM economie.za za " +
                        "UNION ALL " +
                        "SELECT oa_1.id_Tabou, oa_1.shape " +
                        "FROM urba_foncier.oa_limite_intervention oa_1) OAe ";
        if (hasOperationIdParam) {
            baseQuery = baseQuery + "WHERE OAe.id_Tabou = :idTabou";
        }
        baseQuery = baseQuery + ") OA " +
                "ON st_intersects(PA.shape, OA.shape) " +
                "WHERE PA.id_tabou is null";

        if (hasNomParam) {
            baseQuery = baseQuery + " AND PA.programme = :nomProgramme";
        }

        //Requête pour compter le nombre de résultats
        final String countQuery = "select count(*) " + baseQuery;

        Query totalCountQuery = entityManager.createNativeQuery(countQuery, Long.class);
        if (hasOperationIdParam) {
            totalCountQuery.setParameter(FieldsConstants.FIELD_ID_TABOU, operationId);
        }
        if (hasNomParam) {
            totalCountQuery.setParameter("nomProgramme", nom);
        }
        long totalCount = (long) totalCountQuery.getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        String sqlQuery = "SELECT PA.objectid, PA.programme, PA.id_tabou " + baseQuery;

        Query resultsQuery = entityManager.createNativeQuery(sqlQuery, ProgrammeRmEntity.class);
        if (hasOperationIdParam) {
            resultsQuery.setParameter(FieldsConstants.FIELD_ID_TABOU, operationId);
        }
        if (hasNomParam) {
            resultsQuery.setParameter("nomProgramme", nom);
        }
        List<ProgrammeRmEntity> results = resultsQuery
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();

        return new PageImpl<>(results, pageable, totalCount);

    }


    /**
     * Recherche les programmes d'une opération (de type secteur) par intersection spatiale.
     *
     * @param programmeCriteria paramètres de recherche.
     * @param pageable          paramètres de pagination
     * @return Programmes correspondants à la recherche
     */
    @Override
    @SuppressWarnings("unchecked")
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
        Query countTypedQuery = entityManager.createNativeQuery(countQuery, Long.class);
        countTypedQuery.setParameter("idOperationParam", programmeCriteria.getOperationId());
        countTypedQuery.setParameter("nomParam", nom);
        long totalCount = (long) countTypedQuery.getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }


        String sqlQuery = "SELECT zp.objectid, zp.programme, zp.id_tabou " + baseQuery;

        List<ProgrammeRmEntity> results = entityManager.createNativeQuery(sqlQuery, ProgrammeRmEntity.class)
                .setParameter("idOperationParam", programmeCriteria.getOperationId())
                .setParameter("nomParam", nom)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();


        return new PageImpl<>(results, pageable, totalCount);
    }
}

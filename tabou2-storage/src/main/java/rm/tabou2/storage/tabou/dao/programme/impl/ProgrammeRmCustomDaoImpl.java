package rm.tabou2.storage.tabou.dao.programme.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeRmCustomDao;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du DAO d'accès aux Programme Rennes Metropole (oa_programme).
 */
@Repository
public class ProgrammeRmCustomDaoImpl implements ProgrammeRmCustomDao {

    @Qualifier("sigEntityManager")
    @Autowired
    private EntityManager entityManager;


    /**
     * Recherche les programmes d'une opération (de type secteur) par intersection spatiale.
     *
     * @param programmeCriteria paramètres de recherche.
     * @param pageable          paramètres de pagination
     * @return Programmes correspondants à la recherche
     */
    public Page<ProgrammeRmEntity> searchProgrammesWithinOperation(ProgrammeCriteria programmeCriteria, Pageable pageable) {

        //Appel à la procédure stockée programmes_of_operation : retourne les programmesRm contenu dans l'opération

        String nom = programmeCriteria.getNom();
        if (!nom.isEmpty()) {
            nom = nom.replace('*', '%');
        }

        //Requête pour compter le nombre de résultats
        final String countQuery = "select count(*) from urba_foncier.programmes_of_operation(:idOperationParam,:nomParam)";
        BigInteger totalCount = (BigInteger) entityManager.createNativeQuery(countQuery)
                .setParameter("idOperationParam", programmeCriteria.getOperationId())
                .setParameter("nomParam", nom)
                .getSingleResult();

        //Si aucun résultat
        if (totalCount.intValue() == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        String sort = pageable.getSort().toString().replace(":", "");

        final String query = "select * from urba_foncier.programmes_of_operation(:idOperationParam,:nomParam) ORDER BY " + sort;

        List<ProgrammeRmEntity> results = entityManager.createNativeQuery(query, ProgrammeRmEntity.class)
                .setParameter("idOperationParam", programmeCriteria.getOperationId())
                .setParameter("nomParam", nom)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();


        return new PageImpl<>(results, pageable, totalCount.intValue());
    }

}

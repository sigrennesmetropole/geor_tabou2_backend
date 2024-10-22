package rm.tabou2.storage.sig.dao.impl;

import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.sig.dao.SecteurCustomDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SecteurCustomDaoImpl extends AbstractCustomDaoImpl implements SecteurCustomDao {

    @PersistenceContext(unitName = "sigPU")
    private EntityManager entityManager;

    @Override
    public Long findIdParent(Long idSecteur) {
        String query = "SELECT sec.id_tabou FROM urba_foncier.oa_secteur sec " +
                "INNER JOIN (SELECT id_tabou, shape FROM urba_foncier.zac " +
                "UNION ALL SELECT id_tabou, shape FROM urba_foncier.oa_limite_intervention inter) parent " +
                "ON st_within(sec.shape, parent.shape) " +
                "WHERE sec.id_tabou = :idSec";

         List<Long> ids = entityManager.createNativeQuery(query)
                .setParameter("idSec", idSecteur)
                .getResultList();

        if(ids.size() == 1){
            return ids.get(0);
        }else {
            return null;
        }
    }
}

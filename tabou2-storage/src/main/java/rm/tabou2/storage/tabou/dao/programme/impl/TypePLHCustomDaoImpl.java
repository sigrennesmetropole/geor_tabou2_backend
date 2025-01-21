package rm.tabou2.storage.tabou.dao.programme.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.programme.TypePLHCustomDao;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID_TYPE_PLH_PARENT;

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
                builder.equal(root.join(FIELD_ID_TYPE_PLH_PARENT), filsId));
        query.select(root);

        TypedQuery<TypePLHEntity> resultQuery = entityManager.createQuery(query);

        return resultQuery.getSingleResult();
    }

}

package rm.tabou2.storage.tabou.dao.operation.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.MaitriseOuvrageCustomDao;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MaitriseOuvrageCustomDaoImpl extends AbstractCustomDaoImpl implements MaitriseOuvrageCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<MaitriseOuvrageEntity> searchMaitriseOuvrage(Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<MaitriseOuvrageEntity> countRoot = countQuery.from(MaitriseOuvrageEntity.class);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<MaitriseOuvrageEntity> searchQuery = builder.createQuery(MaitriseOuvrageEntity.class);
        Root<MaitriseOuvrageEntity> searchRoot = searchQuery.from(MaitriseOuvrageEntity.class);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<MaitriseOuvrageEntity> typedQuery = entityManager.createQuery(searchQuery);

        List<MaitriseOuvrageEntity> entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(entities, pageable, totalCount.intValue());

    }
    

}

package rm.tabou2.storage.tabou.dao.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.dao.OperationCustomDao;
import rm.tabou2.storage.tabou.entity.*;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class OperationCustomDaoImpl implements OperationCustomDao {

    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<OperationEntity> searchOperations(OperationsCriteria operationsCriteria,  Pageable pageable) {

        List<OperationEntity> result;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<OperationEntity> searchQuery = builder.createQuery(OperationEntity.class);
        Root<OperationEntity> searchRoot = searchQuery.from(OperationEntity.class);

        buildQuery(operationsCriteria, builder, searchQuery, searchRoot);

        TypedQuery<OperationEntity> typedQuery = entityManager.createQuery(searchQuery);

        int totalRows = typedQuery.getResultList().size();

        result = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        Page<OperationEntity> resultPage = new PageImpl<>(result, pageable, totalRows);

        return resultPage;

    }


    private void buildQuery(OperationsCriteria operationsCriteria, CriteriaBuilder builder,
                            CriteriaQuery<OperationEntity> criteriaQuery, Root<OperationEntity> root
    ) {

        if (operationsCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            //nom
            predicateStringCriteria(operationsCriteria.getNom(), "nom", predicates , builder, root);


            //nature
            Join<OperationEntity, NatureEntity> natureJoin= root.join("nature");
            predicateStringCriteriaForJoin(operationsCriteria.getNature(), "libelle", predicates , builder, natureJoin);


            //etape
            Join<OperationEntity, EtapeOperationEntity> etapeJoin= root.join("etapeOperation");
            predicateStringCriteriaForJoin(operationsCriteria.getEtape(), "libelle", predicates , builder, etapeJoin);


            //code
            predicateStringCriteria(operationsCriteria.getCode(), "code", predicates , builder, root);


            //numAds
            predicateStringCriteria(operationsCriteria.getNumAds(), "numAds", predicates , builder, root);


            //diffusionRestreinte
            if (operationsCriteria.getDiffusionRestreinte() != null) {

                predicateBooleanCriteria(operationsCriteria.getDiffusionRestreinte(),"diffusionRetreinte", predicates, builder, root);

            }else{
                predicates.add(builder.isFalse(root.get("diffusionRetreinte")));
            }

            //EstSecteur
            if (operationsCriteria.getEstSecteur() != null) {

                predicateBooleanCriteria(operationsCriteria.getEstSecteur(),"secteur", predicates, builder, root);

            }

            //autorisationDate
            predicateDateCriteria(operationsCriteria.getAutorisationDateDebut(), operationsCriteria.getAutorisationDateFin(), "autorisationDate", predicates, builder, root);

            //operationnelDate
            predicateDateCriteria(operationsCriteria.getOperationnelDateDebut(), operationsCriteria.getOperationnelDateFin(), "operationnelDate", predicates, builder, root);


            //clotureDate
            predicateDateCriteria(operationsCriteria.getClotureDateDebut(), operationsCriteria.getClotureDateFin(), "clotureDate", predicates, builder, root);


            if(operationsCriteria.getTiers() != null){

                Join<OperationEntity, OperationTiersEntity> operationTiersJoin= root.join("operation");
                Join<OperationTiersEntity,TypeTiersEntity>  typeTiersyJoin= operationTiersJoin.join("typeTiers");
                Join<OperationTiersEntity, TiersEntity> tiersJoin= operationTiersJoin.join("tiers");

                predicateStringCriteriaForJoin(operationsCriteria.getTiers(), "libelle", predicates , builder, tiersJoin);

                predicates.add(builder.equal(typeTiersyJoin.get("libelle"), operationsCriteria.getTiers()));

            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }
    }


    public void predicateStringCriteria(String criteria, String type, List<Predicate> predicates, CriteriaBuilder builder ,Root<OperationEntity> root){
        if (criteria != null) {
            if(criteria.indexOf("*") == -1){
                predicates.add(builder.equal(root.get(type), criteria));
            }else{
                predicates.add(builder.like(root.get(type), criteria.replace("*", "%")));
            }
        }
    }

    public void predicateStringCriteriaForJoin(String criteria, String type, List<Predicate> predicates, CriteriaBuilder builder ,Join<?,?> join){
        if (criteria != null) {
            if(criteria.indexOf("*") == -1){
                predicates.add(builder.equal(join.get(type), criteria));
            }else{
                predicates.add(builder.like(join.get(type), criteria.replace("*", "%")));
            }
        }
    }

    public void predicateDateCriteria(Date dateDebut , Date dateFin, String type, List<Predicate> predicates, CriteriaBuilder builder , Root<OperationEntity> root){
        if (dateDebut != null && dateFin != null) {

            predicates.add(builder.between(root.get(type), dateDebut, dateFin));

        }else if(dateDebut != null && dateFin == null){

            predicates.add(builder.greaterThanOrEqualTo(root.get(type), dateDebut));

        }else if(dateDebut == null && dateFin != null){

            predicates.add(builder.lessThanOrEqualTo(root.get(type), dateFin));

        }
    }

    public void predicateBooleanCriteria(Boolean criteria, String type, List<Predicate> predicates, CriteriaBuilder builder , Root<OperationEntity> root) {

            if (criteria) {
                predicates.add(builder.isTrue(root.get(type)));
            } else {
                predicates.add(builder.isFalse(root.get(type)));
            }

    }

}

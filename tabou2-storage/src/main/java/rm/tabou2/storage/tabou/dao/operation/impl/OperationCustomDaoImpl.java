package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.OperationCustomDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class OperationCustomDaoImpl extends AbstractCustomDaoImpl implements OperationCustomDao {

    public static final String FIELD_NOM = "nom";
    public static final String FIELD_NATURE = "nature";
    public static final String FIELD_LIBELLE = "libelle";
    public static final String FIELD_ETAPE_OPERATION = "etapeOperation";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_NUM_ADS = "numAds";
    public static final String FIELD_DIFFUSION_RETREINTE = "diffusionRetreinte";
    public static final String FIELD_SECTEUR = "secteur";
    public static final String FIELD_AUTORISATION_DATE = "autorisationDate";
    public static final String FIELD_OPERATIONNEL_DATE = "operationnelDate";
    public static final String FIELD_CLOTURE_DATE = "clotureDate";
    public static final String FIELD_OPERATION_TIERS = "operationsTiers";
    public static final String FIELD_TYPE_TIERS = "typeTiers";
    public static final String FIELD_TIERS = "tiers";

    
    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<OperationEntity> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable) {


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<OperationEntity> countRoot = countQuery.from(OperationEntity.class);
        buildQuery(operationsCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<OperationEntity> searchQuery = builder.createQuery(OperationEntity.class);
        Root<OperationEntity> searchRoot = searchQuery.from(OperationEntity.class);
        buildQuery(operationsCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<OperationEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<OperationEntity> operationEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(operationEntities, pageable, totalCount.intValue());


    }


    private void buildQuery(OperationsCriteria operationsCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<OperationEntity> root
    ) {

        if (operationsCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            //nom
            predicateStringCriteria(operationsCriteria.getNom(), FIELD_NOM, predicates, builder, root);


            //nature
            if(operationsCriteria.getNature() != null){
                Join<OperationEntity, NatureEntity> natureJoin = root.join(FIELD_NATURE);
                predicateStringCriteriaForJoin(operationsCriteria.getNature(), FIELD_LIBELLE, predicates, builder, natureJoin);
            }



            //etape
            if(operationsCriteria.getEtape() != null) {
                Join<OperationEntity, EtapeOperationEntity> etapeJoin = root.join(FIELD_ETAPE_OPERATION);
                predicateStringCriteriaForJoin(operationsCriteria.getEtape(), FIELD_LIBELLE, predicates, builder, etapeJoin);
            }


            //code
            predicateStringCriteria(operationsCriteria.getCode(), FIELD_CODE, predicates, builder, root);


            //numAds
            predicateStringCriteria(operationsCriteria.getNumAds(), FIELD_NUM_ADS, predicates, builder, root);


            //diffusionRestreinte
            if (operationsCriteria.getDiffusionRestreinte() != null) {

                predicateBooleanCriteria(operationsCriteria.getDiffusionRestreinte(), FIELD_DIFFUSION_RETREINTE, predicates, builder, root);

            } else {
                predicates.add(builder.isFalse(root.get(FIELD_DIFFUSION_RETREINTE)));
            }

            //EstSecteur
            if (operationsCriteria.getEstSecteur() != null) {

                predicateBooleanCriteria(operationsCriteria.getEstSecteur(), FIELD_SECTEUR, predicates, builder, root);

            }

            //autorisationDate
            predicateDateCriteria(operationsCriteria.getAutorisationDateDebut(), operationsCriteria.getAutorisationDateFin(), FIELD_AUTORISATION_DATE, predicates, builder, root);

            //operationnelDate
            predicateDateCriteria(operationsCriteria.getOperationnelDateDebut(), operationsCriteria.getOperationnelDateFin(), FIELD_OPERATIONNEL_DATE, predicates, builder, root);


            //clotureDate
            predicateDateCriteria(operationsCriteria.getClotureDateDebut(), operationsCriteria.getClotureDateFin(), FIELD_CLOTURE_DATE, predicates, builder, root);


            if (operationsCriteria.getTiers() != null) {

                Join<OperationEntity, OperationTiersEntity> operationTiersJoin = root.join(FIELD_OPERATION_TIERS);
                Join<OperationTiersEntity, TypeTiersEntity> typeTiersyJoin = operationTiersJoin.join(FIELD_TYPE_TIERS);
                Join<OperationTiersEntity, TiersEntity> tiersJoin = operationTiersJoin.join(FIELD_TIERS);

                predicateStringCriteriaForJoin(operationsCriteria.getTiers(), FIELD_NOM, predicates, builder, tiersJoin);

                predicates.add(builder.equal(typeTiersyJoin.get(FIELD_LIBELLE), operationsCriteria.getTiers()));

            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }


        }
    }




}

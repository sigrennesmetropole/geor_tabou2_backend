package rm.tabou2.storage.tabou.dao.impl;

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
import rm.tabou2.storage.dao.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.OperationCustomDao;
import rm.tabou2.storage.tabou.entity.*;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_AUTORISATION_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CLOTURE_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CODE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_DIFFUSION_RETREINTE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ETAPE_OPERATION;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NATURE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NOM;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NUM_ADS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_OPERATIONNEL_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_OPERATION_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_SECTEUR;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TYPE_TIERS;


@Repository
public class OperationCustomDaoImpl extends AbstractCustomDaoImpl implements OperationCustomDao {
    
    @Qualifier("tabouEntityManager")
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<OperationEntity> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable) {

        List<OperationEntity> result;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<OperationEntity> searchQuery = builder.createQuery(OperationEntity.class);
        Root<OperationEntity> searchRoot = searchQuery.from(OperationEntity.class);

        buildQuery(operationsCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(),searchRoot,builder));

        TypedQuery<OperationEntity> typedQuery = entityManager.createQuery(searchQuery);

        int totalRows = typedQuery.getResultList().size();

        result = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        return new PageImpl<>(result, pageable, totalRows);



    }


    private void buildQuery(OperationsCriteria operationsCriteria, CriteriaBuilder builder,
                            CriteriaQuery<OperationEntity> criteriaQuery, Root<OperationEntity> root
    ) {

        if (operationsCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            //nom
            predicateStringCriteria(operationsCriteria.getNom(), FIELD_NOM, predicates, builder, root);


            //nature
            Join<OperationEntity, NatureEntity> natureJoin = root.join(FIELD_NATURE);
            predicateStringCriteriaForJoin(operationsCriteria.getNature(), FIELD_LIBELLE, predicates, builder, natureJoin);


            //etape
            Join<OperationEntity, EtapeOperationEntity> etapeJoin = root.join(FIELD_ETAPE_OPERATION);
            predicateStringCriteriaForJoin(operationsCriteria.getEtape(), FIELD_LIBELLE, predicates, builder, etapeJoin);


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
                criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
            }


        }
    }




}

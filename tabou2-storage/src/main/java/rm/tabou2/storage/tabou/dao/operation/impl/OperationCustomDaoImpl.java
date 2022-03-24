package rm.tabou2.storage.tabou.dao.operation.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.OperationCustomDao;
import rm.tabou2.storage.tabou.entity.operation.ConsommationEspaceEntity;
import rm.tabou2.storage.tabou.entity.operation.DecisionEntity;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;
import rm.tabou2.storage.tabou.entity.operation.ModeAmenagementEntity;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_AUTORISATION_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CLOTURE_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CODE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CONSOMMATION_ESPACE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_DECISION;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_DIFFUSION_RETREINTE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ETAPE_OPERATION;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_MAITRISE_OUVRAGE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_MODE_AMENAGEMENT;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NATURE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NOM;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NUM_ADS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_OPERATIONNEL_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_OPERATION_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_SECTEUR;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TYPE_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_VOCATION;


@Repository
public class OperationCustomDaoImpl extends AbstractCustomDaoImpl implements OperationCustomDao {

    @PersistenceContext(unitName = "tabouPU")
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

            // vocation
            if(operationsCriteria.getVocation() != null) {
                Join<OperationEntity, VocationEntity> vocationJoin = root.join(FIELD_VOCATION);
                predicateStringCriteriaForJoin(operationsCriteria.getVocation(), FIELD_LIBELLE, predicates, builder, vocationJoin);
            }

            // decision
            if(operationsCriteria.getDecision() != null) {
                Join<OperationEntity, DecisionEntity> decisionJoin = root.join(FIELD_DECISION);
                predicateStringCriteriaForJoin(operationsCriteria.getDecision(), FIELD_LIBELLE, predicates, builder, decisionJoin);
            }

            // maitrise d'ouvrage
            if(operationsCriteria.getMaitriseOuvrage() != null) {
                Join<OperationEntity, MaitriseOuvrageEntity> maitriseOuvrageJoin = root.join(FIELD_MAITRISE_OUVRAGE);
                predicateStringCriteriaForJoin(operationsCriteria.getMaitriseOuvrage(), FIELD_LIBELLE, predicates, builder, maitriseOuvrageJoin);
            }

            // consommation d'espace
            if(operationsCriteria.getConsommationEspace() != null) {
                Join<OperationEntity, ConsommationEspaceEntity> consommationEspaceJoin = root.join(FIELD_CONSOMMATION_ESPACE);
                predicateStringCriteriaForJoin(operationsCriteria.getConsommationEspace(), FIELD_LIBELLE, predicates, builder, consommationEspaceJoin);
            }

            // mode d'aménagement
            if(operationsCriteria.getModeAmenagement() != null) {
                Join<OperationEntity, ModeAmenagementEntity> modeAmenagementJoin = root.join(FIELD_MODE_AMENAGEMENT);
                predicateStringCriteriaForJoin(operationsCriteria.getModeAmenagement(), FIELD_LIBELLE, predicates, builder, modeAmenagementJoin);
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
                criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
            }


        }
    }




}

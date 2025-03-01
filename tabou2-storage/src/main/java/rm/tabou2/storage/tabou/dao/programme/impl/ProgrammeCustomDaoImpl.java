package rm.tabou2.storage.tabou.dao.programme.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeCustomDao;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ADS_DATE_PREVU;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ATTRIBUTION_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ATTRIBUTION_FONCIERE_ANNEE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CLOTURE_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_CODE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_COMMERCIALISATION_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_DAT_DATE_PREVU;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_DIFFUSION_RETREINTE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_DOC_DATE_PREVU;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ETAPE_PROGRAMME;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ID;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIBELLE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LIVRAISON_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_ANNULATION_DATE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LOGEMENTS_ACCESS_AIDE_PREVU;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_LOGEMENTS_LOCAT_AIDE_PREVU;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NATURE;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NOM;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_NUM_ADS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_OPERATION;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_PROGRAMME_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TIERS;
import static rm.tabou2.storage.tabou.dao.constants.FieldsConstants.FIELD_TYPE_TIERS;

@Repository
public class ProgrammeCustomDaoImpl extends AbstractCustomDaoImpl implements ProgrammeCustomDao {

    @PersistenceContext(unitName = "tabouPU")
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<ProgrammeEntity> searchProgrammes(ProgrammeCriteria programmeCriteria, Pageable pageable) {


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        //Requête pour compter le nombre de résultats total
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<ProgrammeEntity> countRoot = countQuery.from(ProgrammeEntity.class);
        buildQuery(programmeCriteria, builder, countQuery, countRoot);
        countQuery.select(builder.countDistinct(countRoot));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        //Si aucun résultat
        if (totalCount == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        //Requête de recherche
        CriteriaQuery<ProgrammeEntity> searchQuery = builder.createQuery(ProgrammeEntity.class);
        Root<ProgrammeEntity> searchRoot = searchQuery.from(ProgrammeEntity.class);
        buildQuery(programmeCriteria, builder, searchQuery, searchRoot);

        searchQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

        TypedQuery<ProgrammeEntity> typedQuery = entityManager.createQuery(searchQuery);
        List<ProgrammeEntity> programmeEntities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(programmeEntities, pageable, totalCount.intValue());
    }

    private void buildQuery(ProgrammeCriteria programmeCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<ProgrammeEntity> root) {

        if (programmeCriteria == null) {
            return;
        }

        List<Predicate> predicates = new ArrayList<>();

        //nom
        predicateStringCriteria(programmeCriteria.getNom(), FIELD_NOM, predicates, builder, root);

        //etape
        Join<ProgrammeEntity, EtapeProgrammeEntity> etapeJoin = root.join(FIELD_ETAPE_PROGRAMME);
        predicateStringCriteriaForJoin(programmeCriteria.getEtape(), FIELD_LIBELLE, predicates, builder, etapeJoin);

        //diffusionRestreinte
        if (programmeCriteria.getDiffusionRestreinte() != null) {
            predicateBooleanCriteria(programmeCriteria.getDiffusionRestreinte(), FIELD_DIFFUSION_RETREINTE, predicates, builder, root);
        } else {
            predicates.add(builder.isFalse(root.get(FIELD_DIFFUSION_RETREINTE)));
        }

        //code
        predicateStringCriteria(programmeCriteria.getCode(), FIELD_CODE, predicates, builder, root);

        //clotureDate
        predicateDateCriteria(programmeCriteria.getClotureDateDebut(), programmeCriteria.getClotureDateFin(), FIELD_CLOTURE_DATE, predicates, builder, root);

        //numAds
        predicateStringCriteria(programmeCriteria.getNumAds(), FIELD_NUM_ADS, predicates, builder, root);

        //tiers
        if (programmeCriteria.getTiers() != null) {

            Join<ProgrammeEntity, ProgrammeTiersEntity> programmeTiersJoin = root.join(FIELD_PROGRAMME_TIERS);
            Join<ProgrammeTiersEntity, TypeTiersEntity> typeTiersyJoin = programmeTiersJoin.join(FIELD_TYPE_TIERS);
            Join<ProgrammeTiersEntity, TiersEntity> tiersJoin = programmeTiersJoin.join(FIELD_TIERS);

            predicateStringCriteriaForJoin(programmeCriteria.getTiers(), FIELD_NOM, predicates, builder, tiersJoin);

            predicates.add(builder.equal(typeTiersyJoin.get(FIELD_LIBELLE), programmeCriteria.getTiers()));

        }

        // nom et nature de l'opération
        Join<ProgrammeEntity, OperationEntity> operationJoin = root.join(FIELD_OPERATION);
        if (programmeCriteria.getNomOperation() != null) {
            predicateStringCriteriaForJoin(programmeCriteria.getNomOperation(), FIELD_NOM, predicates, builder, operationJoin);
        }
        if (programmeCriteria.getNatureOperation() != null) {
            Join<OperationEntity, NatureEntity> natureJoin = operationJoin.join(FIELD_NATURE);
            predicateStringCriteriaForJoin(programmeCriteria.getNatureOperation(), FIELD_LIBELLE, predicates, builder, natureJoin);
        }


        //identifiant de l'opération
        if (programmeCriteria.getOperationId() > 0) {
            predicateLongCriteriaForJoin(programmeCriteria.getOperationId(), FIELD_ID, predicates, builder, operationJoin);
        }

        //attributionFonciereAnnee
        predicateYearCriteria(programmeCriteria.getAttributionFonciereAnneeDebut(), programmeCriteria.getAttributionFonciereAnneeFin(), FIELD_ATTRIBUTION_FONCIERE_ANNEE, predicates, builder, root);

        //attributionDate
        predicateDateCriteria(programmeCriteria.getAttributionDateDebut(), programmeCriteria.getAttributionDateFin(), FIELD_ATTRIBUTION_DATE, predicates, builder, root);

        //commercialisationDate
        predicateDateCriteria(programmeCriteria.getCommercialisationDateDebut(), programmeCriteria.getCommercialisationDateFin(), FIELD_COMMERCIALISATION_DATE, predicates, builder, root);

        //ADSDate
        predicateDateCriteria(programmeCriteria.getAdsDateDebut(), programmeCriteria.getAdsDateFin(), FIELD_ADS_DATE_PREVU, predicates, builder, root);

        //Date de livraison
        predicateDateCriteria(programmeCriteria.getLivraisonDateDebut(), programmeCriteria.getLivraisonDateFin(), FIELD_LIVRAISON_DATE, predicates, builder, root);

        //Date d'annulation
        predicateDateCriteria(programmeCriteria.getAnnulationDate(), null, FIELD_ANNULATION_DATE, predicates, builder, root);

        //DOCDate
        predicateDateCriteria(programmeCriteria.getDocDateDebut(), programmeCriteria.getDocDateFin(), FIELD_DOC_DATE_PREVU, predicates, builder, root);

        //DATDate
        predicateDateCriteria(programmeCriteria.getDatDateDebut(), programmeCriteria.getDatDateFin(), FIELD_DAT_DATE_PREVU, predicates, builder, root);

        //logementsAidés
        if (programmeCriteria.getLogementsAides() != null) {
            predicateBooleanOrGreaterThanIntegerCriteria(programmeCriteria.getLogementsAides(), 0, List.of(FIELD_LOGEMENTS_LOCAT_AIDE_PREVU, FIELD_LOGEMENTS_ACCESS_AIDE_PREVU), predicates, builder, root);
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(Predicate[]::new)));
        }

    }
}


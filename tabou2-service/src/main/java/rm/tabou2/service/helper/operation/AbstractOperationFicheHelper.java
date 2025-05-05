package rm.tabou2.service.helper.operation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.bean.tabou.common.Commentaire;
import rm.tabou2.service.bean.tabou.operation.suivi.CommentairesOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.ContributionsOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.EcheancierOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.FonciersOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.ProgrammationsOperation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.FicheSuiviOperationDataModel;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.dao.CommuneCustomDao;
import rm.tabou2.storage.sig.dao.SecteurCustomDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;
import rm.tabou2.storage.tabou.dao.evenement.EvenementOperationCustomDao;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.dao.operation.MosCustomDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.operation.ContributionEntity;
import rm.tabou2.storage.tabou.entity.operation.DescriptionFoncierEntity;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.InformationProgrammationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public abstract class AbstractOperationFicheHelper {

    protected static final String ACTIVITE = "ACTIVITE";
    protected static final String HABITAT = "HABITAT";
    protected static final String MIXTE = "MIXTE";
    protected static final String MOBILITE = "MOBILITE";

    @Autowired
    private DocumentGenerator documentGenerator;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private SecteurCustomDao secteurCustomDao;

    @Autowired
    private CommuneCustomDao communeCustomDao;

    @Autowired
    private MosCustomDao mosCustomDao;

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private EvenementOperationCustomDao evenementOperationCustomDao;

    @Value("${typeevenement.commentaire.montage}")
    private String typeCommentaireMontage;

    @Value("${typeevenement.commentaire.plui}")
    private String typeCommentairePlui;

    @Value("${typeevenement.commentaire.operationnel}")
    private String typeCommentaireOperationnel;

    @Value("${typeevenement.commentaire.moa}")
    private String typeCommentaireMoa;

    @Value("${typeevenement.commentaire.mobilite.dauh}")
    private String typeCommentaireDauh;

    @Value("${typeevenement.commentaire.mobilite.pisu}")
    private String typeCommentairePisu;

    @Value("${typeevenement.commentaire.mobilite.commune}")
    private String typeCommentaireCommune;

    @Value("${typeevenement.commentaire.mobilite.mandataire}")
    private String typeCommentaireMandataire;

    @Value("${typeevenement.commentaire.mobilite.autre}")
    private String typeCommentaireMobiliteAutre;

    @Value("#{'${typeevenement.commentaire.autres}'.split(';')}")
    private List<String> typesAutresCommentaires;

    @Value("${typeevenement.commentaire.regex-echeancier}")
    private String regexTypeCommentaireEcheancier;

    @Value("${acteur.code.interne}")
    private String codeActeurInterne;

    @Value("${acteur.code.externe}")
    private String codeActeurExterne;

    @Value("${contribution.code.enjeux}")
    private String codeContributionEnjeux;

    @Value("${contribution.code.traitee}")
    private String codeContributionTraitee;

    @Value("${contribution.code.avenir}")
    private String codeContributionAvenir;

    @Value("${programmation.code.activite}")
    private String codeProgrammationActivite;

    @Value("${programmation.code.autre}")
    private String codeProgrammationAutre;

    @Value("${programmation.code.equipement}")
    private String codeProgrammationEquipement;

    @Value("${programmation.code.habitat}")
    private String codeProgrammationHabitat;

    @Value("${foncier.code.public}")
    private String codeFoncierPublic;

    @Value("${foncier.code.prive}")
    private String codeFoncierPrive;

    public GenerationModel buildGenerationModel(OperationEntity operationEntity, VocationEntity vocation, String path) throws AppServiceException {

        FicheSuiviOperationDataModel ficheSuiviOperationDataModel = new FicheSuiviOperationDataModel();
        ficheSuiviOperationDataModel.setOperation(operationEntity);
        ficheSuiviOperationDataModel.setEntiteReferente(operationEntity.getEntiteReferente());
        ficheSuiviOperationDataModel.setParent(getParent(operationEntity));
        ficheSuiviOperationDataModel.setCommunes(getCommunes(operationEntity));
        ficheSuiviOperationDataModel.setConsommationEspace(operationEntity.getConsommationEspace());
        ficheSuiviOperationDataModel.setEtape(operationEntity.getEtapeOperation());
        ficheSuiviOperationDataModel.setVocation(vocation);
        ficheSuiviOperationDataModel.setTypeOccupation(operationEntity.getTypeOccupation());
        ficheSuiviOperationDataModel.setOutilFoncier(operationEntity.getOutilFoncier());
        ficheSuiviOperationDataModel.setModeAmenagement(operationEntity.getModeAmenagement());
        ficheSuiviOperationDataModel.setMaitriseOuvrage(operationEntity.getMaitriseOuvrage());
        ficheSuiviOperationDataModel.setMOS(mosCustomDao.computeOperationMos(operationEntity.getId(), Boolean.TRUE.equals(operationEntity.getSecteur())));

        if (operationEntity.getFinancementPPI() != null) {
            ficheSuiviOperationDataModel.setFinancementPPI(Boolean.TRUE.equals(operationEntity.getFinancementPPI()) ? "Oui" : "Non");
        }

        // Insertion des fonciers
        ficheSuiviOperationDataModel.setFonciers(getFonciers(operationEntity));

        // Insertion des programmations
        ficheSuiviOperationDataModel.setProgrammations(getProgrammationsFiche(operationEntity));

        // Insertion des contributions
        ficheSuiviOperationDataModel.setContributions(buildContributionsOperation(operationEntity));

        // Insertion des types financements réduits à un string
        ficheSuiviOperationDataModel.setTypesFinancements(operationEntity.getFinancements().stream()
                .distinct()
                .map(x -> x.getTypeFinancement().getLibelle())
                .filter(Objects::nonNull)
                .reduce("", (partialString, element) -> partialString + ", " + element));


        // Insertion des acteurs internes et externes
        ficheSuiviOperationDataModel.setActeursInternes(operationEntity.getActeurs().stream()
                .filter(x-> codeActeurInterne.equals(x.getTypeActeur().getCode())).toList());
        ficheSuiviOperationDataModel.setActeursExternes(operationEntity.getActeurs().stream()
                .filter(x-> codeActeurExterne.equals(x.getTypeActeur().getCode())).toList());

        // Insertion des commentaires
        ficheSuiviOperationDataModel.setCommentaires(buildCommentaires(operationEntity));
        ficheSuiviOperationDataModel.setEcheanciers(buildEcheanciers(operationEntity));

        //Insertion illustration
        ficheSuiviOperationDataModel.setIllustration(documentGenerator.generatedImgForTemplate(AlfrescoTabouType.OPERATION, operationEntity.getId()));

        return new GenerationModel(ficheSuiviOperationDataModel, path, MediaType.APPLICATION_PDF.getSubtype());
    }

    private FonciersOperation getFonciers(OperationEntity operationEntity) {
        FonciersOperation fonciers = new FonciersOperation();
        Set<DescriptionFoncierEntity> foncierEntities = operationEntity.getDescriptionsFoncier();

        fonciers.setFoncierPrive(foncierEntities.stream()
                .filter(x -> codeFoncierPrive.equals(x.getTypeFoncier().getCode()))
                .map(DescriptionFoncierEntity::getDescription)
                .filter(Objects::nonNull)
                .reduce("", (partialString, element)-> partialString + "\n" + element));

        fonciers.setFoncierPublic(foncierEntities.stream()
                .filter(x -> codeFoncierPublic.equals(x.getTypeFoncier().getCode()))
                .map(DescriptionFoncierEntity::getDescription)
                .filter(Objects::nonNull)
                .reduce("", (partialString, element)-> partialString + "\n" + element));

        fonciers.setTauxFoncierPublic(foncierEntities.stream()
                .filter(x -> codeFoncierPublic.equals(x.getTypeFoncier().getCode()))
                .map(DescriptionFoncierEntity::getTaux)
                .filter(Objects::nonNull)
                .reduce(0.0, Double::sum));

        return fonciers;
    }

    private ProgrammationsOperation getProgrammationsFiche(OperationEntity operationEntity) {

        ProgrammationsOperation programmationsOperation = new ProgrammationsOperation();
        Set<InformationProgrammationEntity> informations = operationEntity.getInformationsProgrammation();

        programmationsOperation.setActivite(informations.stream()
                .filter(x-> codeProgrammationActivite.equals(x.getTypeProgrammation().getCode()))
                .map(InformationProgrammationEntity::getDescription)
                .findFirst().orElse(""));

        programmationsOperation.setAutre(informations.stream()
                .filter(x->codeProgrammationAutre.equals(x.getTypeProgrammation().getCode()))
                .map(InformationProgrammationEntity::getDescription)
                .findFirst().orElse(""));

        programmationsOperation.setEquipement(informations.stream()
                .filter(x -> codeProgrammationEquipement.equals(x.getTypeProgrammation().getCode()))
                .map(InformationProgrammationEntity::getDescription)
                .findFirst().orElse(""));

        programmationsOperation.setHabitat(informations.stream()
                .filter(x-> codeProgrammationHabitat.equals(x.getTypeProgrammation().getCode()))
                .map(InformationProgrammationEntity::getDescription)
                .findFirst().orElse(""));
        return programmationsOperation;
    }

    private ContributionsOperation buildContributionsOperation(OperationEntity operationEntity) {
        ContributionsOperation contributionsOperation = new ContributionsOperation();
        Set<ContributionEntity> contributions = operationEntity.getContributions();

        contributionsOperation.setContributionEnjeux(contributions.stream()
                .filter(x-> codeContributionEnjeux.equals(x.getTypeContribution().getCode()))
                .map(ContributionEntity::getDescription)
                .filter(Objects::nonNull)
                .reduce("", (partialString, element) -> partialString + "\n" + element));

        contributionsOperation.setContributionTraitee(contributions.stream()
                .filter(x-> codeContributionTraitee.equals(x.getTypeContribution().getCode()))
                .map(ContributionEntity::getDescription)
                .filter(Objects::nonNull)
                .reduce("", (partialString, element) -> partialString + "\n" + element));

        contributionsOperation.setContributionAvenir(contributions.stream()
                .filter(x-> codeContributionAvenir.equals(x.getTypeContribution().getCode()))
                .map(ContributionEntity::getDescription)
                .filter(Objects::nonNull)
                .reduce("", (partialString, element) -> partialString + "\n" + element));

        return contributionsOperation;
    }

    private OperationEntity getParent(OperationEntity operationEntity){

        if(Boolean.TRUE.equals(operationEntity.getSecteur())){
            Long id = secteurCustomDao.findIdParent(operationEntity.getId());
            if(id != null) {
                operationDao.findOneById(id);
            }
        }

        return null;
    }

    private String getCommunes(OperationEntity operationEntity){
        List<String> communes = communeCustomDao.searchCommunesByOperationId(operationEntity.getId(),
                        operationEntity.getSecteur(), operationEntity.getNature().getId() == 1).stream()
                .map(CommuneEntity::getNom).toList();
        return String.join(", ", communes);
    }

    private CommentairesOperation buildCommentaires(OperationEntity operationEntity){
        //Récupération des évènements de l'opération
        Pageable pageable = PaginationUtils.buildPageable(0,Integer.MAX_VALUE, "id", false, EvenementOperationEntity.class);
        Page<EvenementOperationEntity> results = evenementOperationCustomDao.searchEvenementsOperation(operationEntity.getId(), null, pageable);

        CommentairesOperation commentaires = new CommentairesOperation();

        commentaires.setCommentaireMontage(findLastCommentaire(results, typeCommentaireMontage));
        commentaires.setCommentairePlui(findLastCommentaire(results, typeCommentairePlui));
        commentaires.setCommentaireOperationnel(findLastCommentaire(results, typeCommentaireOperationnel));
        commentaires.setCommentaireMoa(findLastCommentaire(results, typeCommentaireMoa));
        commentaires.setCommentaireDauh(findLastCommentaire(results, typeCommentaireDauh));
        commentaires.setCommentairePisu(findLastCommentaire(results, typeCommentairePisu));
        commentaires.setCommentaireCommune(findLastCommentaire(results, typeCommentaireCommune));
        commentaires.setCommentaireMandataire(findLastCommentaire(results, typeCommentaireMandataire));
        commentaires.setCommentaireMobiliteAutre(findLastCommentaire(results, typeCommentaireMobiliteAutre));

        List<Commentaire> autresCommentaires = new ArrayList<>();
        for(String code : typesAutresCommentaires){
            Commentaire commentaire = findLastCommentaire(results, code);
            if(StringUtils.isNotEmpty(commentaire.getMessage())) {
                autresCommentaires.add(commentaire);
            }
        }
        commentaires.setAutresCommentaires(autresCommentaires);

        return commentaires;
    }

    private Commentaire findLastCommentaire(Page<EvenementOperationEntity> evenements, String typeCommentaire){
        Commentaire result = new Commentaire();
        TypeEvenementEntity typeEvenement = typeEvenementDao.findByCode(typeCommentaire);
        if(typeEvenement != null && StringUtils.isNotEmpty(typeEvenement.getLibelle())){
            result.setLibelle(typeEvenement.getLibelle());
        }else{
            result.setLibelle(typeCommentaire);
        }
        result.setMessage(evenements.stream()
                .filter(evt->evt.getTypeEvenement().getCode().equals(typeCommentaire))
                .sorted(Comparator.comparing(EvenementOperationEntity::getEventDate).reversed())
                .map(EvenementOperationEntity::getDescription)
                .findFirst().orElse(""));
        return result;

    }

    private List<EcheancierOperation> buildEcheanciers(OperationEntity operationEntity) {
        TypeEvenementCriteria typeEvenementCriteria = new TypeEvenementCriteria();
        typeEvenementCriteria.setCode(regexTypeCommentaireEcheancier);

        //Récupération des évènements de l'opération
        Pageable pageable = PaginationUtils.buildPageable(0,Integer.MAX_VALUE, "id", false, EvenementOperationEntity.class);
        Page<EvenementOperationEntity> results = evenementOperationCustomDao.searchEvenementsOperation(operationEntity.getId(),
                typeEvenementCriteria, pageable);

        List<EcheancierOperation> echeancierOperations = new ArrayList<>();
        for(EvenementOperationEntity echeancierEntity : results){

            EcheancierOperation echeancierOperation = new EcheancierOperation();
            echeancierOperation.setTypeEvenement(echeancierEntity.getTypeEvenement().getLibelle());
            echeancierOperation.setDescription(echeancierEntity.getDescription());
            echeancierOperation.setEventDate(echeancierEntity.getEventDate());
            echeancierOperation.setModifUser(echeancierEntity.getModifUser());

            echeancierOperations.add(echeancierOperation);
        }
        return echeancierOperations;
    }
}

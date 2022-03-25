package rm.tabou2.service.helper.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.dto.DescriptionFinancementOperation;
import rm.tabou2.service.dto.Contribution;
import rm.tabou2.service.dto.Acteur;
import rm.tabou2.service.dto.ActionOperation;
import rm.tabou2.service.dto.Amenageur;
import rm.tabou2.service.dto.DescriptionFoncier;
import rm.tabou2.service.dto.InformationProgrammation;
import rm.tabou2.storage.tabou.dao.operation.*;
import rm.tabou2.storage.tabou.entity.operation.*;

import java.util.Optional;
import java.util.Set;

@Component
public class OperationUpdateHelper {

    @Autowired
    private TypeActeurDao typeActeurDao;

    @Autowired
    private ActeurDao acteurDao;

    @Autowired
    private ActionOperationDao actionOperationDao;

    @Autowired
    private TypeActionOperationDao typeActionDao;

    @Autowired
    private AmenageurDao amenageurDao;

    @Autowired
    private TypeAmenageurDao typeAmenageurDao;

    @Autowired
    private ContributionDao contributionDao;

    @Autowired
    private TypeContributionDao typeContributionDao;

    @Autowired
    private DescriptionFoncierDao descriptionsFoncierDao;

    @Autowired
    private TypeFoncierDao typeFoncierDao;

    @Autowired
    private DescriptionFinancementOperationDao financementDao;

    @Autowired
    private TypeFinancementOperationDao typeFinancementDao;

    @Autowired
    private InformationProgrammationDao programmationDao;

    @Autowired
    private TypeProgrammationDao typeProgrammation;

    public void updateActeurs(OperationIntermediaire operation, OperationEntity actualOperation){
        Set<Acteur> acteurs = operation.getActeurs();
        Set<ActeurEntity> actualActeurs = actualOperation.getActeurs();

        for(ActeurEntity acteurEntity : actualActeurs){
            Optional<Acteur> first = acteurs.stream().filter(acteur ->{
                if(acteur.getId() == null) return false;
                else return acteurEntity.getId() == acteur.getId();
            }).findFirst();
            if(first.isEmpty()){
                acteurDao.delete(acteurEntity);
                actualOperation.getActeurs().remove(acteurEntity);
            }
        }

        for(Acteur acteur : acteurs){
            Optional<ActeurEntity> first = actualActeurs.stream().filter(entity -> Long.valueOf(entity.getId()).equals(acteur.getId())).findFirst();
            if(first.isPresent()){
                ActeurEntity toUpdate = first.get();
                toUpdate.setDescription(acteur.getDescription());
                toUpdate.setTypeActeur(typeActeurDao.getOne(acteur.getTypeActeur().getId()));
                acteurDao.save(toUpdate);
            }else{
                ActeurEntity toAdd = new ActeurEntity();
                toAdd.setDescription(acteur.getDescription());
                toAdd.setTypeActeur(typeActeurDao.getOne(acteur.getTypeActeur().getId()));
                toAdd = acteurDao.save(toAdd);
                actualOperation.getActeurs().add(toAdd);
            }
        }
    }

    public void updateActions(OperationIntermediaire operation, OperationEntity actualOperation){
        Set<ActionOperation> actions = operation.getActions();
        Set<ActionOperationEntity> actualActions = actualOperation.getActions();

        for(ActionOperationEntity actionEntity : actualActions){
            Optional<ActionOperation> first = actions.stream().filter(action ->{
                if(action.getId() == null) return false;
                else return actionEntity.getId() == action.getId();
            }).findFirst();
            if(first.isEmpty()){
                actionOperationDao.delete(actionEntity);
                actualOperation.getActions().remove(actionEntity);
            }
        }

        for(ActionOperation action : actions){
            Optional<ActionOperationEntity> first = actualActions.stream().filter(entity -> Long.valueOf(entity.getId()).equals(action.getId())).findFirst();
            if(first.isPresent()){
                ActionOperationEntity toUpdate = first.get();
                toUpdate.setDescription(action.getDescription());
                toUpdate.setTypeAction(typeActionDao.getOne(action.getTypeAction().getId()));
                actionOperationDao.save(toUpdate);
            }else{
                ActionOperationEntity toAdd = new ActionOperationEntity();
                toAdd.setDescription(action.getDescription());
                toAdd.setTypeAction(typeActionDao.getOne(action.getTypeAction().getId()));
                toAdd = actionOperationDao.save(toAdd);
                actualOperation.getActions().add(toAdd);
            }
        }
    }

    public void updateAmenageurs(OperationIntermediaire operation, OperationEntity actualOperation){
        Set<Amenageur> amenageurs = operation.getAmenageurs();
        Set<AmenageurEntity> actualAmenageurs = actualOperation.getAmenageurs();

        for(AmenageurEntity amenageurEntity : actualAmenageurs){
            Optional<Amenageur> first = amenageurs.stream().filter(amenageur ->{
                if(amenageur.getId() == null) return false;
                else return amenageurEntity.getId() == amenageur.getId();
            }).findFirst();
            if(first.isEmpty()){
                amenageurDao.delete(amenageurEntity);
                actualOperation.getAmenageurs().remove(amenageurEntity);
            }
        }

        for(Amenageur amenageur : amenageurs){
            Optional<AmenageurEntity> first = actualAmenageurs.stream().filter(entity -> Long.valueOf(entity.getId()).equals(amenageur.getId())).findFirst();
            if(first.isPresent()){
                AmenageurEntity toUpdate = first.get();
                toUpdate.setNom(amenageur.getNom());
                toUpdate.setTypeAmenageur(typeAmenageurDao.getOne(amenageur.getTypeAmenageur().getId()));
                amenageurDao.save(toUpdate);
            }else{
                AmenageurEntity toAdd = new AmenageurEntity();
                toAdd.setNom(amenageur.getNom());
                toAdd.setTypeAmenageur(typeAmenageurDao.getOne(amenageur.getTypeAmenageur().getId()));
                toAdd = amenageurDao.save(toAdd);
                actualOperation.getAmenageurs().add(toAdd);
            }
        }
    }

    public void updateContributions(OperationIntermediaire operation, OperationEntity actualOperation){
        Set<Contribution> contributions = operation.getContributions();
        Set<ContributionEntity> actualContributions = actualOperation.getContributions();

        for(ContributionEntity contributionEntity : actualContributions){
            Optional<Contribution> first = contributions.stream().filter(contribution ->{
                if(contribution.getId() == null) return false;
                else return contributionEntity.getId() == contribution.getId();
            }).findFirst();
            if(first.isEmpty()){
                contributionDao.delete(contributionEntity);
                actualOperation.getContributions().remove(contributionEntity);
            }
        }

        for(Contribution contribution : contributions){
            Optional<ContributionEntity> first = actualContributions.stream().filter(entity -> Long.valueOf(entity.getId()).equals(contribution.getId())).findFirst();
            if(first.isPresent()){
                ContributionEntity toUpdate = first.get();
                toUpdate.setDescription(contribution.getDescription());
                toUpdate.setTypeContribution(typeContributionDao.getOne(contribution.getTypeContribution().getId()));
                contributionDao.save(toUpdate);
            }else{
                ContributionEntity toAdd = new ContributionEntity();
                toAdd.setDescription(contribution.getDescription());
                toAdd.setTypeContribution(typeContributionDao.getOne(contribution.getTypeContribution().getId()));
                toAdd = contributionDao.save(toAdd);
                actualOperation.getContributions().add(toAdd);
            }
        }
    }

    public void updateDescriptionsFonciers(OperationIntermediaire operation, OperationEntity actualOperation){
        Set<DescriptionFoncier> descriptionsFoncier = operation.getDescriptionsFoncier();
        Set<DescriptionFoncierEntity> actualDescriptionsFoncier = actualOperation.getDescriptionsFoncier();

        for(DescriptionFoncierEntity descriptionFoncierEntity : actualDescriptionsFoncier){
            Optional<DescriptionFoncier> first = descriptionsFoncier.stream().filter(descriptionFoncier ->{
                if(descriptionFoncier.getId() == null) return false;
                else return descriptionFoncierEntity.getId() == descriptionFoncier.getId();
            }).findFirst();
            if(first.isEmpty()){
                descriptionsFoncierDao.delete(descriptionFoncierEntity);
                actualOperation.getDescriptionsFoncier().remove(descriptionFoncierEntity);
            }
        }

        for(DescriptionFoncier descriptionFoncier : descriptionsFoncier){
            Optional<DescriptionFoncierEntity> first = actualDescriptionsFoncier.stream().filter(entity -> Long.valueOf(entity.getId()).equals(descriptionFoncier.getId())).findFirst();
            if(first.isPresent()){
                DescriptionFoncierEntity toUpdate = first.get();
                toUpdate.setDescription(descriptionFoncier.getDescription());
                toUpdate.setTypeFoncier(typeFoncierDao.getOne(descriptionFoncier.getTypeFoncier().getId()));
                toUpdate.setTaux(descriptionFoncier.getTaux());
                descriptionsFoncierDao.save(toUpdate);
            }else{
                DescriptionFoncierEntity toAdd = new DescriptionFoncierEntity();
                toAdd.setDescription(descriptionFoncier.getDescription());
                toAdd.setTypeFoncier(typeFoncierDao.getOne(descriptionFoncier.getTypeFoncier().getId()));
                toAdd.setTaux(descriptionFoncier.getTaux());
                toAdd = descriptionsFoncierDao.save(toAdd);
                actualOperation.getDescriptionsFoncier().add(toAdd);
            }
        }
    }

    public void updateFinancements(OperationIntermediaire operation, OperationEntity actualOperation)
    {
        Set<DescriptionFinancementOperation> financements = operation.getFinancements();
        Set<DescriptionFinancementOperationEntity> actualFinancements = actualOperation.getFinancements();

        for(DescriptionFinancementOperationEntity financementEntity : actualFinancements){
            Optional<DescriptionFinancementOperation> first = financements.stream().filter(financement ->{
                if(financement.getId() == null) return false;
                else return financementEntity.getId() == financement.getId();
            }).findFirst();
            if(first.isEmpty()){
                financementDao.delete(financementEntity);
                actualOperation.getFinancements().remove(financementEntity);
            }
        }

        for(DescriptionFinancementOperation financement : financements){
            Optional<DescriptionFinancementOperationEntity> first = actualFinancements.stream().filter(entity -> Long.valueOf(entity.getId()).equals(financement.getId())).findFirst();
            if(first.isPresent()){
                DescriptionFinancementOperationEntity toUpdate = first.get();
                toUpdate.setDescription(financement.getDescription());
                toUpdate.setTypeFinancement(typeFinancementDao.getOne(financement.getTypeFinancement().getId()));
                financementDao.save(toUpdate);
            }else{
                DescriptionFinancementOperationEntity toAdd = new DescriptionFinancementOperationEntity();
                toAdd.setDescription(financement.getDescription());
                toAdd.setTypeFinancement(typeFinancementDao.getOne(financement.getTypeFinancement().getId()));
                toAdd = financementDao.save(toAdd);
                actualOperation.getFinancements().add(toAdd);
            }
        }
    }

    public void updateInformationsProgrammation(OperationIntermediaire operation, OperationEntity actualOperation)
    {
        Set<InformationProgrammation> programmations = operation.getInformationsProgrammation();
        Set<InformationProgrammationEntity> actualProgrammations = actualOperation.getInformationsProgrammation();

        for(InformationProgrammationEntity programmationEntity : actualProgrammations){
            Optional<InformationProgrammation> first = programmations.stream().filter(programmation ->{
                if(programmation.getId() == null) return false;
                else return programmationEntity.getId() == programmation.getId();
            }).findFirst();
            if(first.isEmpty()){
                programmationDao.delete(programmationEntity);
                actualOperation.getInformationsProgrammation().remove(programmationEntity);
            }
        }

        for(InformationProgrammation programmation : programmations){
            Optional<InformationProgrammationEntity> first = actualProgrammations.stream().filter(entity -> Long.valueOf(entity.getId()).equals(programmation.getId())).findFirst();
            if(first.isPresent()){
                InformationProgrammationEntity toUpdate = first.get();
                toUpdate.setDescription(programmation.getDescription());
                toUpdate.setTypeProgrammation(typeProgrammation.getOne(programmation.getTypeProgrammation().getId()));
                programmationDao.save(toUpdate);
            }else{
                InformationProgrammationEntity toAdd = new InformationProgrammationEntity();
                toAdd.setDescription(programmation.getDescription());
                toAdd.setTypeProgrammation(typeProgrammation.getOne(programmation.getTypeProgrammation().getId()));
                toAdd = programmationDao.save(toAdd);
                actualOperation.getInformationsProgrammation().add(toAdd);
            }
        }
    }
}

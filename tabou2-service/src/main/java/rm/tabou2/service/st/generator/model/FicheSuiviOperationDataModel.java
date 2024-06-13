package rm.tabou2.service.st.generator.model;

import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import rm.tabou2.service.bean.tabou.operation.suivi.CommentairesOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.ContributionsOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.EcheancierOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.FonciersOperation;
import rm.tabou2.service.bean.tabou.operation.suivi.ProgrammationsOperation;
import rm.tabou2.storage.tabou.entity.operation.*;

import java.io.File;
import java.util.List;

public class FicheSuiviOperationDataModel extends DataModel{

    public FicheSuiviOperationDataModel(){
        super();
    }

    public void setOperation(OperationEntity operation){addContextData("operation", operation);}

    public void setEntiteReferente(EntiteReferenteEntity entiteReferente){addContextData("entiteReferente", entiteReferente);}

    public void setParent(OperationEntity parent){addContextData("parent", parent);}

    public void setCommunes(String communes){addContextData("communes", communes);}

    public void setConsommationEspace(ConsommationEspaceEntity consommationEspace){addContextData("consommationEspace", consommationEspace);}

    public void setEtape(EtapeOperationEntity etape){addContextData("etape", etape);}

    public void setVocation(VocationEntity vocation){addContextData("vocation", vocation);}

    public void setTypeOccupation(TypeOccupationEntity typeOccupation){addContextData("typeOccupation", typeOccupation);}

    public void setOutilFoncier(OutilFoncierEntity outilFoncier){addContextData("outilFoncier", outilFoncier);}

    public void setModeAmenagement(ModeAmenagementEntity modeAmenagement){addContextData("modeAmenagement", modeAmenagement);}

    public void setMaitriseOuvrage(MaitriseOuvrageEntity maitriseOuvrage){addContextData("maitriseOuvrage", maitriseOuvrage);}

    public void setContributions(ContributionsOperation contributions){addContextData("contributions", contributions);}

    public void setTypesFinancements(String typesFinancements){addContextData("typesFinancements", typesFinancements);}

    public void setProgrammations(ProgrammationsOperation programmations){addContextData("programmations", programmations);}

    public void setFonciers(FonciersOperation fonciers){addContextData("fonciers", fonciers);}

    public void setCommentaires(CommentairesOperation commentaires){
        addContextFieldList("autresCommentaires.libelle");
        addContextFieldList("autresCommentaires.message");
        addContextData("autresCommentaires", commentaires.getAutresCommentaires());
        addContextData("commentaires", commentaires);
    }

    public void setEcheanciers(List<EcheancierOperation> echeanciers){
        addContextFieldList("echeanciers.typeEvenement");
        addContextFieldList("echeanciers.description");
        addContextFieldList("echeanciers.eventDate");
        addContextFieldList("echeanciers.modifUser");
        addContextData("echeanciers", echeanciers);
    }

    public void setActeursInternes(List<ActeurEntity> acteursInternes){addContextData("acteursInternes", acteursInternes);}

    public void setActeursExternes(List<ActeurEntity> acteursExternes){addContextData("acteursExternes", acteursExternes);}

    public void setIllustration(File imageFile) {
        IImageProvider illustration = new FileImageProvider(imageFile);
        addContextData("illustration", illustration);
        addContextFieldImage("illustration");
    }


}

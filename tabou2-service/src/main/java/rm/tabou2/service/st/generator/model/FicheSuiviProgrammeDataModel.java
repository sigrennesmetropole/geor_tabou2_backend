package rm.tabou2.service.st.generator.model;

import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import org.apache.commons.collections4.CollectionUtils;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.item.AgapeoSuiviHabitat;
import rm.tabou2.storage.tabou.item.PermisConstruireSuiviHabitat;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FicheSuiviProgrammeDataModel extends DataModel {

    public FicheSuiviProgrammeDataModel() {
        super();
        this.setAgapeos(new ArrayList<>());
        this.setPermis(new ArrayList<>());
        this.setAgapeoSuiviHabitat(new AgapeoSuiviHabitat());
        this.setPermisSuiviHabitat(new PermisConstruireSuiviHabitat());
        this.setNomFichier("");
    }

    public void setProgramme(ProgrammeEntity programme) {
        addContextData("programme", programme);
    }

    public void setOperation(OperationEntity operation) {
        addContextData("operation", operation);
    }

    public void setNature(NatureEntity nature) {
        addContextData("nature", nature);
    }

    public void setEtape(EtapeProgrammeEntity etape) {
        addContextData("etape", etape);
    }

    public void setPermisSuiviHabitat(PermisConstruireSuiviHabitat permisSuiviHabitat) {
        addContextData("permisSuiviHabitat", permisSuiviHabitat);
    }

    public void setAgapeoSuiviHabitat(AgapeoSuiviHabitat agapeoSuiviHabitat) {
        addContextData("agapeoSuiviHabitat", agapeoSuiviHabitat);
    }

    public void setAgapeos(List<AgapeoEntity> agapeos) {

        addContextData("agapeos", agapeos);
        addContextFieldList("agapeos");
    }

    public void setPermis(List<PermisConstruireEntity> permis) {

        addContextData("listPermis", permis);
        addContextFieldList("listPermis");
    }

    public void setEvenements(List<EvenementProgrammeEntity> evenements) {
        List<EvenementProgrammeEntity> sortedEvents = evenements
                .stream()
                .sorted(Comparator.comparing(EvenementProgrammeEntity::getEventDate))
                .collect(Collectors.toList());
        addContextData("evenements", sortedEvents);
        addContextFieldList("evenements");
    }

    public void setProgrammeTiers(List<ProgrammeTiersEntity> programmeTiers) {
        if (!CollectionUtils.isEmpty(programmeTiers)) {
            String nomMaitresOuvrage = programmeTiers.stream()
                    .filter(programmeTiersEntity -> programmeTiersEntity.getTypeTiers().getCode().equals("MAITRE_OUVRAGE"))
                    .map(programmeTiersEntity -> programmeTiersEntity.getTiers().getNom())
                    .collect(Collectors.joining(", "));

            String nomMaitresOeuvre = programmeTiers.stream()
                    .filter(programmeTiersEntity -> programmeTiersEntity.getTypeTiers().getCode().equals("MAITRE_OEUVRE"))
                    .map(programmeTiersEntity -> programmeTiersEntity.getTiers().getNom())
                    .collect(Collectors.joining(", "));

            addContextData("nomMaitresOuvrage", nomMaitresOuvrage);
            addContextData("nomMaitresOeuvre", nomMaitresOeuvre);
        }
    }

    public void setIllustration(File imageFile) {
        IImageProvider illustration = new FileImageProvider(imageFile);
        addContextData("illustration", illustration);
        addContextFieldImage("illustration");
    }

    public void setNomFichier(String nomFichier) {
        addContextData("nomFichier",nomFichier);
    }
}

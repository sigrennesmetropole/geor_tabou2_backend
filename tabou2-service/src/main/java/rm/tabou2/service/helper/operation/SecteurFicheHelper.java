package rm.tabou2.service.helper.operation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceNotFoundException;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.storage.tabou.dao.operation.VocationDao;
import rm.tabou2.storage.tabou.entity.operation.*;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecteurFicheHelper extends AbstractOperationFicheHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecteurFicheHelper.class);

    @Autowired
    private VocationDao vocationDao;

    @Value("${fiche.template.secteur.activite}")
    private String pathTemplateActiviteSecteur;

    @Value("${fiche.template.secteur.habitat}")
    private String pathTemplateHabitatSecteur;

    @Value("${fiche.template.secteur.mixte}")
    private String pathTemplateMixteSecteur;

    @Value("${fiche.template.secteur.mobilite}")
    private String pathTemplateMobiliteSecteur;

    private final Map<VocationEntity, String> defaultTemplatesSecteurPath = new HashMap<>();

    private final Map<VocationEntity, String> configuredTemplatesSecteurPath = new HashMap<>();


    @PostConstruct
    private void initDefaultTemplatesPathForSecteur(){
        defaultTemplatesSecteurPath.put(vocationDao.findByCode(ACTIVITE), "template/secteur/template_fiche_suivi_activite.odt");
        defaultTemplatesSecteurPath.put(vocationDao.findByCode(HABITAT), "template/secteur/template_fiche_suivi_habitat.odt");
        defaultTemplatesSecteurPath.put(vocationDao.findByCode(MIXTE), "template/secteur/template_fiche_suivi_mixte.odt");
        defaultTemplatesSecteurPath.put(vocationDao.findByCode(MOBILITE), "template/secteur/template_fiche_suivi_mobilite.odt");
    }

    @PostConstruct
    private void initConfiguredTemplatesPathForSecteur(){
        configuredTemplatesSecteurPath.put(vocationDao.findByCode(ACTIVITE), pathTemplateActiviteSecteur);
        configuredTemplatesSecteurPath.put(vocationDao.findByCode(HABITAT), pathTemplateHabitatSecteur);
        configuredTemplatesSecteurPath.put(vocationDao.findByCode(MIXTE), pathTemplateMixteSecteur);
        configuredTemplatesSecteurPath.put(vocationDao.findByCode(MOBILITE), pathTemplateMobiliteSecteur);
    }


    public GenerationModel buildGenerationModel(OperationEntity operationEntity) throws AppServiceException {

        String path;
        VocationEntity vocation = operationEntity.getVocation();
        if(vocation == null){
            throw new AppServiceNotFoundException();
        }

        path = configuredTemplatesSecteurPath.get(vocation);

        if(path == null){
            throw new AppServiceNotFoundException();
        }

        File file = new File(path);

        if(!file.exists()){
            LOGGER.warn("Le chemin de template spécifié ({}) n'existe pas, utilisation du chemin par défaut", path);

            path = defaultTemplatesSecteurPath.get(vocation);

        }

        return buildGenerationModel(operationEntity, vocation, path);
    }
}

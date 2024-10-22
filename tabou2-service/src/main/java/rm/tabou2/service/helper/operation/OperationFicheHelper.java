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
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class OperationFicheHelper extends AbstractOperationFicheHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationFicheHelper.class);
    private static final String ACTIVITE = "ACTIVITE";
    private static final String HABITAT = "HABITAT";
    private static final String MIXTE = "MIXTE";

    @Autowired
    private VocationDao vocationDao;

    @Value("${fiche.template.operation.activite}")
    private String pathTemplateActiviteOperation;

    @Value("${fiche.template.operation.habitat}")
    private String pathTemplateHabitatOperation;

    @Value("${fiche.template.operation.mixte}")
    private String pathTemplateMixteOperation;

    private final Map<VocationEntity, String> defaultTemplatesPath = new HashMap<>();

    private final Map<VocationEntity, String> configuredTemplatesPath = new HashMap<>();

    @PostConstruct
    private void initDefaultTemplatesPath(){
        defaultTemplatesPath.put(vocationDao.findByCode(ACTIVITE), "template/operation/template_fiche_suivi_activite.odt");
        defaultTemplatesPath.put(vocationDao.findByCode(HABITAT), "template/operation/template_fiche_suivi_habitat.odt");
        defaultTemplatesPath.put(vocationDao.findByCode(MIXTE), "template/operation/template_fiche_suivi_mixte.odt");
    }

    @PostConstruct
    private void initConfiguredTemplatesPath(){
        configuredTemplatesPath.put(vocationDao.findByCode(ACTIVITE), pathTemplateActiviteOperation);
        configuredTemplatesPath.put(vocationDao.findByCode(HABITAT), pathTemplateHabitatOperation);
        configuredTemplatesPath.put(vocationDao.findByCode(MIXTE), pathTemplateMixteOperation);
    }


    public GenerationModel buildGenerationModel(OperationEntity operationEntity) throws AppServiceException {

        String path;
        VocationEntity vocation = operationEntity.getVocation();
        if(vocation == null){
            throw new AppServiceNotFoundException();
        }

        path = configuredTemplatesPath.get(vocation);


        if(path == null){
            throw new AppServiceNotFoundException();
        }

        File file = new File(path);

        if(!file.exists()){
            LOGGER.warn("Le chemin de template spécifié ({}) n'existe pas, utilisation du chemin par défaut", path);


            path = defaultTemplatesPath.get(vocation);

        }

        return buildGenerationModel(operationEntity, vocation, path);
    }
}

package rm.tabou2.service.st.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.ConfigurationApp;
import rm.tabou2.service.exception.AppServiceNotFoundException;
import rm.tabou2.service.st.ConfigurationService;


@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Value("${application.version}")
    private String applicationVersion;

    @Value("${application.comment}")
    private String applicationComment;

    /**
     * Permet de récupérer la configuration
     *
     * @return
     */
    @Override
    public ConfigurationApp getConfiguration(long id) throws AppServiceNotFoundException {


        ConfigurationApp configurationApp = new ConfigurationApp();

        // Ajout de la version contenu dans le fichier properties
        configurationApp.setVersionApp(applicationVersion);
        configurationApp.setComment(applicationComment);

        return configurationApp;

    }

}

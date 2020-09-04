package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rm.tabou2.storage.tabou.dao.ConfigurationDao;
import rm.tabou2.storage.tabou.entity.Configuration;
import rm.tabou2.service.ConfigurationService;
import rm.tabou2.service.dto.ConfigurationApp;
import rm.tabou2.service.exception.AppServiceNotFoundException;
import rm.tabou2.service.mapper.ConfigurationMapper;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    ConfigurationDao configurationDao;

    @Autowired
    ConfigurationMapper configurationMapper;

    @Value("${application.version}")
    private String applicationVersion;

    /**
     * Permet de récupérer la configuration
     *
     * @return
     */
    @Override
    public ConfigurationApp getConfiguration(long id) throws AppServiceNotFoundException {

        // Récupération de la conf en bdd
        Configuration confBdd = configurationDao.findConfigurationById(id);

        if (confBdd == null) {
            throw new AppServiceNotFoundException();
        }

        // Conversion de l'objet "Entity" en "dto"
        ConfigurationApp dto = configurationMapper.entityToDto(confBdd);

        // Ajout de la version contenu dans le fichier properties
        dto.setVersionApp(applicationVersion);

        return dto;

    }

}

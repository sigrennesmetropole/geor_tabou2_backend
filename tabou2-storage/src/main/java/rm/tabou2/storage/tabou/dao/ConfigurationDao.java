package rm.tabou2.storage.tabou.dao;

import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.Configuration;

public interface ConfigurationDao extends CrudRepository<Configuration, Long> {

    /**
     * Récupération d'une conf par son ID
     *
     * @param id
     * @return
     */
    Configuration findConfigurationById(long id);

}

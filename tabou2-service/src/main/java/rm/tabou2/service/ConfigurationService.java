package rm.tabou2.service;

import rm.tabou2.service.dto.ConfigurationApp;
import rm.tabou2.service.exception.AppServiceNotFoundException;

/**
 * Interface de gestion de la configuration
 */
public interface ConfigurationService {

    /**
     * Permet de récupérer la configuration par don Id
     *
     * @param id
     * @return
     */
    ConfigurationApp getConfiguration(long id) throws AppServiceNotFoundException;

}

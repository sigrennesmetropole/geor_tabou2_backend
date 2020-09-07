package rm.tabou2.service;


import rm.tabou2.storage.item.ConnectedUser;
import rm.tabou2.service.exception.AppServiceException;

public interface AuthentificationService {

    /**
     * Authentification via Login/mdp
     *
     * @param login
     * @param mdp
     * @return
     */
    ConnectedUser authentification(final String login, final String mdp) throws AppServiceException;

}

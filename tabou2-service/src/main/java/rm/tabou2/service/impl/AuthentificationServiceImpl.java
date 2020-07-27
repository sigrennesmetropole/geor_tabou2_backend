package rm.tabou2.service.impl;

import org.springframework.stereotype.Service;
import rm.tabou2.storage.item.ConnectedUser;
import rm.tabou2.service.AuthentificationService;
import rm.tabou2.service.exception.AppServiceException;

@Service
public class AuthentificationServiceImpl implements AuthentificationService {

    @Override
    public ConnectedUser authentification(String login, final String mdp) throws AppServiceException {

        // TODO : Authentification
        return null;

    }

}

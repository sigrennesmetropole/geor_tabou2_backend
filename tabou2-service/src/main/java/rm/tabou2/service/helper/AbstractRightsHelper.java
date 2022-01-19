package rm.tabou2.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRightsHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    public boolean checkCanAccess(){
        if(!authentificationHelper.hasViewAccess()){
            LOGGER.warn("L'utilisateur n'a pas les droits de consultation");
            return false;
        }
        return true;
    }

    public boolean checkCanCreate(){
        if(!authentificationHelper.hasEditAccess()){
            LOGGER.warn("L'utilisateur n'a pas les droits de création");
            return false;
        }
        return true;
    }

    public boolean checkCanUpdate(){
        if(!authentificationHelper.hasEditAccess()){
            LOGGER.warn("L'utilisateur n'a pas les droits d'édition");
            return false;
        }
        return true;
    }
}

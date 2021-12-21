package rm.tabou2.service.helper.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;

@Component
public class OutilsFonciersRightsHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutilsFonciersRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    public boolean checkCanSearchOutilsFonciers(){
        if(authentificationHelper.hasConsultationRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits de consultation ne peut pas rechercher les outils fonciers");
            return false;
        }
        return true;
    }

}

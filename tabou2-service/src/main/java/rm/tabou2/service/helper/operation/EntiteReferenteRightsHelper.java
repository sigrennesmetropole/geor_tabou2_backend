package rm.tabou2.service.helper.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;

@Component
public class EntiteReferenteRightsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntiteReferenteRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    /**
     * Vérification des droits de recherche de l'utilisateur pour les entités référentes
     * @return L'utilisateur a le droit de rechercher les entités référentes
     */
    public boolean checkCanSearchEntitesReferentes(){
        if(authentificationHelper.hasConsultationRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits de consultation ne peut pas rechercher les entités Rennes Métropole");
            return false;
        }
        return true;
    }
}

package rm.tabou2.service.helper.tiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;

@Component
public class TiersRightsHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TiersRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    /**
     * Vérifie si l'utilisateur est autorisé à créer un Tiers
     * @return L'utilisateur peut créer un Tiers
     */
    public boolean checkCanCreateTiers(){
        if(!authentificationHelper.hasEditAccess() || authentificationHelper.hasReferentRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits d'édition ne peut pas créer un tiers");
            return false;
        }
        return true;
    }

    /**
     * Vérifie si l'utilisateur est autorisé à mettre à jour un Tiers
     * @return L'utilisateur peut mettre à jour un Tiers
     */
    public boolean checkCanUpdateTiers(){
        if(!authentificationHelper.hasEditAccess() || authentificationHelper.hasReferentRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits d'édition ne peut pas éditer un tiers");
            return false;
        }
        return true;
    }

    /**
     * Vérifie si l'utilisateur est autorisé à accéder à un Tiers
     * @return L'utilisateur peut accéder à un Tiers
     */
    public boolean checkCanGetTiers(){
        if(!authentificationHelper.hasConsultationRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits de consultation ne peut pas accéder à un tiers");
            return false;
        }
        return true;
    }
}

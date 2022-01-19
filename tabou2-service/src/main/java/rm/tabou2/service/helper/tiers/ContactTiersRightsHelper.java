package rm.tabou2.service.helper.tiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;

@Component
public class ContactTiersRightsHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactTiersRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    /**
     * Vérifie si l'utilisateur est autorisé à créer un ContactTiers
     * @return L'utilisateur peut créer un ContactTiers
     */
    public boolean checkCanCreateContactTiers(){
        if(!authentificationHelper.hasEditAccess() && !authentificationHelper.hasReferentRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits d'édition ne peut pas créer un contact de tiers");
            return false;
        }
        return true;
    }

    /**
     * Vérifie si l'utilisateur est autorisé à mettre à jour un ContactTiers
     * @return L'utilisateur peut mettre à jour un ContactTiers
     */
    public boolean checkCanUpdateContactTiers(){
        if(!authentificationHelper.hasEditAccess() && !authentificationHelper.hasReferentRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits d'édition ne peut pas éditer un contact de tiers");
            return false;
        }
        return true;
    }

    /**
     * Vérifie si l'utilisateur est autorisé à accéder à un ContactTiers
     * @return L'utilisateur peut accéder à un ContactTiers
     */
    public boolean checkCanGetContactTiers(){
        if(!authentificationHelper.hasConsultationRole()){
            LOGGER.warn("L'utilisateur n'ayant pas les droits de consultation ne peut pas accéder à un contact de tiers");
            return false;
        }
        return true;
    }
}

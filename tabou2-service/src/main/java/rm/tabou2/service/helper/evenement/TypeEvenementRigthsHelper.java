package rm.tabou2.service.helper.evenement;

import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

@Component
public class TypeEvenementRigthsHelper {

    private final AuthentificationHelper authentificationHelper;

    public TypeEvenementRigthsHelper(AuthentificationHelper authentificationHelper) {
        this.authentificationHelper = authentificationHelper;
    }

    /**
     * Vérifie si l'utilisateur a les droits admin de récupérer un type d'événement système
     * @param typeEvenementEntity   type d'événement
     * @return                      true si l'utilisateur peut le récupérer
     */
    public boolean canGetTypeEvenement(TypeEvenementEntity typeEvenementEntity) {
        return !typeEvenementEntity.isSysteme() || authentificationHelper.hasAdministratorRole();
    }
}

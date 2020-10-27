package rm.tabou2.service.helper.programme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.AuthentificationHelper;



@Component
public class ProgrammeRightsHelper {

    @Autowired
    private AuthentificationHelper authentificationHelper;

    /**
     * Vérifie si l'utilisateur a les droits d'ajouter un programme
     * @param programme programme
     * @return true si l'utilisateur peut ajouter le programme
     */
    public boolean checkCanCreateProgramme(Programme programme) {
        // L'utilisateur peut créer un programme en étant au moins contributeur en diffusion non restreinte
        boolean checkDiffusionNonRestreinte = !programme.isDiffusionRestreinte() && authentificationHelper.hasEditAccess();
        // L'utilisateur peut créer un programme en étant au moins référent en diffusion restreinte
        boolean checkDiffusionRestreinte = programme.isDiffusionRestreinte() && authentificationHelper.hasRestreintAccess();

        return checkDiffusionNonRestreinte || checkDiffusionRestreinte;
    }

    /**
     * Vérifie si l'utilisateur a les droits de modifier un programme
     * @param programme programme
     * @param diffusionRestreinteChanged true si le paramètre diffusionRestreinte est modifié
     * @return true si l'utilisateur peut modifier le programme
     */
    public boolean checkCanUpdateProgramme(Programme programme, boolean diffusionRestreinteChanged) {
        return diffusionRestreinteChanged
                ? authentificationHelper.hasRestreintAccess()
                : checkCanCreateProgramme(programme);
    }
}

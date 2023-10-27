package rm.tabou2.service.helper.programme;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;

@Component
public class EvenementProgrammeRigthsHelper {

    @Autowired
    private ProgrammeRightsHelper programmeRightsHelper;

    /**
     * Permet de savoir si l'utilisateur a le droit de modifier un événement d'un programme
     * @param programme         programme
     * @param actualEvenement   événement actuel
     * @return true si l'utilisateur peut modifier
     */
    public boolean checkCanUpdateEvenementProgramme(Programme programme, Evenement actualEvenement) {
        return programmeRightsHelper.checkCanUpdateProgramme(programme, programme.getDiffusionRestreinte())
                && BooleanUtils.isFalse(actualEvenement.getSysteme());
    }
}

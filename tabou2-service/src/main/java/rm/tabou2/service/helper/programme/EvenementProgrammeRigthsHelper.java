package rm.tabou2.service.helper.programme;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;

@Component
@RequiredArgsConstructor
public class EvenementProgrammeRigthsHelper {

    private final ProgrammeRightsHelper programmeRightsHelper;

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

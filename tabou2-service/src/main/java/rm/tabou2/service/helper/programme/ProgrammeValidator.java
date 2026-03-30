package rm.tabou2.service.helper.programme;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.storage.tabou.dao.programme.EtapeProgrammeDao;

@Component
@RequiredArgsConstructor
public class ProgrammeValidator {

    private final EtapeProgrammeWorkflowHelper etapeProgrammeWorkflowHelper;

    private final EtapeProgrammeDao etapeProgrammeDao;

    /**
     * Vérifie si les contraintes métiers sont bien vérifiées
     * @param programme programme
     */
    public void validateProgramme(Programme programme) throws AppServiceException {
        Etape etape = programme.getEtape();
        if(etape == null || (etape.getId() == null && etape.getCode() == null)){
            throw new AppServiceException("L'étape passée en paramètre ne contient ni code, ni id", AppServiceExceptionsStatus.BADREQUEST);
        }

        // validation de l'étape
        boolean etapeValidation = etapeProgrammeWorkflowHelper.checkCanAssignEtapeToProgramme(etape, programme.getId());
        if (!etapeValidation) {
            String identifiant = etape.getCode();
            if(identifiant == null){
                identifiant = etapeProgrammeDao.findOneById(etape.getId()).getCode();
            }
            throw new AppServiceException("L'étape " + identifiant + " ne peut-être attribuée au programme " + programme.getNom(), AppServiceExceptionsStatus.BADREQUEST);
        }
    }

    public void validateUpdateProgramme(Programme programme) throws AppServiceException {
        validateProgramme(programme);
    }

}

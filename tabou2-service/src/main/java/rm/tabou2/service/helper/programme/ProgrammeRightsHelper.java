package rm.tabou2.service.helper.programme;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;


@Component
public class ProgrammeRightsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammeRightsHelper.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private OperationDao operationDao;

    /**
     * Vérifie si l'utilisateur a les droits d'ajouter un programme
     * @param programme programme
     * @return true si l'utilisateur peut ajouter le programme
     */
    public boolean checkCanCreateProgramme(Programme programme) {
        // L'utilisateur peut créer un programme en étant au moins contributeur en diffusion non restreinte
        if (BooleanUtils.isFalse(programme.getDiffusionRestreinte()) && !authentificationHelper.hasEditAccess()) {
            LOGGER.warn("L'utilisateur n'ayant pas les droits d'édition ne peut pas créer un programme");
            return false;
        }
        // L'utilisateur peut créer un programme en étant au moins référent en diffusion restreinte
        if (BooleanUtils.isTrue(programme.getDiffusionRestreinte()) && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn("L'utilisateur n'ayant pas au moins le rôle référent ne peut pas créer un programme en diffusion restreinte");
            return false;
        }

        // Vérification de l'accès à l'éventuelle opération associée
        if (!operationRightsHelper.checkCanGetOperation(operationDao.findOneById(programme.getOperationId()))) {
            String errorMessage = String.format("L'utilisateur n'a pas les droits de récupérer l'opération %d", programme.getOperationId());
            LOGGER.warn(errorMessage);
            return false;
        }

        return true;
    }

    /**
     * Vérifie si l'utilisateur a les droits de modifier un programme
     * @param programme programme
     * @param actualDiffusionRestreinte actuelle diffusion restreinte du programme avant modification
     * @return true si l'utilisateur peut modifier le programme
     */
    public boolean checkCanUpdateProgramme(Programme programme, boolean actualDiffusionRestreinte) {

        if (!actualDiffusionRestreinte && !authentificationHelper.hasEditAccess()) {
            LOGGER.warn("Le programme ne peut être modifié par un utilisateur qui n'a pas les droits de modification");
            return false;
        }

        if (actualDiffusionRestreinte && !authentificationHelper.hasRestreintAccess()) {
            LOGGER.warn("Le programme avec diffusion restreinte ne peut être modifié par un utilisateur qui n'a pas les droits");
            return false;
        }

        // Vérification de l'accès à l'éventuelle opération associée
        if (programme.getOperationId() != null && !operationRightsHelper.checkCanGetOperation(operationDao.findOneById(programme.getOperationId()))) {
            String errorMessage = String.format("L'utilisateur n'a pas les droits de récupérer l'opération %s", programme.getOperationId());
            LOGGER.warn(errorMessage);
            return false;
        }

        return true;
    }

    /**
     * Vérifie si l'utilisateur peut récupérer un programme
     * @param programme programme
     * @return true si l'utilisateur peut récupérer le programme
     */
    public boolean checkCanGetProgramme(Programme programme) {
        return BooleanUtils.isFalse(programme.getDiffusionRestreinte()) || authentificationHelper.hasRestreintAccess();
    }

    /**
     * Vérifie si l'utilisateur peut récupérer un programme
     * @param programmeEntity programme
     * @return true si l'utilisateur peut récupérer le programme
     */
    public boolean checkCanGetProgramme(ProgrammeEntity programmeEntity) {
        return !programmeEntity.isDiffusionRestreinte() || authentificationHelper.hasRestreintAccess();
    }

    /**
     * Vérifie si l'utilisateur peut récupérer la liste des étapes possible pour un programme
     * @param idProgramme identifiant du programme
     * @return true si l'utilisateur le peut
     */
    public boolean checkCanGetEtapesForProgramme(long idProgramme) {
        ProgrammeEntity programmeEntity = programmeDao.findOneById(idProgramme);
        return !programmeEntity.isDiffusionRestreinte() || authentificationHelper.hasRestreintAccess();
    }
}

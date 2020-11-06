package rm.tabou2.service.tabou.programme;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.validator.ValidEvenementCreation;
import rm.tabou2.service.validator.ValidEvenementUpdate;
import rm.tabou2.service.validator.ValidProgrammeCreation;
import rm.tabou2.service.validator.ValidProgrammeUpdate;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import java.util.List;

public interface ProgrammeService {


    /**
     * Ajout d'un programme.
     *
     * @param programme programme à ajouter
     * @return programme ajouté
     */
    Programme createProgramme(@ValidProgrammeCreation Programme programme);

    /**
     * Mise à jour d'un programme.
     *
     * @param programme programme à modifier
     * @return programme modifié
     */
    Programme updateProgramme(@ValidProgrammeUpdate Programme programme);

    /**
     * Modification de l'étape d'un programme
     * @param programmeId Identifiant du programme
     * @param etapeId identifiant de l'étape
     * @return programme modifié
     */
    Programme updateEtapeOfProgrammeId (long programmeId, long etapeId);

    /**
     * Récupération d'un programme par son identifiant.
     *
     * @param programmeId identifiant du programme
     * @return programme
     */
    Programme getProgrammeById(long programmeId);

    /**
     * Recherche de programme à partir des paramètres.
     *
     * @param programmeCriteria paramètres des programmes
     * @param pageable paramètre lié à la pagination
     * @return Liste des programmes correspondants à la recherche
     */
    Page<Programme> searchProgrammes(ProgrammeCriteria programmeCriteria, Pageable pageable);

    /**
     * Récupération de la liste des évènements d'un programme
     * @param programmeId               identifiant du programme
     * @return                          liste des évènements
     * @throws AppServiceException      erreur lors de la récupération des évènements
     */
    List<Evenement> getEvenementsByProgrammeId(Long programmeId) throws AppServiceException;

    /**
     * Ajout d'un évènement système pour un programme
     * @param programmeId           identifiant du programme
     * @param evenement             evenement
     * @return                      evenement crée
     * @throws AppServiceException  erreur lors de l'enregistrement du programme
     */
    Evenement addEvenementSystemeByProgrammeId(Long programmeId, @ValidEvenementCreation Evenement evenement) throws AppServiceException;

    /**
     * Ajout d'un évènement non système pour un programme
     * @param programmeId           identifiant du programme
     * @param evenement             evenement
     * @return                      evenement crée
     * @throws AppServiceException  erreur lors de l'enregistrement du programme
     */
    Evenement addEvenementNonSystemeByProgrammeId(Long programmeId, @ValidEvenementCreation Evenement evenement) throws AppServiceException;

    /**
     * Modification d'un événement d'un programme
     * @param programmeId           identifiant du programme
     * @param evenement             événement à modifier
     * @return                      événement modifié
     * @throws AppServiceException  erreur lors de la mise à jour de l'événement
     */
    Evenement updateEvenementByProgrammeId(long programmeId, @ValidEvenementUpdate Evenement evenement) throws AppServiceException;
}

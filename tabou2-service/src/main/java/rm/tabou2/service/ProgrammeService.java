package rm.tabou2.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.validator.ValidProgrammeCreation;
import rm.tabou2.service.validator.ValidProgrammeUpdate;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

public interface ProgrammeService {


    /**
     * Ajout d'un programme.
     *
     * @param programme programme à ajouter
     * @return programme ajouté
     */
    Programme addProgramme(@ValidProgrammeCreation Programme programme);

    /**
     * modification d'un programme
     *
     * @param programme programme à modifier
     * @return programme modifié
     */
    Programme editProgramme(@ValidProgrammeUpdate Programme programme) throws AppServiceException;

    /**
     * Récupération d'un programme par son identifiant.
     *
     * @param programmeId identifiant du programme
     * @return programme
     */
    Programme getProgrammeById(long programmeId) ;

    /**
     * Recherche de programme à partir des paramètres.
     *
     * @param programmeCriteria paramètres des programmes
     * @param pageable paramètre lié à la pagination
     * @return Liste des programme correspondant à la recherche
     */
    Page<Programme> searchProgrammes(ProgrammeCriteria programmeCriteria, Pageable pageable);
}

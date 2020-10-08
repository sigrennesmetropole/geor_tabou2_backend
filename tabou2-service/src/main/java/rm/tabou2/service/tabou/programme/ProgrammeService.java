package rm.tabou2.service.tabou.programme;


import rm.tabou2.service.dto.Programme;

import java.util.List;

public interface ProgrammeService {


    /**
     * Ajout d'un programme.
     *
     * @param programme programme à ajouter
     * @return programme ajouté
     */
    Programme addProgramme(Programme programme);

    /**
     * Récupération d'un programme par son identifiant.
     *
     * @param programmeId identifiant du programme
     * @return programme
     */
    Programme getProgrammeById(long programmeId);

    /**
     * Recherche de programme à partir d'un mot clé.
     *
     * @param keyword       mot clé à rechercher
     * @param start         index du début
     * @param resultsNumber nombre de résultats à retourner
     * @param orderBy       nom du champ sur lequel trier
     * @param asc           true si tri ascendant, faux sinon
     * @return Liste des programme correspondant à la recherche
     */
    List<Programme> searchProgrammes(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc);
}

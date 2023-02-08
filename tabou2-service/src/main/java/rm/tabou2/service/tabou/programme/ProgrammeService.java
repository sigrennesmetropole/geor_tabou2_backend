package rm.tabou2.service.tabou.programme;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.ProgrammeLight;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.validator.evenement.ValidEvenementCreation;
import rm.tabou2.service.validator.evenement.ValidEvenementUpdate;
import rm.tabou2.service.validator.programme.ValidProgrammeCreation;
import rm.tabou2.service.validator.programme.ValidProgrammeUpdate;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import java.util.Date;
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
    Programme updateProgramme(@ValidProgrammeUpdate Programme programme) throws AppServiceException;

    /**
     * Modification de l'étape d'un programme
     *
     * @param programmeId Identifiant du programme
     * @param etapeId     identifiant de l'étape
     * @return programme modifié
     */
    Programme updateEtapeOfProgrammeId(long programmeId, long etapeId) throws AppServiceException;

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
     * @param pageable          paramètre lié à la pagination
     * @return Liste des programmes correspondants à la recherche
     */
    Page<Programme> searchProgrammes(ProgrammeCriteria programmeCriteria, Pageable pageable);

    /**
     * Recherche les programmes d'une opération (secteur) par intersection spatiale.
     *
     * @param programmeCriteria paramètres des programmes
     * @param pageable          paramètres de la pagination
     * @return Liste des programmes correspondants à la recherche
     */
    Page<ProgrammeLight> searchProgrammesOfOperation(ProgrammeCriteria programmeCriteria, Pageable pageable);

    /**
     * Récupération de la liste des évènements d'un programme
     *
     * @param programmeId identifiant du programme
     * @return liste des évènements
     * @throws AppServiceException erreur lors de la récupération des évènements
     */
    List<Evenement> getEvenementsByProgrammeId(Long programmeId) throws AppServiceException;

    /**
     * Ajout d'un évènement pour un programme
     *
     * @param programmeId identifiant du programme
     * @param evenement   evenement
     * @return evenement crée
     * @throws AppServiceException erreur lors de l'enregistrement du programme
     */
    Evenement addEvenementByProgrammeId(Long programmeId, @ValidEvenementCreation Evenement evenement) throws AppServiceException;

    /**
     * Modification d'un événement d'un programme
     *
     * @param programmeId identifiant du programme
     * @param evenement   événement à modifier
     * @return événement modifié
     * @throws AppServiceException erreur lors de la mise à jour de l'événement
     */
    Evenement updateEvenementByProgrammeId(long programmeId, @ValidEvenementUpdate Evenement evenement) throws AppServiceException;

    /**
     * Génération de la fiche suivi d'un programme
     *
     * @param programmeId identifiant du programme
     * @return document
     * @throws AppServiceException erreur lors de la génératino de la fiche suivi
     */
    DocumentContent generateFicheSuivi(Long programmeId) throws AppServiceException;

    /**
     * Retourne la liste des emprises des programmes non suivies.
     *
     * @param nom         nom du programme
     * @param operationId identifiant de l'opération
     * @param pageable    paramètres de la pagination
     * @return Listes des programmes sans suivi d'emprise
     */
    Page<Emprise> getEmprisesAvailables(String nom, Long operationId, Pageable pageable);

    /**
     * Téléchargement du contenu du document.
     *
     * @param programmeId identifiant du programme
     * @param documentId identifiant du document
     * @return
     * @throws AppServiceException
     */

    DocumentContent downloadDocument(long programmeId, String documentId) throws AppServiceException;

    /**
     * Récupération des métadonnées d'un document.
     *
     * @param programmeId identifiant du programme
     * @param documentId  identifiant du document
     * @return DocumentMetadata
     * @throws AppServiceException
     */
    DocumentMetadata getDocumentMetadata(long programmeId, String documentId) throws AppServiceException;

    /**
     * Ajout d'un document à un programme
     *
     * @param programmeId identifiant du programme
     * @param nom nom du document
     * @param libelleTypeDocument libellé du type de document
     * @param file document à ajouter
     * @param datedocument
     * @return métadonnées du document
     * @throws AppServiceException erreur lors de l'ajout d'un document
     */
    DocumentMetadata addDocument(long programmeId, String nom, String libelleTypeDocument, MultipartFile file, Date datedocument) throws AppServiceException;

    /**
     * Suppresion d'un document dans Alfresco.
     *
     * @param programmeId identifiant du programme
     * @param documentId  identifiant du document
     * @throws AppServiceException
     */
    void deleteDocument(long programmeId, String documentId) throws AppServiceException;

    /**
     * Mise à jour des métadonnées d'un document d'un projet.
     *
     * @param programmeId      identifiant du programme
     * @param documentId       identifiant du document
     * @param documentMetadata métadonnées du document
     * @return métadonnées
     * @throws AppServiceException
     */
    DocumentMetadata updateDocumentMetadata(long programmeId, String documentId, DocumentMetadata documentMetadata) throws AppServiceException;

    /**
     * Mise à jour du contenu d'un document.
     *
     * @param programmeId identifiant d'un programme
     * @param documentId identifiant du document
     * @param file fichier à mettre à jour
     * @throws AppServiceException
     */
    void updateDocumentContent(long programmeId, String documentId, MultipartFile file) throws AppServiceException;

    /**
     * Recherche des documents d'un programme.
     *
     * @param programmeId identifiant d'un programme
     * @param nom nom du document
     * @param libelleTypeDocument libellé du type de document
     * @param typeMime typeMime d'un document
     * @param pageable paramètres de pagination
     * @return liste des documents
     */
    Page<DocumentMetadata> searchDocuments(long programmeId, String nom, String libelleTypeDocument, String typeMime, Pageable pageable)
            throws AppServiceException;


}

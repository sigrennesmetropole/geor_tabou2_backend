package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.dto.TypePLHBean;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.validator.evenement.ValidEvenementCreation;
import rm.tabou2.service.validator.evenement.ValidEvenementUpdate;
import rm.tabou2.service.validator.operation.ValidOperationCreation;
import rm.tabou2.service.validator.operation.ValidOperationUpdate;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.time.LocalDateTime;
import java.util.List;

public interface OperationService {

    /**
     * Création d'une opération
     *
     * @param operation opération à créer
     * @return opération crée
     */
    OperationIntermediaire createOperation(@ValidOperationCreation OperationIntermediaire operation)
            throws AppServiceException;

    /**
     * Mise à jour d'une opération
     *
     * @param operation opération à modifier
     * @return opération modifiée
     */
    OperationIntermediaire updateOperation(@ValidOperationUpdate OperationIntermediaire operation)
            throws AppServiceException;

    /**
     * Récupération d'une opération
     *
     * @param operationId identifiant de l'opération
     * @return opération
     */
    OperationIntermediaire getOperationById(long operationId);

    /**
     * Modification de l'étape d'une opération
     *
     * @param operationId Identifiant de l'opération
     * @param etapeId     identifiant de l'étape
     * @return opération modifiée
     * @throws AppServiceException Erreur lors de la mise à jour d'une opération
     */
    OperationIntermediaire updateEtapeOfOperationId(long operationId, long etapeId) throws AppServiceException;

    /**
     * Recherche des opérations à partir des paramètres.
     *
     * @param operationsCriteria paramètres des opérations
     * @param pageable           paramètre lié à la pagination
     * @return Liste des opérations correspondantes à la recherche
     */
    Page<OperationIntermediaire> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable);

    /**
     * Récupération de la liste des évènements d'une opération
     *
     * @param operationId identifiant de l'opération
     * @return liste des évènements
     */
    List<Evenement> getEvenementsByOperationId(Long operationId);

    /**
     * Ajout d'un évènement pour une opération
     *
     * @param operationId identifiant de l'opération
     * @param evenement   événement
     * @return événement crée
     * @throws AppServiceException erreur lors de l'enregistrement de l'opération
     */
    Evenement addEvenementByOperationId(Long operationId, @ValidEvenementCreation Evenement evenement)
            throws AppServiceException;

    /**
     * Modification d'un événement d'une opération
     *
     * @param idOperation identifiant de l'opération
     * @param evenement   événement à modifier
     * @return événement modifié
     * @throws AppServiceException erreur lors de la mise à jour de l'événement
     */
    Evenement updateEvenementByOperationId(long idOperation, @ValidEvenementUpdate Evenement evenement)
            throws AppServiceException;

    /**
     * Récupération des métadonnées d'un document Alfresco d'une opération.
     *
     * @param operationId identifiant de l'opération
     * @param documentId  identifiant du document
     * @return document
     */
    DocumentMetadata getDocumentMetadata(long operationId, String documentId) throws AppServiceException;

    /**
     * Mise à jour des métadonnées d'un document.
     *
     * @param operationId      identifiant de l'opération
     * @param documentId       identifiant du document
     * @param documentMetadata métadonnées d'un document
     * @return Contenu du document
     * @throws AppServiceException exception si erreur lors du téléchargement du
     *                             contenu du document
     */
    DocumentMetadata updateDocumentMetadata(long operationId, String documentId, DocumentMetadata documentMetadata)
            throws AppServiceException;

    /**
     * Télécharge le contenu d'un document d'une opération.
     *
     * @param operationId identifiant de l'opération
     * @param documentId  identifiant du document
     * @return contenu du document
     * @throws AppServiceException erreur lors de la récupération du contenu d'un
     *                             document
     */
    DocumentContent downloadDocument(long operationId, String documentId) throws AppServiceException;

    /**
     * Recherche de document.
     *
     * @param operationId         identifiant de l'opération
     * @param nom                 nom du document
     * @param libelleTypeDocument libellé du type de document
     * @param typeMime            libellé du type MIME du document
     * @param pageable            paramètres de pagination
     */
    Page<DocumentMetadata> searchDocuments(long operationId, String nom, String libelleTypeDocument,
                                           String typeMime,
                                           Pageable pageable);

    /**
     * Mise à jour du contenu d'un document.
     *
     * @param operationId identifiant d'une opération
     * @param documentId  identifiant du document
     * @param file        fichier à mettre à jour
     * @throws AppServiceException erreur lors de la mise à jour du contenu d'un
     *                             document
     */
    void updateDocumentContent(long operationId, String documentId, MultipartFile file) throws AppServiceException;

    /**
     * Ajout d'un document.
     *
     * @param operationId         identifiant de l'opération
     * @param nom                 nom du document
     * @param libelleTypeDocument libellé du type de document
     * @param dateDocument        la date du document
     * @param file                document à ajouter
     * @return métadonnées du document
     * @throws AppServiceException erreur lors de l'ajout d'un document
     */
    DocumentMetadata addDocument(long operationId, String nom, String libelleTypeDocument,
                                 LocalDateTime dateDocument,
                                 Object file) throws AppServiceException;

    /**
     * Suppression d'un document d'une opération.
     *
     * @param operationId identifiant de l'opération
     * @param documentId  identifiant du document
     */
    void deleteDocument(long operationId, String documentId) throws AppServiceException;

    /**
     * Génère la fiche de suivi d'une opération.
     *
     * @param operationId identifiant de l'opération
     * @return La fiche de suivi
     */
    DocumentContent generateFicheSuivi(Long operationId) throws AppServiceException;

    /**
     * Mise à jour d'un type PLH associé à une opération
     *
     * @param operationId identifiant de l'opération
     * @param typePLHId   identifiant du type PLH
     * @param typePLH     type PLH à mettre à jour
     * @return le type PLH mis à jour
     */
    TypePLH updatePLHOperation(long operationId, long typePLHId, TypePLH typePLH) throws AppServiceException;

    /**
     * Récupération d'un type PLH associé à une opération
     *
     * @param operationId identifiant de l'opération
     * @param typePLHId   identifiant du type PLH
     * @return le type PLH peuplé avec ses valeurs
     */
    TypePLH getPLHOperation(long operationId, long typePLHId) throws AppServiceException;

    /**
     * Suppression du lien entre une opération et un type PLH
     *
     * @param operationId identifiant de l'opération
     * @param typePLHId   identifiant du type PLH
     * @throws AppServiceException si l'opération ou le type PLH est introuvable
     */
    void removePLHOperationById(long operationId, long typePLHId) throws AppServiceException;

    /**
     * Ajout d'un type PLH à une opération
     *
     * @param operationId identifiant de l'opération
     * @param typePLHId   identifiant du type PLH à associer
     * @return le type PLH peuplé avec ses valeurs
     * @throws AppServiceException si l'opération ou le type PLH est introuvable
     */
    TypePLH addPLHOperationById(long operationId, long typePLHId) throws AppServiceException;

    /**
     * Récupération de la liste des types PLH associés à une opération
     *
     * @param operationId identifiant de l'opération
     * @return la liste des TypePLHBean associés
     * @throws AppServiceException si l'opération est introuvable
     */
    List<TypePLHBean> getPLHsOperation(long operationId) throws AppServiceException;

}

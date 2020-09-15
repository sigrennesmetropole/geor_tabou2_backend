package rm.tabou2.service;

import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.exception.AppServiceException;

import java.util.List;

public interface TypeDocumentService {

    /**
     * Ajout d'un nouveau type de document.
     *
     * @param typeDocument type de document à ajouter
     * @return type de document ajouté
     */
    TypeDocument addTypeDocument(TypeDocument typeDocument) throws AppServiceException;

    /**
     * Edition d'un type de document.
     *
     * @param typeDocument type de document à ajouter
     * @return type de document ajouté
     */
    TypeDocument editTypeDocument(TypeDocument typeDocument) throws AppServiceException;

    /**
     * Désactivation d'un type de document.
     *
     * @param typeDocumentId identifiant du type de document
     */
    TypeDocument inactivateTypeDocument(Long typeDocumentId) throws AppServiceException;

    /**
     * Récuperer la liste des types de document.
     *
     * @param keyword
     * @param start
     * @param onlyActive
     * @param resultsNumber
     * @param orderBy
     * @param asc
     */
    List<TypeDocument> searchTypeDocument(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;
}

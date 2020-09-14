package rm.tabou2.service;

import rm.tabou2.service.dto.TypeDocument;

import java.util.List;

public interface TypeDocumentService {

    /**
     * Ajout d'un nouveau type de document.
     *
     * @param typeDocument type de document à ajouter
     * @return type de document ajouté
     */
    TypeDocument addTypeDocument(TypeDocument typeDocument);

    /**
     * Désactivation d'un type de document.
     *
     * @param typeDocumentId identifiant du type de document
     */
    void inactivateTypeDocument(Long typeDocumentId);

    List<TypeDocument> searchTypeDocument(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;
}

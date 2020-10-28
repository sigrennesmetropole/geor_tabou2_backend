package rm.tabou2.service.tabou.document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.storage.tabou.item.TypeDocumentCriteria;



public interface TypeDocumentService {

    /**
     * Ajout d'un nouveau type de document.
     *
     * @param typeDocument type de document à ajouter
     * @return type de document ajouté
     */
    TypeDocument createTypeDocument(TypeDocument typeDocument) throws AppServiceException;

    /**
     * Edition d'un type de document.
     *
     * @param typeDocument type de document à ajouter
     * @return type de document ajouté
     */
    TypeDocument updateTypeDocument(TypeDocument typeDocument) throws AppServiceException;

    /**
     * Désactivation d'un type de document.
     *
     * @param typeDocumentId identifiant du type de document
     */
    TypeDocument inactivateTypeDocument(Long typeDocumentId) throws AppServiceException;

    /**
     * Récuperer la liste des types de document.
     *
     */
    Page<TypeDocument> searchTypeDocument(TypeDocumentCriteria typeDocumentCriteria, Pageable pageable);
}
package rm.tabou2.service;

import rm.tabou2.service.dto.TypeDocument;

import java.util.List;

public interface TypeDocumentService {
    TypeDocument addTypeDocument(TypeDocument typeDocument);

    void inactivateTypeDocument(Long typeDocumentId);

    List<TypeDocument> searchTiers(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;
}

package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesDocumentsApi;
import rm.tabou2.service.TypeDocumentService;
import rm.tabou2.service.dto.TypeDocument;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TypeDocumentApiController implements TypesDocumentsApi {

    @Autowired
    private TypeDocumentService typeDocumentService;

    @Override
    public ResponseEntity<TypeDocument> addTypeDocument(@Valid TypeDocument typeDocument) throws Exception {

        return new ResponseEntity<>(typeDocumentService.editTypeDocument(typeDocument), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<TypeDocument> editTypeDocument(@Valid TypeDocument typeDocument) throws Exception {
        return new ResponseEntity<>(typeDocumentService.addTypeDocument(typeDocument), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TypeDocument>> getTypeDocument(@Valid String keyword, @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        return new ResponseEntity<>(typeDocumentService.searchTypeDocument(keyword, start, onlyActive, resultsNumber, orderBy, asc), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeDocument> inactivateTypeDocument(Long typeDocumentId) throws Exception {
        return new ResponseEntity<>( typeDocumentService.inactivateTypeDocument(typeDocumentId), HttpStatus.OK);
    }



}

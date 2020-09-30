package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesDocumentsApi;
import rm.tabou2.service.TypeDocumentService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.utils.PaginationUtils;

import javax.validation.Valid;
import java.util.Date;
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
    public ResponseEntity<PageResult> getTypeDocument(@Valid Long typeDocumentId, @Valid String libelle, @Valid Date dateInactif, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PageRequest.of(start, resultsNumber, Sort.by(Sort.Direction.ASC, orderBy));


        Page<TypeDocument> page = typeDocumentService.searchTypeDocument(typeDocumentId, libelle, dateInactif, pageable);

        return new ResponseEntity<>( PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<TypeDocument> inactivateTypeDocument(Long typeDocumentId) throws Exception {
        return new ResponseEntity<>( typeDocumentService.inactivateTypeDocument(typeDocumentId), HttpStatus.OK);
    }



}

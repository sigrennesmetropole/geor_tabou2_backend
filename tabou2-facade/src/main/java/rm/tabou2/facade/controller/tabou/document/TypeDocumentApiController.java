package rm.tabou2.facade.controller.tabou.document;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rm.tabou2.facade.api.TypesDocumentsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.service.tabou.document.TypeDocumentService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;
import rm.tabou2.storage.tabou.item.TypeDocumentCriteria;

@RestController
@RequiredArgsConstructor
public class TypeDocumentApiController implements TypesDocumentsApi {


    private final TypeDocumentService typeDocumentService;
    
    private final DateHelper dateHelper;

    @Override
    public ResponseEntity<TypeDocument> createTypeDocument(TypeDocument typeDocument) throws Exception {
        return new ResponseEntity<>(typeDocumentService.createTypeDocument(typeDocument), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeDocument> getTypeDocumentById(Long typeDocumentId) throws Exception {
        return new ResponseEntity<>(typeDocumentService.getTypeDocumentById(typeDocumentId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeDocument> updateTypeDocument(TypeDocument typeDocument) throws Exception {
        return new ResponseEntity<>(typeDocumentService.updateTypeDocument(typeDocument), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTypeDocument(Long typeDocumentId, String libelle, OffsetDateTime dateInactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        TypeDocumentCriteria typeDocumentCriteria = new TypeDocumentCriteria();
        typeDocumentCriteria.setId(typeDocumentId);
        typeDocumentCriteria.setLibelle(libelle);
        typeDocumentCriteria.setDateInactif(dateHelper.convert(dateInactif));

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeDocumentEntity.class);

        Page<TypeDocument> page = typeDocumentService.searchTypeDocument(typeDocumentCriteria, pageable);

        return new ResponseEntity<>( PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<TypeDocument> inactivateTypeDocument(Long typeDocumentId) throws Exception {
        return new ResponseEntity<>( typeDocumentService.inactivateTypeDocument(typeDocumentId), HttpStatus.OK);
    }



}

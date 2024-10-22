package rm.tabou2.facade.controller.tabou.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypesDocumentsApi;
import rm.tabou2.service.tabou.document.TypeDocumentService;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;
import rm.tabou2.storage.tabou.item.TypeDocumentCriteria;

import java.util.Date;

@RestController
public class TypeDocumentApiController implements TypesDocumentsApi {


    @Autowired
    private TypeDocumentService typeDocumentService;

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
    public ResponseEntity<PageResult> searchTypeDocument(Long typeDocumentId, String libelle, Date dateInactif, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        TypeDocumentCriteria typeDocumentCriteria = new TypeDocumentCriteria();
        typeDocumentCriteria.setId(typeDocumentId);
        typeDocumentCriteria.setLibelle(libelle);
        typeDocumentCriteria.setDateInactif(dateInactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeDocumentEntity.class);

        Page<TypeDocument> page = typeDocumentService.searchTypeDocument(typeDocumentCriteria, pageable);

        return new ResponseEntity<>( PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<TypeDocument> inactivateTypeDocument(Long typeDocumentId) throws Exception {
        return new ResponseEntity<>( typeDocumentService.inactivateTypeDocument(typeDocumentId), HttpStatus.OK);
    }



}

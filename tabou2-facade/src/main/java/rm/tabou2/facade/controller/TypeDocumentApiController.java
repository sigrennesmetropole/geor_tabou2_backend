package rm.tabou2.facade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.TypesDocumentApi;
import rm.tabou2.service.dto.TypeDocument;

import java.util.List;

@Controller
public class TypeDocumentApiController implements TypesDocumentApi {


    @Override
    public ResponseEntity<List<TypeDocument>> inactivateTypeDocument(String typeDocumentId) throws Exception {
        return null;
    }
}

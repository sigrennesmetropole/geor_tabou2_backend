package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.SecteursSpeuApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.service.sig.SecteurSpeuService;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

import javax.validation.Valid;

@Controller
public class SecteurSpeuApiController extends AbstractExportDocumentApi implements SecteursSpeuApi {

    @Autowired
    private SecteurSpeuService secteurSpeuService;

    @Autowired
    private OperationService operationService;

    @Override
    public ResponseEntity<PageResult> searchSecteursSpeu(@Valid Integer numSecteur, @Valid String nomSecteur, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurSpeuEntity.class);

        Page<SecteurSpeu> page = secteurSpeuService.searchSecteursSpeu(numSecteur, nomSecteur, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> downloadSecteurFicheSuivi(Long operationId) throws Exception {
        return downloadDocument(operationService.generateFicheSuivi(operationId));
    }
}

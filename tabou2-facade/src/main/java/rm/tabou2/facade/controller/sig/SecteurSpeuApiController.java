package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.SecteursSpeuApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.service.sig.SecteurSpeuService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

@RestController
public class SecteurSpeuApiController extends AbstractExportDocumentApi implements SecteursSpeuApi {

    @Autowired
    private SecteurSpeuService secteurSpeuService;

    @Override
    public ResponseEntity<PageResult> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, SecteurSpeuEntity.class);

        Page<SecteurSpeu> page = secteurSpeuService.searchSecteursSpeu(numSecteur, nomSecteur, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> downloadSecteurFicheSuivi(Long operationId) throws Exception {
        return downloadDocument(secteurSpeuService.generateFicheSuivi(operationId));
    }
}

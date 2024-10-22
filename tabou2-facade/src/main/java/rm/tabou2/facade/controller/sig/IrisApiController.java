package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.IrisApi;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.sig.IrisService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.IrisEntity;
import rm.tabou2.storage.sig.item.IrisCriteria;

@RestController
public class IrisApiController implements IrisApi {

    @Autowired
    private IrisService irisService;

    @Override
    public ResponseEntity<PageResult> searchIris(String ccom, String codeIris, String nom, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, IrisEntity.class);

        IrisCriteria irisCriteria = new IrisCriteria();
        irisCriteria.setCodeIris(codeIris);
        irisCriteria.setCcom(ccom);
        irisCriteria.setNom(nom);

        Page<Iris> page = irisService.searchIris(irisCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Iris> getIrisById(Integer irisId) throws Exception {
        return new ResponseEntity<>(irisService.getIrisById(irisId), HttpStatus.OK);
    }

}

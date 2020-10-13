package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.IrisApi;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.sig.IrisService;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.IrisEntity;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class IrisApiController implements IrisApi {

    @Autowired
    private IrisService irisService;

    @Override
    public ResponseEntity<PageResult> getIris(@Valid String nom, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, IrisEntity.class);


        //Page<Iris> page = irisService.searchIris(nom, pageable);
        Page<Iris> page = null;

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Iris> getIrisById(Integer irisId) throws Exception {
        return null;
        //return new ResponseEntity<>(irisService.getById(irisId), HttpStatus.OK);
    }

}

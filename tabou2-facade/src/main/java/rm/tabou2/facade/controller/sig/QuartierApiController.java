package rm.tabou2.facade.controller.sig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.QuartiersApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.service.sig.QuartierService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.QuartierEntity;
import rm.tabou2.storage.sig.item.QuartierCriteria;

@RestController
public class QuartierApiController implements QuartiersApi {


    @Autowired
    private QuartierService quartierService;

    @Override
    public ResponseEntity<PageResult> searchQuartiers(Integer codeInsee, Integer nuQuart, String nom, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, QuartierEntity.class);

        QuartierCriteria quartierCriteria = new QuartierCriteria();
        quartierCriteria.setNuQuart(nuQuart);
        quartierCriteria.setCodeInsee(codeInsee);
        quartierCriteria.setNom(nom);

        Page<Quartier> page = quartierService.searchQuartiers(quartierCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Quartier> getQuartierById(Integer quartierId) throws Exception {
        return new ResponseEntity<>(quartierService.getQuartierById(quartierId), HttpStatus.OK);
    }


}

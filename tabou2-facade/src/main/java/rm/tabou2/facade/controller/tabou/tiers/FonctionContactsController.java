package rm.tabou2.facade.controller.tabou.tiers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.tabou.tiers.FonctionContactsService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.tiers.FonctionContactsEntity;
import rm.tabou2.storage.tabou.item.FonctionContactsCriteria;
import rm.tabou2.service.dto.FonctionContacts;
import rm.tabou2.facade.api.FonctionsContactsApi;

@Controller
public class FonctionContactsController implements FonctionsContactsApi {

    @Autowired
    private FonctionContactsService fonctionContactsService;

    @Override
    public ResponseEntity<PageResult> searchFonctionsContacts(String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        FonctionContactsCriteria criteria = new FonctionContactsCriteria();
        criteria.setLibelle(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, FonctionContactsEntity.class);

        Page<FonctionContacts> page = fonctionContactsService.searchFonctionContacts(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

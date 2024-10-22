package rm.tabou2.facade.controller.tabou.financement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypesFinancementsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeFinancement;
import rm.tabou2.service.tabou.financement.TypeFinancementService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.financement.TypeFinancementEntity;
import rm.tabou2.storage.tabou.item.TypeFinancementCriteria;

@RestController
public class TypesFinancementsApiController implements TypesFinancementsApi {

    @Autowired
    TypeFinancementService typeFinancementService;

    @Override
    public ResponseEntity<PageResult> searchTypesFinancements(String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        TypeFinancementCriteria typeFinancementCriteria = new TypeFinancementCriteria();

        typeFinancementCriteria.setLibelle(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeFinancementEntity.class);

        Page<TypeFinancement> page = typeFinancementService.searchTypeFinancement(typeFinancementCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

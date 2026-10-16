package rm.tabou2.facade.controller.tabou.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.RenouvellementsEconomiquesPlaeApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.RenouvellementEconomiquePlae;
import rm.tabou2.service.tabou.operation.RenouvellementEconomiquePlaeService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.RenouvellementEconomiquePlaeEntity;

@RestController
@RequiredArgsConstructor
public class RenouvellementsEconomiquesPlaeApiController implements RenouvellementsEconomiquesPlaeApi {

    private final RenouvellementEconomiquePlaeService renouvellementEconomiquePlaeService;

    @Override
    public ResponseEntity<PageResult> searchRenouvellementsEconomiquesPlae(Boolean actifUniquement, Integer start, Integer resultsNumber,
                                                                        String orderBy, Boolean asc) {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, RenouvellementEconomiquePlaeEntity.class);

        Page<RenouvellementEconomiquePlae> page = renouvellementEconomiquePlaeService.searchRenouvellementsEconomiquesPlae(actifUniquement, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }
}

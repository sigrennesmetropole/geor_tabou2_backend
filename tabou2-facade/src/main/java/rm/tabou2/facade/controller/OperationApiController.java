package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.OperationsApi;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.OperationTiersService;
import rm.tabou2.service.dto.*;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.validation.Valid;
import java.util.Date;


@Controller
public class OperationApiController implements OperationsApi {

    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationTiersService operationTiersService;

    @Override
    public ResponseEntity<Operation> addOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.addOperation(operation), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> editOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.addOperation(operation), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> getOperationById(Long operationId) throws Exception {

        return new ResponseEntity<>(operationService.getOperationById(operationId), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> getOperations(@Valid String nom, @Valid String nature, @Valid String etape, @Valid Boolean diffusionRestreinte, @Valid Boolean estSecteur, @Valid String code, @Valid String numAds, @Valid Date autorisationDateDebut, @Valid Date autorisationDateFin, @Valid Date operationnelDateDebut, @Valid Date operationnelDateFin, @Valid Date clotureDateDebut, @Valid Date clotureDateFin, @Valid String tiers, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        OperationsCriteria operationsCriteria = new OperationsCriteria();

        operationsCriteria.setNom(nom);
        operationsCriteria.setNature(nature);
        operationsCriteria.setEtape(etape);
        operationsCriteria.setCode(code);
        operationsCriteria.setNumAds(numAds);

        operationsCriteria.setDiffusionRestreinte(diffusionRestreinte);
        operationsCriteria.setEstSecteur(estSecteur);

        operationsCriteria.setTiers(tiers);

        operationsCriteria.setAutorisationDateDebut(autorisationDateDebut);
        operationsCriteria.setAutorisationDateFin(autorisationDateFin);

        operationsCriteria.setOperationnelDateDebut(operationnelDateDebut);
        operationsCriteria.setOperationnelDateFin(operationnelDateFin);

        operationsCriteria.setClotureDateDebut(clotureDateDebut);
        operationsCriteria.setClotureDateFin(clotureDateFin);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc);

        Page<Operation> page = operationService.searchOperations(operationsCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }


    @Override
    public ResponseEntity<Operation> associateTiersToOperation(@Valid OperationTiers operationTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.associateTiersToOperation(operationTiers.getOperationId(), operationTiers.getTiersId(), operationTiers.getTypeTiersId()), HttpStatus.OK);
    }

}

package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.OperationsApi;
import rm.tabou2.service.tabou.operation.EvenementOperationService;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.service.dto.*;
import rm.tabou2.service.tabou.programme.EvenementProgrammeService;
import rm.tabou2.service.tabou.tiers.TiersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Controller
public class OperationApiController implements OperationsApi {

    @Autowired
    private OperationService operationService;

    @Autowired
    private TiersService tiersService;

    @Autowired
    private OperationTiersService operationTiersService;

    @Autowired
    private EvenementOperationService evenementOperationService;

    @Autowired
    private EvenementProgrammeService evenementProgrammeService;

    @Override
    public ResponseEntity<Operation> createOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.createOperation(operation), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> deleteEvenementByOperationId(Long evenementId, Long operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteTiersFromOperation(Long operationId, Long associationTiersId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Operation> updateOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.updateOperation(operation), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> updateTiersByOperationId(Long operationId, Long associationTiersId, @Valid TiersTypeTiers associationTiers) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Emprise>> getAvailableEmprises(@NotNull @Valid String nature, @NotNull @Valid Boolean estSecteur) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesByOperationId(Long operationId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Operation> getOperationById(Long operationId) throws Exception {

        return new ResponseEntity<>(operationService.getOperationById(operationId), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<AssociationTiersTypeTiers>> getTiersByOperationId(Long operationId) throws Exception {
        return null;
        //return new ResponseEntity<>(tiersService.getTiersByOperationId(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchOperations(@Valid String nom, @Valid String nature, @Valid String etape, @Valid Boolean diffusionRestreinte, @Valid Boolean estSecteur, @Valid String code, @Valid String numAds, @Valid Date autorisationDateDebut, @Valid Date autorisationDateFin, @Valid Date operationnelDateDebut, @Valid Date operationnelDateFin, @Valid Date clotureDateDebut, @Valid Date clotureDateFin, @Valid String tiers, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

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

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, OperationEntity.class);

        Page<Operation> page = operationService.searchOperations(operationsCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> updateEtapeByOperationId(Long operationId, @NotNull @Valid Long etapeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<Evenement>> getEvenementsByOperationId(Long operationId) throws Exception {
        return new ResponseEntity<>(evenementOperationService.getByOperationId(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByOperationId(@Valid Evenement evenement, Long operationId) throws Exception {
        return new ResponseEntity<>(evenementOperationService.addEvenement(evenement, operationId), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> associateTiersToOperation(Long operationId, @Valid TiersTypeTiers tiersTypeTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.associateTiersToOperation(operationId, tiersTypeTiers.getTiersId(), tiersTypeTiers.getTypeTiersId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> updateEvenementByOperationId(@Valid Evenement evenement, Long operationId) throws Exception {

        //TODO : test des droits
        return new ResponseEntity<>(evenementOperationService.updateEvenementByOperationId(evenement, operationId), HttpStatus.OK);
    }


}
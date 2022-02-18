package rm.tabou2.facade.controller.tabou.operation.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.v1.OperationsApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.OperationV1;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.mapper.tabou.operation.OperationV1Mapper;
import rm.tabou2.service.tabou.evenement.EvenementOperationService;
import rm.tabou2.service.tabou.operation.EtapeOperationService;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Controller
public class OperationV1ApiController extends AbstractExportDocumentApi implements OperationsApi {

    @Autowired
    private OperationService operationService;

    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private OperationTiersService operationTiersService;

    @Autowired
    private EtapeOperationService etapeOperationService;

    @Autowired
    private EvenementOperationService evenementOperationService;

    @Autowired
    private OperationEmpriseHelper operationEmpriseHelper;

    @Autowired
    private OperationV1Mapper mapper;

    @Override
    public ResponseEntity<OperationV1> createOperation(@Valid OperationV1 operation) throws Exception {

        return new ResponseEntity<>(mapper.entityToDto(operationService.createOperation(mapper.dtoToEntity(operation))), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<OperationV1> deleteEvenementByOperationId(Long evenementId, Long operationId) throws Exception {
        evenementOperationService.deleteEvenementByOperationId(evenementId, operationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OperationV1> updateOperation(@Valid OperationV1 operation) throws Exception {

        return new ResponseEntity<>(mapper.entityToDto(operationService.updateOperation(mapper.dtoToEntity(operation))), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<OperationV1> getOperationById(Long operationId) throws Exception {

        return new ResponseEntity<>(mapper.entityToDto(operationService.getOperationById(operationId)), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchOperations(@Valid String nom, @Valid String nature, @Valid String vocation, @Valid String decision,
                                                       @Valid String modeAmenagement, @Valid String maitriseOuvrage, @Valid String consommationEspace,
                                                       @Valid String etape, @Valid Boolean diffusionRestreinte, @Valid Boolean estSecteur,
                                                       @Valid String code, @Valid String numAds, @Valid Date autorisationDateDebut,
                                                       @Valid Date autorisationDateFin, @Valid Date operationnelDateDebut, @Valid Date operationnelDateFin,
                                                       @Valid Date clotureDateDebut, @Valid Date clotureDateFin, @Valid String tiers, @Valid Integer start,
                                                       @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        OperationsCriteria operationsCriteria = new OperationsCriteria();

        operationsCriteria.setNom(nom);
        operationsCriteria.setNature(nature);
        operationsCriteria.setDecision(decision);
        operationsCriteria.setVocation(vocation);
        operationsCriteria.setMaitriseOuvrage(maitriseOuvrage);
        operationsCriteria.setConsommationEspace(consommationEspace);
        operationsCriteria.setModeAmenagement(modeAmenagement);
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

        Page<OperationV1> page = mapper.entitiesToDto(operationService.searchOperations(operationsCriteria, pageable), pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<OperationV1> updateEtapeByOperationId(Long operationId, @NotNull @Valid Long etapeId) throws Exception {
        return new ResponseEntity<>(mapper.entityToDto(operationService.updateEtapeOfOperationId(operationId, etapeId)), HttpStatus.OK);
    }

}

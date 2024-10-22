package rm.tabou2.facade.controller.tabou.operation.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.v2.OperationsApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.mapper.tabou.operation.OperationV2Mapper;
import rm.tabou2.service.tabou.evenement.EvenementOperationService;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.Date;

@RestController
public class OperationV2ApiController extends AbstractExportDocumentApi implements OperationsApi {

    @Autowired
    private OperationService operationService;

    @Autowired
    private EvenementOperationService evenementOperationService;

    @Autowired
    private OperationV2Mapper mapper;

    @Override
    public ResponseEntity<Operation> createOperation(Operation operation) throws Exception {

        return new ResponseEntity<>(mapper.entityToDto(operationService.createOperation(mapper.dtoToEntity(operation))), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> deleteEvenementByOperationId(Long evenementId, Long operationId) throws Exception {
        evenementOperationService.deleteEvenementByOperationId(evenementId, operationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Operation> updateOperation(Operation operation) throws Exception {

        return new ResponseEntity<>(mapper.entityToDto(operationService.updateOperation(mapper.dtoToEntity(operation))), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> getOperationById(Long operationId) throws Exception {

        return new ResponseEntity<>(mapper.entityToDto(operationService.getOperationById(operationId)), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchOperations(String nom, String nature, String vocation, String decision,
                                                       String modeAmenagement, String maitriseOuvrage, String consommationEspace,
                                                       String etape, Boolean diffusionRestreinte, Boolean estSecteur,
                                                       String code, String numAds, Date autorisationDateDebut,
                                                       Date autorisationDateFin, Date operationnelDateDebut, Date operationnelDateFin,
                                                       Date livraisonDateDebut, Date livraisonDateFin,
                                                       Date clotureDateDebut, Date clotureDateFin, String tiers, Integer start,
                                                       Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

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

        operationsCriteria.setLivraisonDateDebut(livraisonDateDebut);
        operationsCriteria.setLivraisonDateFin(livraisonDateFin);

        operationsCriteria.setClotureDateDebut(clotureDateDebut);
        operationsCriteria.setClotureDateFin(clotureDateFin);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, OperationEntity.class);

        Page<Operation> page = mapper.entitiesToDto(operationService.searchOperations(operationsCriteria, pageable), pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Operation> updateEtapeByOperationId(Long operationId, Long etapeId) throws Exception {
        return new ResponseEntity<>(mapper.entityToDto(operationService.updateEtapeOfOperationId(operationId, etapeId)), HttpStatus.OK);
    }
}

package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.facade.api.OperationsApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.ProgrammeLight;
import rm.tabou2.service.dto.TiersTypeTiers;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.service.tabou.evenement.EvenementOperationService;
import rm.tabou2.service.tabou.operation.EtapeOperationService;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.service.tabou.operation.OperationTiersService;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;
import rm.tabou2.storage.tabou.item.OperationsCriteria;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Controller
public class OperationApiController extends AbstractExportDocumentApi implements OperationsApi {

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

    @Override
    public ResponseEntity<Operation> createOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.createOperation(operation), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> deleteEvenementByOperationId(Long evenementId, Long operationId) throws Exception {
        evenementOperationService.deleteEvenementByOperationId(evenementId, operationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteTiersFromOperation(Long operationId, Long associationTiersId) throws Exception {
        operationTiersService.deleteTiersByOperationId(operationId, associationTiersId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> updateOperation(@Valid Operation operation) throws Exception {

        return new ResponseEntity<>(operationService.updateOperation(operation), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> updateTiersByOperationId(Long operationId, Long associationTiersId, @Valid TiersTypeTiers associationTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.updateTiersAssociation(operationId, associationTiersId, associationTiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getAvailableEmprises(@NotNull @Valid Long natureId, @NotNull @Valid Boolean estSecteur, @Valid String nom, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, OperationTiersEntity.class);

        Page<Emprise> page = operationEmpriseHelper.getAvailableEmprises(natureId, estSecteur, pageable, nom);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesByOperationId(Long operationId) throws Exception {
        return new ResponseEntity<>(etapeOperationService.getEtapesForOperationById(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Operation> getOperationById(Long operationId) throws Exception {

        return new ResponseEntity<>(operationService.getOperationById(operationId), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchTiersByOperationId(Long operationId, @Valid String libelle, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        TiersAmenagementCriteria criteria = new TiersAmenagementCriteria();
        criteria.setAsc(asc);
        criteria.setOrderBy(orderBy);
        criteria.setLibelle(libelle);
        criteria.setOperationId(operationId);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, criteria.getOrderBy(), criteria.isAsc(), OperationTiersEntity.class);

        Page<AssociationTiersTypeTiers> page = operationTiersService.searchOperationTiers(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

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

        Page<Operation> page = operationService.searchOperations(operationsCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchOperationsEtapes(@Valid String code, @Valid String libelle, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        EtapeCriteria etapeCriteria = new EtapeCriteria();

        etapeCriteria.setCode(code);
        etapeCriteria.setLibelle(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EtapeOperationEntity.class);

        Page<EtapeRestricted> page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchProgrammesOfOperation(Long operationId, @Valid String nom, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        ProgrammeCriteria programmeCriteria = new ProgrammeCriteria();
        programmeCriteria.setOperationId(operationId);
        programmeCriteria.setNom(nom);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ProgrammeRmEntity.class);

        Page<ProgrammeLight> page = programmeService.searchProgrammesOfOperation(programmeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Operation> updateEtapeByOperationId(Long operationId, @NotNull @Valid Long etapeId) throws Exception {
        return new ResponseEntity<>(operationService.updateEtapeOfOperationId(operationId, etapeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getEvenementsByOperationId(Long operationId, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EvenementOperationEntity.class);

        Page<Evenement> page = evenementOperationService.searchEvenementOperation(operationId, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<DocumentMetadata> addDocument(@NotNull @Valid Long operationId, @NotNull @Valid String nom, @NotNull @Valid String libelle, @Valid MultipartFile fileToUpload) throws Exception {
        return new ResponseEntity<>(operationService.addDocument(operationId, nom, libelle, fileToUpload), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByOperationId(@Valid Evenement evenement, Long operationId) throws Exception {
        return new ResponseEntity<>(operationService.addEvenementByOperationId(operationId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> updateEvenementByOperationId(@Valid Evenement evenement, Long operationId) throws Exception {
        return new ResponseEntity<>(operationService.updateEvenementByOperationId(operationId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> associateTiersToOperation(Long operationId, @Valid TiersTypeTiers tiersTypeTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.associateTiersToOperation(operationId, tiersTypeTiers.getTiersId(), tiersTypeTiers.getTypeTiersId()), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<DocumentMetadata> getDocumentMetadata(Long operationId, String documentId) throws Exception {
        return new ResponseEntity<>(operationService.getDocumentMetadata(operationId, documentId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> updateDocumentMetadata(Long operationId, String documentId, @Valid DocumentMetadata documentMetadata) throws Exception {
        return new ResponseEntity<>(operationService.updateDocumentMetadata(operationId, documentId, documentMetadata), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> getDocumentContent(Long operationId, String documentId) throws Exception {
        return downloadDocument(operationService.downloadDocument(operationId, documentId));
    }

    @Override
    public ResponseEntity<Void> deleteDocument(Long operationId, String documentId) throws Exception {
        operationService.deleteDocument(operationId, documentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> updateDocumentContent(Long operationId, String documentId, @Valid MultipartFile fileToUpload) throws Exception {

        operationService.updateDocumentContent(operationId, documentId, fileToUpload);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchDocuments(Long operationId, String nom, String libelle, String typeMime, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageableForAlfresco(start, resultsNumber, orderBy, asc);

        Page<rm.tabou2.service.dto.DocumentMetadata> page = operationService.searchDocuments(operationId, nom, libelle, typeMime, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }


}

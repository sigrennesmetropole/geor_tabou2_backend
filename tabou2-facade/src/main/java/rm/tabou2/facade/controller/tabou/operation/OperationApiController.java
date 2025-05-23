package rm.tabou2.facade.controller.tabou.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.facade.api.OperationsApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.TiersTypeTiers;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.dto.ProgrammeLight;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.DocumentMetadata;
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
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

import java.util.Date;
import java.util.List;


@RestController
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
    public ResponseEntity<Void> deleteTiersFromOperation(Long operationId, Long associationTiersId) throws Exception {
        operationTiersService.deleteTiersByOperationId(operationId, associationTiersId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> updateTiersByOperationId(Long operationId, Long associationTiersId, TiersTypeTiers associationTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.updateTiersAssociation(operationId, associationTiersId, associationTiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getAvailableOperationEmprises(Long natureId, Boolean estSecteur, String nom, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, OperationTiersEntity.class);

        Page<Emprise> page = operationEmpriseHelper.getAvailableEmprises(natureId, estSecteur, pageable, nom);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesByOperationId(Long operationId) throws Exception {
        return new ResponseEntity<>(etapeOperationService.getEtapesForOperationById(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTiersByOperationId(Long operationId, String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

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
    public ResponseEntity<PageResult> searchOperationsEtapes(String code, String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        EtapeCriteria etapeCriteria = new EtapeCriteria();

        etapeCriteria.setCode(code);
        etapeCriteria.setLibelle(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EtapeOperationEntity.class);

        Page<EtapeRestricted> page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchProgrammesOfOperation(Long operationId, String nom, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        ProgrammeCriteria programmeCriteria = new ProgrammeCriteria();
        programmeCriteria.setOperationId(operationId);
        programmeCriteria.setNom(nom);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ProgrammeRmEntity.class);

        Page<ProgrammeLight> page = programmeService.searchProgrammesOfOperation(programmeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> getEvenementsByOperationId(Long operationId, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EvenementOperationEntity.class);

        Page<Evenement> page = evenementOperationService.searchEvenementsOperations(operationId, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<DocumentMetadata> addOperationDocument(Long operationId, String nom, String libelle, Date dateDocument, MultipartFile fileToUpload) throws Exception {
        return new ResponseEntity<>(operationService.addDocument(operationId, nom, libelle, dateDocument , fileToUpload), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByOperationId(Long operationId, Evenement evenement) throws Exception {
        return new ResponseEntity<>(operationService.addEvenementByOperationId(operationId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> updateEvenementByOperationId(Long operationId, Evenement evenement) throws Exception {
        return new ResponseEntity<>(operationService.updateEvenementByOperationId(operationId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> associateTiersToOperation(Long operationId, TiersTypeTiers tiersTypeTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.associateTiersToOperation(operationId, tiersTypeTiers.getTiersId(), tiersTypeTiers.getTypeTiersId()), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<DocumentMetadata> getOperationDocumentMetadata(Long operationId, String documentId) throws Exception {
        return new ResponseEntity<>(operationService.getDocumentMetadata(operationId, documentId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> updateOperationDocumentMetadata(Long operationId, String documentId, DocumentMetadata documentMetadata) throws Exception {
        return new ResponseEntity<>(operationService.updateDocumentMetadata(operationId, documentId, documentMetadata), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> getOperationDocumentContent(Long operationId, String documentId) throws Exception {
        return downloadDocument(operationService.downloadDocument(operationId, documentId));
    }

    @Override
    public ResponseEntity<Void> deleteOperationDocument(Long operationId, String documentId) throws Exception {
        operationService.deleteDocument(operationId, documentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> updateOperationDocumentContent(Long operationId, String documentId, MultipartFile fileToUpload) throws Exception {

        operationService.updateDocumentContent(operationId, documentId, fileToUpload);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchOperationDocuments(Long operationId, String nom, String libelleTypeDocument, String typeMime, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageableForAlfresco(start, resultsNumber, orderBy, asc);

        Page<rm.tabou2.service.dto.DocumentMetadata> page = operationService.searchDocuments(operationId, nom, libelleTypeDocument, typeMime, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> downloadOperationFicheSuivi(Long operationId) throws Exception {
        return downloadDocument(operationService.generateFicheSuivi(operationId));
    }

}

package rm.tabou2.facade.controller.tabou.operation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.facade.api.OperationsApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.*;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.service.mapper.tabou.operation.OperationDtoMapper;
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

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OperationApiController extends AbstractExportDocumentApi implements OperationsApi {

    private final OperationService operationService;

    private final ProgrammeService programmeService;

    private final OperationTiersService operationTiersService;

    private final EtapeOperationService etapeOperationService;

    private final EvenementOperationService evenementOperationService;

    private final OperationEmpriseHelper operationEmpriseHelper;

    private final DateHelper dateHelper;

    private final OperationDtoMapper operationDtoMapper;

	@Override
	public ResponseEntity<Void> deleteTiersFromOperation(Long operationId, Long associationTiersId) throws Exception {
		operationTiersService.deleteTiersByOperationId(operationId, associationTiersId);
		return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> updateTiersByOperationId(Long operationId, Long associationTiersId,
                                                                              TiersTypeTiers associationTiers) throws Exception {
        return new ResponseEntity<>(
                operationTiersService.updateTiersAssociation(operationId, associationTiersId, associationTiers),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getAvailableOperationEmprises(Long natureId, Boolean estSecteur, String nom,
                                                                    Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc,
                OperationTiersEntity.class);

        Page<Emprise> page = operationEmpriseHelper.getAvailableEmprises(natureId, estSecteur, pageable, nom);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesByOperationId(Long operationId) {
        return new ResponseEntity<>(etapeOperationService.getEtapesForOperationById(operationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTiersByOperationId(Long operationId, String libelle, Integer start,
                                                               Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        TiersAmenagementCriteria criteria = new TiersAmenagementCriteria();
        criteria.setAsc(asc);
        criteria.setOrderBy(orderBy);
        criteria.setLibelle(libelle);
        criteria.setOperationId(operationId);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, criteria.getOrderBy(), criteria.isAsc(),
                OperationTiersEntity.class);

        Page<AssociationTiersTypeTiers> page = operationTiersService.searchOperationTiers(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchOperationsEtapes(String code, String libelle, String type, Boolean secteur,
                                                             Integer start, Integer resultsNumber, String orderBy,
                                                             Boolean asc) {

        EtapeCriteria etapeCriteria = new EtapeCriteria();

        etapeCriteria.setCode(code);
        etapeCriteria.setLibelle(libelle);
        etapeCriteria.setType(type);
        etapeCriteria.setSecteur(secteur);

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "order_";
        }
        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc,
                EtapeOperationEntity.class);

        Page<EtapeRestricted> page = etapeOperationService.searchEtapesOperation(etapeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchProgrammesOfOperation(Long operationId, String nom, Integer start,
                                                                  Integer resultsNumber, String orderBy, Boolean asc) {

        ProgrammeCriteria programmeCriteria = new ProgrammeCriteria();
        programmeCriteria.setOperationId(operationId);
        programmeCriteria.setNom(nom);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ProgrammeRmEntity.class);

        Page<ProgrammeLight> page = programmeService.searchProgrammesOfOperation(programmeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> getEvenementsByOperationId(Long operationId, Integer start, Integer resultsNumber,
                                                                 String orderBy, Boolean asc) {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc,
                EvenementOperationEntity.class);

        Page<Evenement> page = evenementOperationService.searchEvenementsOperations(operationId, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<DocumentMetadata> addOperationDocument(Long operationId, String nom, String libelle,
                                                                 OffsetDateTime dateDocument, MultipartFile fileToUpload) throws Exception {
        return new ResponseEntity<>(
                operationService.addDocument(operationId, nom, libelle, dateHelper.convert(dateDocument), fileToUpload),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByOperationId(Long operationId, Evenement evenement) throws Exception {
        return new ResponseEntity<>(operationService.addEvenementByOperationId(operationId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> updateEvenementByOperationId(Long operationId, Evenement evenement)
            throws Exception {
        return new ResponseEntity<>(operationService.updateEvenementByOperationId(operationId, evenement),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> associateTiersToOperation(Long operationId,
                                                                               TiersTypeTiers tiersTypeTiers) throws Exception {
        return new ResponseEntity<>(operationTiersService.associateTiersToOperation(operationId,
                tiersTypeTiers.getTiersId(), tiersTypeTiers.getTypeTiersId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> getOperationDocumentMetadata(Long operationId, String documentId)
            throws Exception {
        return new ResponseEntity<>(operationService.getDocumentMetadata(operationId, documentId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> updateOperationDocumentMetadata(Long operationId, String documentId,
                                                                            DocumentMetadata documentMetadata) throws Exception {
        return new ResponseEntity<>(operationService.updateDocumentMetadata(operationId, documentId, documentMetadata),
                HttpStatus.OK);
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
    public ResponseEntity<DocumentMetadata> updateOperationDocumentContent(Long operationId, String documentId,
                                                                           MultipartFile fileToUpload) throws Exception {

        operationService.updateDocumentContent(operationId, documentId, fileToUpload);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchOperationDocuments(Long operationId, String nom, String libelleTypeDocument,
                                                               String typeMime, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        Pageable pageable = PaginationUtils.buildPageableForAlfresco(start, resultsNumber, orderBy, asc);

        Page<rm.tabou2.service.dto.DocumentMetadata> page = operationService.searchDocuments(operationId, nom,
                libelleTypeDocument, typeMime, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> downloadOperationFicheSuivi(Long operationId) throws Exception {
        return downloadDocument(operationService.generateFicheSuivi(operationId));
    }

    @Override
    public ResponseEntity<TypePLH> updatePLHOperation(Long operationId, Long typePLHId, TypePLH typePLH) throws Exception {
        return new ResponseEntity<>(operationService.updatePLHOperation(operationId, typePLHId, typePLH), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypePLH> getPLHOperation(Long operationId, Long typePLHId) throws Exception {
        return new ResponseEntity<>(operationService.getPLHOperation(operationId, typePLHId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> removePLHOperationById(Long operationId, Long typePLHId) throws Exception {
        operationService.removePLHOperationById(operationId, typePLHId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypePLH> addPLHOperation(Long operationId, Long typePLHId) throws Exception {
        return ResponseEntity.ok(operationService.addPLHOperationById(operationId, typePLHId));
    }

    @Override
    public ResponseEntity<List<TypePLHBean>> getPLHsOperation(Long operationId) throws Exception {
        return ResponseEntity.ok(operationService.getPLHsOperation(operationId));
    }

	@Override
	public ResponseEntity<Operation> createOperation(Operation operation) throws Exception {
		return new ResponseEntity<>(
				operationDtoMapper.entityToDto(operationService.createOperation(operationDtoMapper.dtoToEntity(operation))),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Operation> updateOperation(Operation operation) throws Exception {
		return new ResponseEntity<>(
				operationDtoMapper.entityToDto(operationService.updateOperation(operationDtoMapper.dtoToEntity(operation))),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Operation> getOperationById(Long operationId) throws Exception {
		return new ResponseEntity<>(operationDtoMapper.entityToDto(operationService.getOperationById(operationId)),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PageResult> searchOperations(String nom, String nature, String vocation, String decision,
			String modeAmenagement, String maitriseOuvrage, String consommationEspace, String etape,
			Boolean diffusionRestreinte, Boolean estSecteur, String code, String numAds,
			OffsetDateTime autorisationDateDebut, OffsetDateTime autorisationDateFin,
			OffsetDateTime operationnelDateDebut, OffsetDateTime operationnelDateFin, OffsetDateTime livraisonDateDebut,
			OffsetDateTime livraisonDateFin, OffsetDateTime clotureDateDebut, OffsetDateTime clotureDateFin,
			String tiers, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

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

		operationsCriteria.setAutorisationDateDebut(dateHelper.convert(autorisationDateDebut));
		operationsCriteria.setAutorisationDateFin(dateHelper.convert(autorisationDateFin));

		operationsCriteria.setOperationnelDateDebut(dateHelper.convert(operationnelDateDebut));
		operationsCriteria.setOperationnelDateFin(dateHelper.convert(operationnelDateFin));

		operationsCriteria.setLivraisonDateDebut(dateHelper.convert(livraisonDateDebut));
		operationsCriteria.setLivraisonDateFin(dateHelper.convert(livraisonDateFin));

		operationsCriteria.setClotureDateDebut(dateHelper.convert(clotureDateDebut));
		operationsCriteria.setClotureDateFin(dateHelper.convert(clotureDateFin));

		Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, OperationEntity.class);

		Page<Operation> page = operationDtoMapper.entitiesToDto(
				operationService.searchOperations(operationsCriteria, pageable), pageable);

		return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Operation> updateEtapeByOperationId(Long operationId, Long etapeId) throws Exception {
		return new ResponseEntity<>(
				operationDtoMapper.entityToDto(operationService.updateEtapeOfOperationId(operationId, etapeId)),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Operation> deleteEvenementByOperationId(Long evenementId, Long operationId) throws Exception {
		evenementOperationService.deleteEvenementByOperationId(evenementId, operationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}

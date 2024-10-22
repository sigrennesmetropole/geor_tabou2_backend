package rm.tabou2.facade.controller.tabou.programme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rm.tabou2.facade.api.ProgrammesApi;
import rm.tabou2.facade.controller.common.AbstractExportDocumentApi;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.TiersTypeTiers;
import rm.tabou2.service.tabou.agaepo.AgapeoService;
import rm.tabou2.service.tabou.ddc.PermisConstruireService;
import rm.tabou2.service.tabou.evenement.EvenementProgrammeService;
import rm.tabou2.service.tabou.programme.EtapeProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeTiersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

import java.util.Date;
import java.util.List;

@RestController
public class ProgrammeApiController extends AbstractExportDocumentApi implements ProgrammesApi {

    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private ProgrammeTiersService programmeTiersService;

    @Autowired
    private EtapeProgrammeService etapeProgrammeService;

    @Autowired
    private EvenementProgrammeService evenementProgrammeService;

    @Autowired
    private PermisConstruireService permisConstruireService;

    @Autowired
    private AgapeoService agapeoService;

    @Override
    public ResponseEntity<Programme> createProgramme(Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.createProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> updateProgramme(Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.updateProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> updateTiersByProgrammeId(Long programmeId, Long associationTiersId, TiersTypeTiers associationTiers) throws Exception {
        return new ResponseEntity<>(programmeTiersService.updateTiersAssociation(programmeId, associationTiersId, associationTiers), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchProgrammes(String nom, String etape, Boolean diffusionRestreinte,
                                                       String code, String numAds, String nomOperation,
                                                       String natureOperation, Date clotureDateDebut, Date clotureDateFin,
                                                       String tiers, Integer attributionFonciereAnneeDebut, Integer attributionFonciereAnneeFin,
                                                       Date attributionDateDebut, Date attributionDateFin, Date commercialisationDateDebut,
                                                       Date commercialisationDateFin, Date adsDateDebut, Date adsDateFin,
                                                       Date livraisonDateDebut, Date livraisonDateFin, Date annulationDate,
                                                       Date docDateDebut, Date docDateFin, Date datDateDebut,
                                                       Date datDateFin, Boolean logementsAides, Integer start,
                                                       Boolean onlyActive, Integer resultsNumber,
                                                       String orderBy, Boolean asc) throws Exception {

        ProgrammeCriteria programmeCriteria = new ProgrammeCriteria();

        programmeCriteria.setEtape(etape);
        programmeCriteria.setDiffusionRestreinte(diffusionRestreinte);
        programmeCriteria.setCode(code);
        programmeCriteria.setNom(nom);
        programmeCriteria.setClotureDateDebut(clotureDateDebut);
        programmeCriteria.setClotureDateFin(clotureDateFin);
        programmeCriteria.setNumAds(numAds);
        programmeCriteria.setNomOperation(nomOperation);
        programmeCriteria.setNatureOperation(natureOperation);
        programmeCriteria.setTiers(tiers);
        programmeCriteria.setAttributionFonciereAnneeDebut(attributionFonciereAnneeDebut);
        programmeCriteria.setAttributionFonciereAnneeFin(attributionFonciereAnneeFin);
        programmeCriteria.setAttributionDateDebut(attributionDateDebut);
        programmeCriteria.setAttributionDateFin(attributionDateFin);
        programmeCriteria.setCommercialisationDateDebut(commercialisationDateDebut);
        programmeCriteria.setCommercialisationDateFin(commercialisationDateFin);
        programmeCriteria.setAdsDateDebut(adsDateDebut);
        programmeCriteria.setAdsDateFin(adsDateFin);
        programmeCriteria.setLivraisonDateDebut(livraisonDateDebut);
        programmeCriteria.setLivraisonDateFin(livraisonDateFin);
        programmeCriteria.setAnnulationDate(annulationDate);
        programmeCriteria.setDocDateDebut(docDateDebut);
        programmeCriteria.setDocDateFin(docDateFin);
        programmeCriteria.setDatDateDebut(datDateDebut);
        programmeCriteria.setDatDateFin(datDateFin);
        programmeCriteria.setLogementsAides(logementsAides);


        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ProgrammeEntity.class);

        Page<Programme> page = programmeService.searchProgrammes(programmeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchProgrammesEtapes(String code, String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
        EtapeCriteria etapeCriteria = new EtapeCriteria();

        etapeCriteria.setCode(code);
        etapeCriteria.setLibelle(libelle);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EtapeProgrammeEntity.class);

        Page<EtapeRestricted> page = etapeProgrammeService.searchEtapesProgramme(etapeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> getProgrammeById(Long programmeId) throws Exception {
        return new ResponseEntity<>(programmeService.getProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesForProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(etapeProgrammeService.getEtapesForProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getAgapeoByProgrammeId(Long programmeId, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, AgapeoEntity.class);

        Page<Agapeo> page = agapeoService.getApapeosByProgrammeId(programmeId, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getAvailableProgrammeEmprises(String nom, Long operationId, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, ProgrammeRmEntity.class);

        Page<Emprise> page = programmeService.getEmprisesAvailables(nom, operationId, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> getPermisByProgrammeId(Long programmeId, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, PermisConstruireEntity.class);

        Page<PermisConstruire> page = permisConstruireService.getPermisConstruiresByProgrammeId(programmeId, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchTiersByProgrammeId(Long programmeId, String libelle, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        TiersAmenagementCriteria criteria = new TiersAmenagementCriteria();
        criteria.setAsc(asc);
        criteria.setOrderBy(orderBy);
        criteria.setLibelle(libelle);
        criteria.setProgrammeId(programmeId);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, criteria.getOrderBy(), criteria.isAsc(), OperationTiersEntity.class);

        Page<AssociationTiersTypeTiers> page = programmeTiersService.searchProgrammeTiers(criteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }



    @Override
    public ResponseEntity<Evenement> updateEvenementByProgrammeId(Long programmeId, Evenement evenement) throws Exception {
        return new ResponseEntity<>(programmeService.updateEvenementByProgrammeId(programmeId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getEvenementsByProgrammeId(Long programmeId, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, EvenementProgrammeEntity.class);

        Page<Evenement> page = evenementProgrammeService.searchEvenementsProgramme(programmeId, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByProgrammeId(Long programmeId, Evenement evenement) throws Exception {
        return new ResponseEntity<>(programmeService.addEvenementByProgrammeId(programmeId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> deleteEvenementByProgrammeId(Long evenementId, Long programmeId) throws Exception {
        evenementProgrammeService.deleteEvenementByProgrammeId(evenementId, programmeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteTiersFromProgramme(Long programmeId, Long associationTiersId) throws Exception {
        programmeTiersService.deleteTiersByProgrammeId(programmeId, associationTiersId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> downloadProgrammeFicheSuivi(Long programmeId) throws Exception {
        return downloadDocument(programmeService.generateFicheSuivi(programmeId));
    }

    @Override
    public ResponseEntity<Programme> updateEtapeOfProgrammeId(Long programmeId, Long etapeId) throws Exception {
        return new ResponseEntity<>(programmeService.updateEtapeOfProgrammeId(programmeId, etapeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssociationTiersTypeTiers> associateTiersToProgramme(Long programmeId, TiersTypeTiers tiersTypeTiers) throws Exception {
        return new ResponseEntity<>(programmeTiersService.associateTiersToProgramme(programmeId, tiersTypeTiers.getTiersId(), tiersTypeTiers.getTypeTiersId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> getProgrammeDocumentMetadata(Long programmeId, String documentId) throws Exception {
        return new ResponseEntity<>(programmeService.getDocumentMetadata(programmeId, documentId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> getProgrammeDocumentContent(Long programmeId, String documentId) throws Exception {
        return downloadDocument(programmeService.downloadDocument(programmeId, documentId));
    }

    @Override
    public ResponseEntity<Void> deleteProgrammeDocument(Long programmeId, String documentId) throws Exception {
        programmeService.deleteDocument(programmeId, documentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> updateProgrammeDocumentMetadata(Long programmeId, String documentId, DocumentMetadata documentMetadata) throws Exception {
        return new ResponseEntity<>(programmeService.updateDocumentMetadata(programmeId, documentId, documentMetadata), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> updateProgrammeDocumentContent(Long programmeId, String documentId, MultipartFile fileToUpload) throws Exception {
        programmeService.updateDocumentContent(programmeId, documentId, fileToUpload);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentMetadata> addProgrammeDocument(Long programmeId, String nom, String libelleTypeDocument, Date dateDocument, MultipartFile fileToUpload) throws Exception {
        return new ResponseEntity<>(programmeService.addDocument(programmeId, nom, libelleTypeDocument, fileToUpload, dateDocument), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PageResult> searchProgrammeDocuments(Long programmeId, String nom, String libelleTypeDocument, String typeMime, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        Pageable pageable = PaginationUtils.buildPageableForAlfresco(start, resultsNumber, orderBy, asc);

        Page<DocumentMetadata> page = programmeService.searchDocuments(programmeId, nom, libelleTypeDocument, typeMime, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }


}

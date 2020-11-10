package rm.tabou2.facade.controller.tabou.programme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ProgrammesApi;
import rm.tabou2.service.ddc.PermisConstruireService;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.tabou.agaepo.AgapeoService;
import rm.tabou2.service.tabou.programme.EtapeProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeService;
import rm.tabou2.service.tabou.programme.ProgrammeTiersService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Controller
public class ProgrammeApiController implements ProgrammesApi {

    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private ProgrammeTiersService programmeTiersService;

    @Autowired
    private EtapeProgrammeService etapeProgrammeService;

    @Autowired
    private PermisConstruireService permisConstruireService;

    @Autowired
    private AgapeoService agapeoService;

    @Override
    public ResponseEntity<Programme> createProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.createProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> updateProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.updateProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchProgrammes(@Valid String nom, @Valid String etape, @Valid Boolean diffusionRestreinte,
                                                       @Valid String code, @Valid String numAds, @Valid Date clotureDateDebut,
                                                       @Valid Date clotureDateFin, @Valid String tiers, @Valid Integer attributionFonciereAnneeDebut,
                                                       @Valid Integer attributionFonciereAnneeFin, @Valid Date attributionDateDebut, @Valid Date attributionDateFin,
                                                       @Valid Date commercialisationDateDebut, @Valid Date commercialisationDateFin, @Valid Date adsDateDebut,
                                                       @Valid Date adsDateFin, @Valid Date docDateDebut, @Valid Date docDateFin,
                                                       @Valid Date datDateDebut, @Valid Date datDateFin, @Valid Boolean logementsAides,
                                                       @Valid Integer start, @Valid Boolean onlyActive, @Valid Integer resultsNumber,
                                                       @Valid String orderBy, @Valid Boolean asc) throws Exception {

        ProgrammeCriteria programmeCriteria = new ProgrammeCriteria();

        programmeCriteria.setEtape(etape);
        programmeCriteria.setDiffusionRestreinte(diffusionRestreinte);
        programmeCriteria.setCode(code);
        programmeCriteria.setNom(nom);
        programmeCriteria.setClotureDateDebut(clotureDateDebut);
        programmeCriteria.setClotureDateFin(clotureDateFin);
        programmeCriteria.setNumAds(numAds);
        programmeCriteria.setTiers(tiers);
        programmeCriteria.setAttributionFonciereAnneeDebut(attributionFonciereAnneeDebut);
        programmeCriteria.setAttributionFonciereAnneeFin(attributionFonciereAnneeFin);
        programmeCriteria.setAttributionDateDebut(attributionDateDebut);
        programmeCriteria.setAttributionDateFin(attributionDateFin);
        programmeCriteria.setCommercialisationDateDebut(commercialisationDateDebut);
        programmeCriteria.setCommercialisationDateFin(commercialisationDateFin);
        programmeCriteria.setAdsDateDebut(adsDateDebut);
        programmeCriteria.setAdsDateFin(adsDateFin);
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
    public ResponseEntity<Programme> getProgrammeById(Long programmeId) throws Exception {
        return new ResponseEntity<>(programmeService.getProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Etape>> getEtapesForProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(etapeProgrammeService.getEtapesForProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Agapeo>> getAgapeoByProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(agapeoService.getApapeosByProgrammeId(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PermisConstruire>> getPermisByProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(permisConstruireService.getPermisConstruiresByProgrammeId(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AssociationTiersTypeTiers>> getTiersByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Evenement> updateEvenementByProgrammeId(@Valid Evenement evenement, Long programmeId) throws Exception {
        return new ResponseEntity<>(programmeService.updateEvenementByProgrammeId(programmeId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Evenement>> getEvenementsByProgrammeId(Long programmeId) throws Exception {
        return new ResponseEntity<>(programmeService.getEvenementsByProgrammeId( programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Evenement> addEvenementByProgrammeId(@Valid Evenement evenement, Long programmeId) throws Exception {
        return new ResponseEntity<>(programmeService.addEvenementByProgrammeId(programmeId, evenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> deleteEvenementByProgrammeId(Long evenementId, Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Programme> updateEtapeOfProgrammeId (Long programmeId, @Valid Long etapeId) throws Exception {
        return new ResponseEntity<>(programmeService.updateEtapeOfProgrammeId (programmeId, etapeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> associateTiersToProgramme(Long programmeId, @NotNull @Valid Long tiersId, @NotNull @Valid Long typeTiersId) throws Exception {
        return new ResponseEntity<>(programmeTiersService.associateTiersToProgramme(programmeId, tiersId, typeTiersId), HttpStatus.OK);
    }

}

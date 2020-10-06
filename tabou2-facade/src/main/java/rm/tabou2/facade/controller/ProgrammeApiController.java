package rm.tabou2.facade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ProgrammesApi;
import rm.tabou2.service.ProgrammeService;
import rm.tabou2.service.ProgrammeTiersService;
import rm.tabou2.service.dto.*;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class ProgrammeApiController implements ProgrammesApi {


    @Autowired
    private ProgrammeService programmeService;

    @Autowired
    private ProgrammeTiersService programmeTiersService;

    @Override
    public ResponseEntity<Programme> addProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.addProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Programme> editProgramme(@Valid Programme programme) throws Exception {
        return new ResponseEntity<>(programmeService.editProgramme(programme), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Agapeo>> getAgapeoByProgrammeId(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Dates> getDates(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Logements> getLogements(Long programmeId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<List<PermisConstruire>> getPermisByProgrammeId(Long programmeId) throws Exception {
        //TODO
        return null;
    }

    @Override
    public ResponseEntity<Programme> getProgrammeById(Long programmeId) throws Exception {
        return new ResponseEntity<>(programmeService.getProgrammeById(programmeId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> getProgrammes(@Valid String nom, @Valid String etape, @Valid Boolean diffusionRestreinte,
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

        if( null == orderBy){
            orderBy = "nom";
        }

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc);

        Page<Programme> page = programmeService.searchProgrammes(programmeCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Programme> associateTiersToProgramme(@Valid ProgrammeTiers programmeTiers) throws Exception {
        return new ResponseEntity<>(programmeTiersService.associateTiersToProgramme(programmeTiers.getProgrammeId(), programmeTiers.getTiersId(), programmeTiers.getTypeTiersId()), HttpStatus.OK);
    }

}

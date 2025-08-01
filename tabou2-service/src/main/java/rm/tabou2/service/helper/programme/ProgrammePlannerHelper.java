package rm.tabou2.service.helper.programme;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.item.AgapeoSuiviHabitat;

@Component
@RequiredArgsConstructor
public class ProgrammePlannerHelper {

    private static final String PERMIS_MODIFICATIF = "m";
    private static final String PERMIS_TEMPORAIRE = "t";
    
    private final AgapeoDao agapeoDao;

    private final PermisConstruireDao permisConstruireDao;
    
    private final DateHelper dateHelper;

    @Value("#{'${pc.decisions-exclues}'.split(';')}")
    private List<String> decisionsExclues;

    public void computeSuiviHabitatOfProgramme(Page<Programme> programmes) {
        for (Programme programme : programmes.getContent()) {
            computeSuiviHabitatOfProgramme(programme);
        }
    }

    public void computeSuiviHabitatOfProgramme(Programme programme) {
        if (StringUtils.isNotEmpty(programme.getNumAds())) {
            computePermisSuiviHabitatOfProgramme(programme);
            computeAgapeoSuiviHabitatOfProgramme(programme);
        }
    }

    /**
     * Mise à jour des paramètres date agapeo en fonction des données agapeo du programme
     *
     * @param programme programme
     */
    public void computePermisSuiviHabitatOfProgramme(Programme programme) {

        List<PermisConstruireEntity> permis = permisConstruireDao.findAllByNumAds(programme.getNumAds());

        programme.setDocDate(computeDocDate(permis));

        programme.setDatDate(computeDatDate(permis));

        programme.setAdsDate(computeAdsDate(permis));

    }

    /** Gestion de la date Doc
     * Règle : la date Doc s'obtient en prenant le maximum de la date de DOC de toutes les versions de l'ADS ( T et M exclus)
     *
     * @param permis Liste des permis
     * @return Date dateDoc
     */
    public OffsetDateTime computeDocDate(List<PermisConstruireEntity> permis){
        return dateHelper.convertToOffset(permis.stream().filter(p -> p.getVersionAds() == null
                        || (!p.getVersionAds().toLowerCase().contains(PERMIS_TEMPORAIRE)
                        && !p.getVersionAds().toLowerCase().contains(PERMIS_MODIFICATIF)))
                .filter(p -> p.getDecision() != null && !decisionsExclues.contains(p.getDecision()))
                .map(PermisConstruireEntity::getDocDate)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null));
    }

    /** Gestion de la date Dat
     *  Règle : la date de DAACT s'obtient en prenant le maximum de la date de DAACT de toutes les versions de l'ADS ( inclus T )
     *
     * @param permis Liste des permis
     * @return Date dateDat
     */
    public OffsetDateTime computeDatDate(List<PermisConstruireEntity> permis){
        return dateHelper.convertToOffset(permis.stream().filter(p -> p.getVersionAds() == null
                        || p.getVersionAds().toLowerCase().contains(PERMIS_TEMPORAIRE))
                .filter(p -> p.getDecision() != null && !decisionsExclues.contains(p.getDecision()))
                .map(PermisConstruireEntity::getDatDate)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null));
    }

    /** Gestion de la date ADS
     * Règle :  la date de décision s'obtient en prenant le maximum de la date de décision de toutes les versions de l'ADS (inclus T et M)
     * @param permis
     * @return
     */
    public OffsetDateTime computeAdsDate(List<PermisConstruireEntity> permis){
    	return dateHelper.convertToOffset(permis.stream()
                .filter(p -> p.getDecision() != null && !decisionsExclues.contains(p.getDecision()))
                .map(PermisConstruireEntity::getAdsDate)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null));
    }
    /**
     * Mise à jour des paramètres nombre de logements en fonction des permis de constuire du programme
     *
     * @param programme programme
     */
    public void computeAgapeoSuiviHabitatOfProgramme(Programme programme) {
        AgapeoSuiviHabitat agapeoSuiviHabitat = agapeoDao.getAgapeoSuiviHabitatByNumAds(programme.getNumAds());

        programme.setLogementsAccessAide(agapeoSuiviHabitat != null && agapeoSuiviHabitat.getLogementsAccessAide() > 0
                ? agapeoSuiviHabitat.getLogementsAccessAide()
                : programme.getLogementsAccessAidePrevu());

        programme.setLogementsAccessMaitrise(agapeoSuiviHabitat != null && agapeoSuiviHabitat.getLogementsAccessMaitrise() > 0
                ? agapeoSuiviHabitat.getLogementsAccessMaitrise()
                : programme.getLogementsAccessMaitrisePrevu());

        programme.setLogementsLocatifAide(agapeoSuiviHabitat != null && agapeoSuiviHabitat.getLogementsLocatifAide() > 0
                ? agapeoSuiviHabitat.getLogementsLocatifAide()
                : programme.getLogementsLocatifAidePrevu());

        programme.setLogementsLocatifReguleHLM(agapeoSuiviHabitat != null && agapeoSuiviHabitat.getLogementsLocatifReguleHlm() > 0
                ? agapeoSuiviHabitat.getLogementsLocatifReguleHlm()
                : programme.getLogementsLocatifReguleHlmPrevu());

        programme.setLogementsLocatifRegulePrive(agapeoSuiviHabitat != null && agapeoSuiviHabitat.getLogementsLocatifRegulePrive() > 0
                ? agapeoSuiviHabitat.getLogementsLocatifRegulePrive()
                : programme.getLogementsLocatifRegulePrivePrevu());

    }
}

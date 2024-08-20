package rm.tabou2.service.helper.programme;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.item.AgapeoSuiviHabitat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ProgrammePlannerHelper {

    private static final String PERMIS_MODIFICATIF = "m";
    private static final String PERMIS_TEMPORAIRE = "t";
    @Autowired
    private AgapeoDao agapeoDao;

    @Autowired
    private PermisConstruireDao permisConstruireDao;

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
    public Date computeDocDate(List<PermisConstruireEntity> permis){
        return permis.stream().filter(p -> p.getVersionAds() == null
                        || (!p.getVersionAds().toLowerCase().contains(PERMIS_TEMPORAIRE)
                        && !p.getVersionAds().toLowerCase().contains(PERMIS_MODIFICATIF)))
                .filter(p -> p.getDecision() != null && !decisionsExclues.contains(p.getDecision()))
                .map(PermisConstruireEntity::getDocDate)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);
    }

    /** Gestion de la date Dat
     *  Règle : la date de DAACT s'obtient en prenant le maximum de la date de DAACT de toutes les versions de l'ADS ( inclus T )
     *
     * @param permis Liste des permis
     * @return Date dateDat
     */
    public Date computeDatDate(List<PermisConstruireEntity> permis){
        return permis.stream().filter(p -> p.getVersionAds() == null
                        || p.getVersionAds().toLowerCase().contains(PERMIS_TEMPORAIRE))
                .filter(p -> p.getDecision() != null && !decisionsExclues.contains(p.getDecision()))
                .map(PermisConstruireEntity::getDatDate)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);
    }

    /** Gestion de la date ADS
     * Règle :  la date de décision s'obtient en prenant le maximum de la date de décision de toutes les versions de l'ADS (inclus T et M)
     * @param permis
     * @return
     */
    public Date computeAdsDate(List<PermisConstruireEntity> permis){
        return permis.stream()
                .filter(p -> p.getDecision() != null && !decisionsExclues.contains(p.getDecision()))
                .map(PermisConstruireEntity::getAdsDate)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);
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

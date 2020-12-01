package rm.tabou2.service.helper.programme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.storage.ddc.dao.PermisConstruireDao;
import rm.tabou2.storage.ddc.item.PermisConstruireSuiviHabitat;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;
import rm.tabou2.storage.tabou.item.AgapeoSuiviHabitat;

@Component
public class ProgrammePlannerHelper {

    @Autowired
    private AgapeoDao agapeoDao;

    @Autowired
    private PermisConstruireDao permisConstruireDao;

    public void computeSuiviHabitatOfProgramme(Programme programme) {
        computePermisSuiviHabitatOfProgramme(programme);
        computeAgapeoSuiviHabitatOfProgramme(programme);
    }

    /**
     * Mise à jour des paramètres date agapeo en fonction des données agapeo du programme
     * @param programme programme
     */
    public void computePermisSuiviHabitatOfProgramme(Programme programme) {
        PermisConstruireSuiviHabitat permisConstruireSuiviHabitat = permisConstruireDao.getPermisSuiviHabitatByNumAds(programme.getNumAds());

        programme.setDatDate(permisConstruireSuiviHabitat != null && permisConstruireSuiviHabitat.getDatDate() != null
                ? permisConstruireSuiviHabitat.getDatDate()
                : programme.getDatDatePrevu());

        programme.setDocDate(permisConstruireSuiviHabitat != null && permisConstruireSuiviHabitat.getDocDate() != null
                ? permisConstruireSuiviHabitat.getDocDate()
                : programme.getDocDatePrevu());

        programme.setAdsDate(permisConstruireSuiviHabitat != null && permisConstruireSuiviHabitat.getAdsDate() != null
                ? permisConstruireSuiviHabitat.getAdsDate()
                : programme.getAdsDatePrevu());
    }

    /**
     * Mise à jour des paramètres nombre de logements en fonction des permis de constuire du programme
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

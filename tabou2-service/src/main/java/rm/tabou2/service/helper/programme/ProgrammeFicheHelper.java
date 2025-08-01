package rm.tabou2.service.helper.programme;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AbstractFicheHelper;
import rm.tabou2.service.helper.date.DateHelper;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.FicheSuiviProgrammeDataModel;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeTiersDao;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.AgapeoSuiviHabitat;
import rm.tabou2.storage.tabou.item.PermisConstruireSuiviHabitat;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProgrammeFicheHelper extends AbstractFicheHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammeFicheHelper.class);

	private final DocumentGenerator documentGenerator;

	private final AgapeoDao agapeoDao;

	private final PermisConstruireDao permisConstruireDao;

	private final ProgrammeTiersDao programmeTiersDao;

	private final ProgrammePlannerHelper programmePlannerHelper;

	private final DateHelper dateHelper;

	@Value("${fiche.template.programme}")
	private String pathTemplate;

	public GenerationModel buildGenerationModel(ProgrammeEntity programmeEntity) throws
			AppServiceException {

		String path = pathTemplate;

		File templateFile = new File(pathTemplate);
		if(!templateFile.exists()) {
			LOGGER.warn("Le chemin de template spécifié ({}) n'existe pas, utilisation du chemin par défaut", pathTemplate);
			path = "template/programme/template_fiche_suivi.odt";
		}

		FicheSuiviProgrammeDataModel ficheSuiviProgrammeDataModel = new FicheSuiviProgrammeDataModel();
		ficheSuiviProgrammeDataModel.setProgramme(programmeEntity);
		ficheSuiviProgrammeDataModel.setOperation(programmeEntity.getOperation());
		ficheSuiviProgrammeDataModel.setNature(programmeEntity.getOperation().getNature());
		ficheSuiviProgrammeDataModel.setEtape(programmeEntity.getEtapeProgramme());
		ficheSuiviProgrammeDataModel.setIllustration(documentGenerator.generatedImgForTemplate(AlfrescoTabouType.PROGRAMME, programmeEntity.getId()));
		ficheSuiviProgrammeDataModel.setNomFichier(buildRapportFileName(programmeEntity));

		if (StringUtils.isNotEmpty(programmeEntity.getNumAds())) { // traiter le cas où le numAds ne retourne rien / est vide

			AgapeoSuiviHabitat agapeoSuiviHabitat = agapeoDao.getAgapeoSuiviHabitatByNumAds(programmeEntity.getNumAds());
			if (agapeoSuiviHabitat != null) ficheSuiviProgrammeDataModel.setAgapeoSuiviHabitat(agapeoSuiviHabitat);

			List<AgapeoEntity> agapeos = agapeoDao.findAllByNumAds(programmeEntity.getNumAds());
			if (agapeos != null) ficheSuiviProgrammeDataModel.setAgapeos(agapeos);

			List<PermisConstruireEntity> permis = permisConstruireDao.findAllByNumAds(programmeEntity.getNumAds());
			if (permis != null) {
				ficheSuiviProgrammeDataModel.setPermis(permis);

				PermisConstruireSuiviHabitat permisConstruireSuiviHabitat = new PermisConstruireSuiviHabitat();
				permisConstruireSuiviHabitat.setAdsDate(dateHelper.convert(programmePlannerHelper.computeAdsDate(permis)));
				permisConstruireSuiviHabitat.setDatDate(dateHelper.convert(programmePlannerHelper.computeDatDate(permis)));
				permisConstruireSuiviHabitat.setDocDate(dateHelper.convert(programmePlannerHelper.computeDocDate(permis)));
				ficheSuiviProgrammeDataModel.setPermisSuiviHabitat(permisConstruireSuiviHabitat);
			}
		}

		ficheSuiviProgrammeDataModel.setEvenements(List.copyOf(programmeEntity.getEvenements()));
		ficheSuiviProgrammeDataModel.setProgrammeTiers(programmeTiersDao.findByProgrammeId(programmeEntity.getId()));
		ficheSuiviProgrammeDataModel.setHelper(this);
		return new GenerationModel(ficheSuiviProgrammeDataModel, path, MediaType.APPLICATION_PDF.getSubtype());
	}

	private String buildRapportFileName(ProgrammeEntity programme) {

		StringBuilder fileName = new StringBuilder("FicheSuivi_");
		fileName.append(programme.getId());
		fileName.append("_");
		fileName.append(programme.getCode());
		fileName.append("_");
		fileName.append(programme.getNom());
		fileName.append("_");
		fileName.append(System.nanoTime());

		return fileName.toString();

	}
}

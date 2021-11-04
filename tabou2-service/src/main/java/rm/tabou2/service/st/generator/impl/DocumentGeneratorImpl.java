package rm.tabou2.service.st.generator.impl;

import fr.opensagres.odfdom.converter.core.utils.IOUtils;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.alfresco.impl.AlfrescoServiceImpl;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.DataModel;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.FieldMetadataTypeEnum;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.service.utils.PaginationUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DocumentGeneratorImpl implements DocumentGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentGeneratorImpl.class);
    private static final String[] ALLOWED_ILLUSTRATION_MIME_TYPE = {"image/jpeg", "image/png"};

    @Value("${temporary.directory}")
    private String temporaryDirectory;

    @Value("${Fiche_PA_type_illustration}")
    private String libelleIllustration;

    @Value("${Fiche_PA_illustration_defaut}")
    private String defaultIllustration;

    @Autowired
    private AlfrescoServiceImpl alfrescoService;

    @Override
    public DocumentContent generateDocument(GenerationModel generationModel) throws AppServiceException {

        if (generationModel == null) {
            throw new IllegalArgumentException("Les données de génération du document sont absentes");
        }

        // on s'assure que le répertoire temporaire existe
        ensureTargetDirectoryFile();

        // export pdf
        if (MediaType.APPLICATION_PDF.getSubtype().equalsIgnoreCase(generationModel.getOutputFileExtension())) {
            return generatePDFDocument(generationModel);
        }

        // format d'export non traité
        throw new AppServiceException(String.format("Le type de document %s est inexistant ou non traité par l'application",
                generationModel.getOutputFileExtension()));
    }

    /**
     * Génération d'un document pdf
     *
     * @param generationModel modèle d'export
     * @return document
     * @throws AppServiceException erreur lors de l'export
     */
    private DocumentContent generatePDFDocument(GenerationModel generationModel) throws AppServiceException {
        File generateFile;

        try {
            generateFile = File.createTempFile("tmp", "." + MediaType.APPLICATION_PDF.getSubtype(), new File(temporaryDirectory));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Temporary generation file:{}", generateFile);
            }
        } catch (IOException e) {
            throw new AppServiceException("erreur lors de la génération du fichier temporaire", e);
        }

        Options options = Options.getTo(ConverterTypeTo.PDF);

        generateDocumentIntoFile(generationModel, generateFile, options);

        return new DocumentContent(generateFile.getName(), MediaType.APPLICATION_PDF_VALUE, generateFile);
    }

    /**
     * Génération d'un document vers un fichier
     *
     * @param generationModel modèle d'export
     * @param outputFile      fichier output
     * @param options         options d'export
     * @throws AppServiceException erreur lors de l'export
     */
    protected void generateDocumentIntoFile(GenerationModel generationModel, File outputFile, Options options) throws AppServiceException {

        try (FileOutputStream out = new FileOutputStream(outputFile)) {

            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(generationModel.getTemplateinputStream(), TemplateEngineKind.Freemarker);

            IContext context = report.createContext();

            DataModel dataModel = generationModel.getDataModel();

            if (dataModel != null) {
                context.putMap(dataModel.getContextDatas());

                FieldsMetadata metadata = buildFieldsMetadata(dataModel.getContextFieldMetadatas());
                report.setFieldsMetadata(metadata);
            }

            report.convert(context, options, out);

        } catch (IOException e) {
            throw new AppServiceException("Erreur lors de la récupération du template", e);
        } catch (XDocReportException e) {
            throw new AppServiceException("Erreur lors de la génération du document", e);
        }
    }


    @Override
    public File generatedImgForTemplate(AlfrescoTabouType tabouType, long objectId) throws AppServiceException {

        // on s'assure que le répertoire temporaire existe
        ensureTargetDirectoryFile();

        File generateFile;
        try {
            generateFile = File.createTempFile("tmp", "." + MediaType.IMAGE_JPEG.getSubtype(), new File(temporaryDirectory));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Temporary generation file:{}", generateFile);
            }
        } catch (IOException e) {
            throw new AppServiceException("Erreur lors de la génération de l'image", e);
        }

        List<String> ids = new ArrayList<>();
        InputStream fileiImgIllustration = null;

        Pageable pageable = PaginationUtils.buildPageableForAlfresco(0, 1, null, true);
        try {
            ids = alfrescoService.searchDocuments(tabouType, objectId, null,
                            libelleIllustration, null, pageable)
                    .getList()
                    .getEntries()
                    .stream()
                    .filter(x -> Arrays.asList(ALLOWED_ILLUSTRATION_MIME_TYPE).contains(x.getEntry().getContent().getMimeType()))
                    .map(x -> x.getEntry().getId())
                    .collect(Collectors.toList());
        }catch(Exception e){
            LOGGER.warn("Alfresco is unreachable, uses default image", e);
        }
        try {
            fileiImgIllustration = new ClassPathResource(defaultIllustration).getInputStream();
            if (!ids.isEmpty()) {
                fileiImgIllustration = alfrescoService.downloadDocument(tabouType, objectId, ids.get(0)).getFileStream();
            }
        }catch (IOException e) {
            throw new AppServiceException("Erreur lors de la génération de l'image", e);
        }

        try (OutputStream outputStream = new FileOutputStream(generateFile);) {
            IOUtils.copy(fileiImgIllustration, outputStream);
        } catch (IOException e) {
            LOGGER.warn(Arrays.toString(e.getStackTrace()));
        }

        return generateFile;
    }

    /**
     * Construit les métadonnées xdocreport liées au données du document
     *
     * @param contextFieldMetadatas model métadonnées
     * @return métadonnées xdocreport
     */
    private FieldsMetadata buildFieldsMetadata(Map<String, FieldMetadataTypeEnum> contextFieldMetadatas) {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();

        contextFieldMetadatas.forEach((key, value) -> {
            if (value.equals(FieldMetadataTypeEnum.LIST)) {
                fieldsMetadata.addFieldAsList(key);
            } else if (value.equals(FieldMetadataTypeEnum.IMAGE)) {
                fieldsMetadata.addFieldAsImage(key);
            }
        });

        return fieldsMetadata;
    }

    /**
     * Permet de vérifier que le répertoire temporaire existe, sinon on le crée
     *
     * @throws AppServiceException Erreur lors de la création du répertoire temporaire
     */
    private void ensureTargetDirectoryFile() throws AppServiceException {
        File targetDirectoryFile = new File(temporaryDirectory);
        if (!targetDirectoryFile.exists()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Create directory:{}", temporaryDirectory);
            }
            if (!targetDirectoryFile.mkdirs()) {
                throw new AppServiceException("Failed to create directory:" + temporaryDirectory);
            }
        }
    }

}

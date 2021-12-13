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
import org.apache.commons.collections4.CollectionUtils;
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DocumentGeneratorImpl implements DocumentGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentGeneratorImpl.class);

    @Value("${temporary.directory}")
    private String temporaryDirectory;

    @Value("${fiche.illustration.libelle}")
    private String libelleIllustration;

    @Value("${fiche.illustration.default}")
    private String defaultIllustration;

    @Value("${fiche.illustration.typesmime}")
    private  String[] allowedMimeTypes;

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
                    .filter(x -> Arrays.asList(allowedMimeTypes).contains(x.getEntry().getContent().getMimeType()))
                    .map(x -> x.getEntry().getId())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("Alfresco innaccessible, utilisation de l'image par défaut", e);
        }

        if (!CollectionUtils.isEmpty(ids)) {
            try {
                fileiImgIllustration = alfrescoService.downloadDocument(tabouType, objectId, ids.get(0)).getFileStream();
            } catch (Exception e) {
                LOGGER.warn("Alfresco innaccessible, ou l'image d'id={} est innaccessible", ids.get(0), e);
            }
        }

        if (fileiImgIllustration == null) {
            String type = tabouType.name();
            LOGGER.warn("Aucune image disponible pour {} id = {}", type, objectId);
            try {
                fileiImgIllustration = new ClassPathResource(defaultIllustration).getInputStream();
            } catch (IOException e) {
                throw new AppServiceException("Erreur lors de la récupération de l'image par défaut", e);
            }
        }


        try (OutputStream outputStream = new FileOutputStream(generateFile);) {
            IOUtils.copy(fileiImgIllustration, outputStream);
        } catch (IOException e) {
            throw new AppServiceException(String.format("Erreur lors de la génération de l'image pour %s id = %s", tabouType.name(), objectId), e);
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

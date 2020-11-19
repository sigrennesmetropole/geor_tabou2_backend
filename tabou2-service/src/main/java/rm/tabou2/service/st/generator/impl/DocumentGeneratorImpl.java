package rm.tabou2.service.st.generator.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.DataModel;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.FieldMetadataTypeEnum;
import rm.tabou2.service.st.generator.model.GenerationModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class DocumentGeneratorImpl implements DocumentGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentGeneratorImpl.class);

    @Value("${temporary.directory}")
    private String temporaryDirectory;

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

    private FieldsMetadata buildFieldsMetadata(Map<String, FieldMetadataTypeEnum> contextFieldMetadatas) {
        FieldsMetadata fieldsMetadata = new FieldsMetadata();

        contextFieldMetadatas.forEach((key, value) -> {
            if (value.equals(FieldMetadataTypeEnum.LIST)) {
                fieldsMetadata.addFieldAsList(key);
            }
            else if (value.equals(FieldMetadataTypeEnum.IMAGE)) {
                fieldsMetadata.addFieldAsImage(key);
            }
        });

        return fieldsMetadata;
    }

    /**
     * Permet de vérifier que le répertoire temporaire existe, sinon on le crée
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

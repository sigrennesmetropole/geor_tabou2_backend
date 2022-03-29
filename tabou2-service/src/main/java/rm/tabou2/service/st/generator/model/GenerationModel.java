package rm.tabou2.service.st.generator.model;

import java.io.InputStream;

public class GenerationModel {

    private DataModel dataModel;

    private String templatePath;

    private String outputFileExtension;

    public GenerationModel(DataModel dataModel, String templatePath, String outputFileExtension) {
        this.dataModel = dataModel;
        this.templatePath = templatePath;
        this.outputFileExtension = outputFileExtension;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getOutputFileExtension() {
        return outputFileExtension;
    }

    public void setOutputFileExtension(String outputFileExtension) {
        this.outputFileExtension = outputFileExtension;
    }
}

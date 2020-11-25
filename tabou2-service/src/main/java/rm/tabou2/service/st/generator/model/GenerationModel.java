package rm.tabou2.service.st.generator.model;

import java.io.InputStream;

public class GenerationModel {

    private DataModel dataModel;

    private InputStream templateinputStream;

    private String outputFileExtension;

    public GenerationModel(DataModel dataModel, InputStream templateinputStream, String outputFileExtension) {
        this.dataModel = dataModel;
        this.templateinputStream = templateinputStream;
        this.outputFileExtension = outputFileExtension;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public InputStream getTemplateinputStream() {
        return templateinputStream;
    }

    public void setTemplateinputStream(InputStream templateinputStream) {
        this.templateinputStream = templateinputStream;
    }

    public String getOutputFileExtension() {
        return outputFileExtension;
    }

    public void setOutputFileExtension(String outputFileExtension) {
        this.outputFileExtension = outputFileExtension;
    }
}

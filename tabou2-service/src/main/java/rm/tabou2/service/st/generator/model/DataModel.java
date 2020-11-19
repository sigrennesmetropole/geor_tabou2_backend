package rm.tabou2.service.st.generator.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataModel {

    private final Map<String, Object> contextDatas;

    private final Map<String, FieldMetadataTypeEnum> contextFieldMetadatas;

    public DataModel() {
        this.contextDatas = new HashMap<>();
        this.contextFieldMetadatas = new HashMap<>();
    }

    public DataModel(Map<String, Object> contextDatas) {
        this.contextDatas = contextDatas;
        this.contextFieldMetadatas = new HashMap<>();
    }

    public DataModel(Map<String, Object> contextDatas, Map<String, FieldMetadataTypeEnum> contextFieldMetadatas) {
        this.contextDatas = contextDatas;
        this.contextFieldMetadatas = contextFieldMetadatas;
    }

    public Map<String, Object> getContextDatas() {
        return Collections.unmodifiableMap(this.contextDatas) ;
    }

    public Map<String, FieldMetadataTypeEnum> getContextFieldMetadatas() {
        return Collections.unmodifiableMap(this.contextFieldMetadatas);
    }

    public void addContextData(String key, Object data) {
        this.contextDatas.put(key, data);
    }

    public void addContextFieldList(String key) {
        addContextFieldMetadata(key, FieldMetadataTypeEnum.LIST);
    }

    public void addContextFieldImage(String key) {
        addContextFieldMetadata(key, FieldMetadataTypeEnum.IMAGE);
    }

    public void addContextFieldMetadata(String key, FieldMetadataTypeEnum metadataTypeEnum) {
        this.contextFieldMetadatas.put(key, metadataTypeEnum);
    }
}

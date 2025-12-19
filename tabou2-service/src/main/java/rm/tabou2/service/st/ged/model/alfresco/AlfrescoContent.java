package rm.tabou2.service.st.ged.model.alfresco;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AlfrescoContent {

	private String mimeType;

	private String mimeTypeName;

	@JsonProperty("sizeInBytes")
	private int size;

	private String encoding;

}

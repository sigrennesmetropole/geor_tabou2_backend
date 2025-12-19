package rm.tabou2.service.st.ged.model.alfresco;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlfrescoProperties {

	@JsonProperty("cm:title")
	public String cmTitle;

	@JsonProperty("cm:description")
	public String cmDescription;

	@JsonProperty("search:identifiers")
	public List<String> searchIdentifiers;

	@JsonProperty("search:typeDocument")
	public String searchTypeDocument;

	@JsonProperty("search:generated")
	public boolean searchGenerated;

}

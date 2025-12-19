package rm.tabou2.service.st.ged.model.alfresco;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AlfrescoEntry {

	private List<String> aspectNames;

	private Date createdAt;

	@JsonProperty("isFolder")
	private boolean folder;

	@JsonProperty("isFile")
	private boolean file;

	private AlfrescoUser createdByUser;

	private Date modifiedAt;

	private AlfrescoUser modifiedByUser;

	private String name;

	private String id;

	private String nodeType;

	private AlfrescoProperties properties;

	private AlfrescoContent content;

	private String parentId;

	private List<String> allowableOperations;

	private AlfrescoPath path;

	private AlfrescoPermissions permissions;

}

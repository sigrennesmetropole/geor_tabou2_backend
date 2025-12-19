package rm.tabou2.service.st.ged.model.alfresco;

import lombok.Data;

import java.util.Set;

@Data
public class AlfrescoContainers {

	private Set<AlfrescoContainer> entries;

	private AlfrescoPagination pagination;
}

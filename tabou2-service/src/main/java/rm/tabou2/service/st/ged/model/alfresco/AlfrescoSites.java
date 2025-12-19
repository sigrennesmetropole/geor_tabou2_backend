package rm.tabou2.service.st.ged.model.alfresco;

import lombok.Data;

import java.util.Set;

@Data
public class AlfrescoSites {

	private Set<AlfrescoSite> entries;

	private AlfrescoPagination pagination;
}

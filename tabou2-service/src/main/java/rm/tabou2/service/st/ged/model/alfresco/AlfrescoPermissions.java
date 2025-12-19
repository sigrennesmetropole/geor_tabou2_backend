/**
 * 
 */
package rm.tabou2.service.st.ged.model.alfresco;

import lombok.Data;

import java.util.List;

/**
 * @author FNI18300
 *
 */
@Data
public class AlfrescoPermissions {

	private boolean isInheritanceEnabled;

	private List<String> settable;

	private List<AlfrescoPermission> inherited;

	private List<AlfrescoPermission> locallySet;

}

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
public class AlfrescoPath {

	private List<AlfrescoEntry> elements;

	private String name;

	private boolean isComplete;
}

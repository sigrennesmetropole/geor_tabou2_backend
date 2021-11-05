package rm.tabou2.service.alfresco.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlfrescoProperties {

    @JsonProperty("cm:description")
    public String cmDescription;
    @JsonProperty("app:icon")
    public String appIcon;
    @JsonProperty("tabou2:id")
    public Long tabouId;
    @JsonProperty("tabou2:objet")
    public String objetTabou;
    @JsonProperty("tabou2:libelleTypeDocument")
    public String libelleTypeDocument;

}

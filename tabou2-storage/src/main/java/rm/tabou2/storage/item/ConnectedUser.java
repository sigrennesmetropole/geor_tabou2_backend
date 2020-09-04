package rm.tabou2.storage.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import rm.tabou2.storage.tabou.entity.Authority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Utilisateur connecté.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectedUser implements Serializable {

    private static final long serialVersionUID = 7341693007849857154L;
    private long id;
    private String login;
    private String displayName;
    private final Collection<Authority> authorities = new ArrayList<>();

    public ConnectedUser() {
    }

}

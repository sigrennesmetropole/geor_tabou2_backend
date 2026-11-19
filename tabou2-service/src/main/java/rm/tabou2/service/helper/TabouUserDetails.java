package rm.tabou2.service.helper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Utilisateur connecté, avec son organisation geOrchestra (en-tête sec-orgname).
 */
public class TabouUserDetails extends User {

    private static final long serialVersionUID = 4172945035180269231L;

    private final String organisation;

    /**
     * @param username     nom de l'utilisateur
     * @param authorities  rôles de l'utilisateur
     * @param organisation nom de l'organisation décodé, peut être null
     */
    public TabouUserDetails(String username, Collection<? extends GrantedAuthority> authorities, String organisation) {
        super(username, "", true, true, true, true, authorities);
        this.organisation = organisation;
    }

    /**
     * @return le nom de l'organisation décodé, ou null si absent
     */
    public String getOrganisation() {
        return organisation;
    }
}

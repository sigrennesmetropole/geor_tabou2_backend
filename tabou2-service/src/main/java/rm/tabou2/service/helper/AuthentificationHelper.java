package rm.tabou2.service.helper;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthentificationHelper {


    public static final String ROLE_CONSULTATION = "a_determiner";
    public static final String ROLE_CONTRIBUTEUR = "a_determiner";
    public static final String ROLE_REFERENT = "a_determiner";
    public static final String ROLE_ADMINISTRATEUR = "a_determiner";

    /**
     * Retourne le nom de l'utilisateur connecté.
     *
     * @return username
     */
    public String getConnectedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Teste si l'utilisateur connecté a le droit de consultation.
     *
     * @return
     */
    public boolean hasViewAccess() {
        return (hasConsultationRole() || hasContributeurRole() || hasReferentRole() || hasAdministratorRole());
    }

    /**
     * Teste si l'utilisateur connecté a le droit d'édition.
     *
     * @return
     */
    public boolean hasEditAccess() {
        return (hasContributeurRole() || hasReferentRole() || hasAdministratorRole());
    }

    /**
     * Test si l'utilisateur connecté a le droit d'accéder aux entités restreintes (en off).
     *
     * @return
     */
    public boolean hasRestreintAccess() {
        return (hasReferentRole() || hasAdministratorRole());
    }

    /**
     * Teste si l'utilisateur a le role consultation.
     *
     * @return
     */
    public boolean hasConsultationRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(ROLE_CONSULTATION));
    }

    /**
     * Teste si l'utilisateur a le role contributeur.
     *
     * @return
     */
    public boolean hasContributeurRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(ROLE_CONTRIBUTEUR));
    }

    /**
     * Teste si l'utilisateur a le role référent.
     *
     * @return
     */
    public boolean hasReferentRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(ROLE_REFERENT));
    }

    /**
     * Teste si l'utilisateur a le role adminsitrateur.
     *
     * @return
     */
    public boolean hasAdministratorRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMINISTRATEUR));
    }

}

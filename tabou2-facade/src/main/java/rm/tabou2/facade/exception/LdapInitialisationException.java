package rm.tabou2.facade.exception;

import org.springframework.security.core.AuthenticationException;


public class LdapInitialisationException extends AuthenticationException {

    /**
     * Exception se déclenche si LDAP inaccessible
     *
     * @param message
     */
    public LdapInitialisationException(final String message) {
        super(message);
    }
}

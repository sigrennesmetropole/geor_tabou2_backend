/**
 * 
 */
package rm.tabou2.facade.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * A provider that accepts {@link rm.tabou2.facade.security.PreAuthenticationToken} authentication objects.
 *
 * @author from Jesse on 4/24/2014.
 */
public class PreAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication instanceof rm.tabou2.facade.security.PreAuthenticationToken) {
            return authentication;
        }
        return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return rm.tabou2.facade.security.PreAuthenticationToken.class.isAssignableFrom(authentication);
	}
}

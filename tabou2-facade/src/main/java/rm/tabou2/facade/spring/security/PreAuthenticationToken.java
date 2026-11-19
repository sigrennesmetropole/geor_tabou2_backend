/**
 * 
 */
package rm.tabou2.facade.spring.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import rm.tabou2.service.helper.TabouUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * An authentication that is obtained by reading the credentials from the
 * headers.
 *
 * @see PreAuthenticationFilter
 *
 * @author from Jesse on 4/24/2014.
 */
public class PreAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -3957062197124193685L;
	private final String principal;

	/**
	 * Constructeur
	 * 
	 * @param username
	 * @param roles
	 */
	public PreAuthenticationToken(String username, Set<String> roles) {
		this(username, roles, null);
	}

	/**
	 * Constructeur
	 *
	 * @param username
	 * @param roles
	 * @param organisation nom de l'organisation (décodé), peut être null
	 */
	public PreAuthenticationToken(String username, Set<String> roles, String organisation) {
		super(createGrantedAuthorities(roles));
		this.principal = username;

		setAuthenticated(true);
		setDetails(new TabouUserDetails(username, super.getAuthorities(), organisation));

	}

	/**
	 * Construction de la listes de roles
	 * 
	 * @param roles
	 * @return
	 */
	private static Collection<? extends GrantedAuthority> createGrantedAuthorities(Set<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PreAuthenticationToken other = (PreAuthenticationToken) obj;
		if (principal == null) {
			if (other.principal != null) {
				return false;
			}
		} else if (!principal.equals(other.principal)) {
			return false;
		}
		return true;
	}
}

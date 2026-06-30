/**
 * 
 */
package rm.tabou2.facade.spring.security;

import jakarta.servlet.ServletContext;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;

/**
 * @author FNI18300
 *
 */
public class RewriteHttpConfigurationProvider extends HttpConfigurationProvider {

	public RewriteHttpConfigurationProvider() {
		super();
	}

	@Override
	public Configuration getConfiguration(ServletContext context) {
		return ConfigurationBuilder.begin();
	}

	@Override
	public int priority() {
		return 10;
	}

}

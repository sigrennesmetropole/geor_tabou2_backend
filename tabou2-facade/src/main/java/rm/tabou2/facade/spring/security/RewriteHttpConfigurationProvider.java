/**
 * 
 */
package rm.tabou2.facade.spring.security;

import javax.servlet.ServletContext;

import org.ocpsoft.logging.Logger.Level;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Log;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.Forward;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

/**
 * @author FNI18300
 *
 */
public class RewriteHttpConfigurationProvider extends HttpConfigurationProvider {

	private static final String SWAGGER_API_DOCS = "/v2/api-docs";
	private static final String TABOU2_TIERS_PATH = "/tiers/{path}";
	private static final String TABOU2_TIERS = "/tiers";

	private static final String TABOU2_OPERATION_PATH = "/operations/{path}";
	private static final String TABOU2_OPERATION = "/operations";

	public RewriteHttpConfigurationProvider() {
		super();
	}

	@Override
	public Configuration getConfiguration(ServletContext context) {
		return ConfigurationBuilder.begin()
				.addRule().when(Direction.isInbound().and(Path.matches(SWAGGER_API_DOCS))).perform(Log.message(Level.INFO,"Request swagger").and(Forward.to(SWAGGER_API_DOCS)))
				// V2 -> c'est l'officiel
				// L'URL de swgger ne doit pas être redirigée également
				.addRule().when(Direction.isInbound().and(Path.matches("/v2/{path}")).andNot(Path.matches(SWAGGER_API_DOCS).and(DispatchType.isRequest()))).perform(Forward.to("/{path}")).where("path").matches(".*")
				// V1/tiers** -> c'est l'officiel pour les tiers
				.addRule(Join.path("/v1" + TABOU2_TIERS).to("/v1" +  TABOU2_TIERS))
				.addRule(Join.path("/v1" + TABOU2_TIERS_PATH).to("/v1" +  TABOU2_TIERS_PATH)).where("path").matches(".*")
				//Gestion de la redirection des opérations
				.addRule(Join.path("/v1" + TABOU2_OPERATION).to("/v1" + TABOU2_OPERATION))
				.addRule(Join.path("/v1" + TABOU2_OPERATION_PATH).to("/v1" + TABOU2_OPERATION_PATH))
				.addRule(Join.path("/v1" + TABOU2_OPERATION_PATH + "/etapes").to("/v1" + TABOU2_OPERATION_PATH + "/etapes"))
				.addRule(Join.path("/v1" + TABOU2_OPERATION_PATH + "/evenements/{eventId}").to("/v1" + TABOU2_OPERATION_PATH + "/evenements/{eventId}"))
				// V1/** -> c'est l'officiel
				.addRule(Join.path("/v1/{path}").to("/{path}")).where("path").matches(".*")
				// /tiers -> l'officiel c'est V1
				.addRule(Join.path(TABOU2_TIERS).to("/v1" + TABOU2_TIERS))
				.addRule(Join.path(TABOU2_TIERS_PATH).to("/v1" + TABOU2_TIERS_PATH)).where("path").matches(".*")
				// Redirection des opérations actuelles vers la v1
				.addRule(Join.path(TABOU2_OPERATION).to("/v1" + TABOU2_OPERATION))
				.addRule(Join.path(TABOU2_OPERATION_PATH).to("/v1" + TABOU2_OPERATION_PATH))
				.addRule(Join.path(TABOU2_OPERATION_PATH + "/etapes").to("/v1" + TABOU2_OPERATION_PATH + "/etapes"))
				.addRule(Join.path(TABOU2_OPERATION_PATH + "/evenements/{eventId}").to("/v1" + TABOU2_OPERATION_PATH + "/evenements/{eventId}"));
	}

	@Override
	public int priority() {
		return 10;
	}

}

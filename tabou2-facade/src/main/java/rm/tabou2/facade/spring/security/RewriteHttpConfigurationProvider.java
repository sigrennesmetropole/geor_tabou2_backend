/**
 * 
 */
package rm.tabou2.facade.spring.security;

import javax.servlet.ServletContext;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Log;
import org.ocpsoft.rewrite.servlet.config.*;
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
				.addRule().when(Direction.isInbound().and(Path.matches(SWAGGER_API_DOCS)).and(DispatchType.isRequest())).perform(Log.message(Logger.Level.INFO,"Request swagger").and(Forward.to(SWAGGER_API_DOCS)))

				// /V2/tiers** c'est la v2
				.addRule(Join.path("/v2" + TABOU2_TIERS).to("/v2" + TABOU2_TIERS)).withPriority(0).perform(Log.message(Logger.Level.INFO, "Redirection /v2/tiers -> /v2/tiers"))
				.addRule(Join.path("/v2" + TABOU2_TIERS_PATH).to("/v2" +  TABOU2_TIERS_PATH)).where("path").matches(".*").withPriority(0)
				// /V2/operations** c'est la v2
				.addRule(Join.path("/v2" + TABOU2_OPERATION).to("/v2" + TABOU2_OPERATION)).withPriority(0).perform(Log.message(Logger.Level.INFO, "Redirection /v2/operations -> /v2/operations"))
				.addRule(Join.path("/v2" + TABOU2_OPERATION_PATH).to("/v2" + TABOU2_OPERATION_PATH)).where("path").matches("[0-9]*").withPriority(0)
				.addRule(Join.path("/v2" + TABOU2_OPERATION_PATH + "/etapes").to("/v2" + TABOU2_OPERATION_PATH + "/etapes")).when(Method.isPut().and(Direction.isInbound()).and(DispatchType.isRequest())).withPriority(0)
				.addRule(Join.path("/v2" + TABOU2_OPERATION_PATH + "/evenements/{eventId}").to("/v2" + TABOU2_OPERATION_PATH + "/evenements/{eventId}")).withPriority(0)
				// V2/** -> c'est l'officiel
				.addRule().when(Direction.isInbound().and(DispatchType.isRequest()).and(Path.matches("/v2/{path}")).andNot(Path.matches(SWAGGER_API_DOCS)))
				.perform(Forward.to("/{path}").and(Log.message(Logger.Level.INFO, "Redirection /v2/{path} -> /{path}")))
				.where("path").matches(".*").withPriority(10)

				// V1/tiers** -> c'est l'officiel pour les tiers
				.addRule(Join.path("/v1" + TABOU2_TIERS).to("/v1" +  TABOU2_TIERS)).withPriority(0).perform(Log.message(Logger.Level.INFO, "Redirection /v1/tiers -> /v1/tiers"))
				.addRule(Join.path("/v1" + TABOU2_TIERS_PATH).to("/v1" +  TABOU2_TIERS_PATH)).where("path").matches(".*").withPriority(0)
				//Gestion de la redirection des opérations
				.addRule(Join.path("/v1" + TABOU2_OPERATION).to("/v1" + TABOU2_OPERATION)).withPriority(0).perform(Log.message(Logger.Level.INFO, "Redirection /v1/operations -> /v1/operations"))
				.addRule(Join.path("/v1" + TABOU2_OPERATION_PATH).to("/v1" + TABOU2_OPERATION_PATH)).where("path").matches("[0-9]*").withPriority(0)
				.addRule(Join.path("/v1" + TABOU2_OPERATION_PATH + "/etapes").to("/v1" + TABOU2_OPERATION_PATH + "/etapes")).withPriority(0)
				.addRule(Join.path("/v1" + TABOU2_OPERATION_PATH + "/evenements/{eventId}").to("/v1" + TABOU2_OPERATION_PATH + "/evenements/{eventId}")).withPriority(0)

				// V1/** -> c'est l'officiel
				.addRule(Join.path("/v1/{path}").to("/{path}")).when(DispatchType.isRequest()).perform(Log.message(Logger.Level.INFO, "Redirection /v1/{path} -> /{path}")).where("path").matches(".*").withPriority(10)

				// /tiers -> l'officiel c'est V1
				.addRule(Join.path(TABOU2_TIERS).to("/v1" + TABOU2_TIERS)).perform(Log.message(Logger.Level.INFO, "Redirection /tiers -> /v1/tiers"))
				.addRule(Join.path(TABOU2_TIERS_PATH).to("/v1" + TABOU2_TIERS_PATH)).where("path").matches(".*")
				// Redirection des opérations actuelles vers la v1
				.addRule(Join.path(TABOU2_OPERATION).to("/v1" + TABOU2_OPERATION)).when(Direction.isInbound().and(DispatchType.isRequest())).perform(Log.message(Logger.Level.INFO, "Redirection /operations -> /v1/operations"))
				.addRule(Join.path(TABOU2_OPERATION_PATH).to("/v1" + TABOU2_OPERATION_PATH)).when(Direction.isInbound().and(DispatchType.isRequest())).where("path").matches("[0-9]*")
				.addRule(Join.path(TABOU2_OPERATION_PATH + "/etapes").to("/v1" + TABOU2_OPERATION_PATH + "/etapes")).when(Method.isPut().and(Direction.isInbound()).and(DispatchType.isRequest()))
				.addRule(Join.path(TABOU2_OPERATION_PATH + "/evenements/{eventId}").to("/v1" + TABOU2_OPERATION_PATH + "/evenements/{eventId}")).when(Direction.isInbound().and(DispatchType.isRequest()));
	}

	@Override
	public int priority() {
		return 10;
	}

}

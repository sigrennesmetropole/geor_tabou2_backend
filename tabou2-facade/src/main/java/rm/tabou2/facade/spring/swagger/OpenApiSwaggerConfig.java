package rm.tabou2.facade.spring.swagger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;


public class OpenApiSwaggerConfig {

    @Value("${swagger-server:}")
    private List<String> serverUrls;

    @Bean
	public OpenAPI springOpenAPI() {
		OpenAPI openAPI = new OpenAPI().openapi("3.0.0").info(apiInfo()).components(apiComponents()).security(apiSecurityRequirements());

		if( CollectionUtils.isNotEmpty(serverUrls)) {
			openAPI.servers(computeServers());
		}
		return openAPI;
	}

	protected Info apiInfo() {
		return new Info().title("Georchestra - Plugin Urb@Map API").version("1.0");
	}

	protected Components apiComponents() {
		return new Components().addSecuritySchemes("basicauth", securityScheme());
	}
	protected io.swagger.v3.oas.models.security.SecurityScheme securityScheme() {
		return new io.swagger.v3.oas.models.security.SecurityScheme().type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP).scheme("basic");
	}

	protected List<SecurityRequirement> apiSecurityRequirements() {
		return Collections.singletonList(new SecurityRequirement().addList("basicauth"));
	}

	protected List<Server> computeServers() {
		List<Server> result = new ArrayList<>();
		for (String serverUrl : serverUrls) {
			Server server = new Server();
			server.setUrl(serverUrl);
			result.add(server);
		}
		return result;
	}
}

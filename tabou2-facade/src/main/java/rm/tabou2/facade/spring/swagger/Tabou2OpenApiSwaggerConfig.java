package rm.tabou2.facade.spring.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Tabou2OpenApiSwaggerConfig extends OpenApiSwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("Urbam@p").packagesToScan("rm.tabou2.facade.controller").pathsToMatch("/*").build();
    }
}

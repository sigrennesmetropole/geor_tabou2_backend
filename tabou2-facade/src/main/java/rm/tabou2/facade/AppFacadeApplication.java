package rm.tabou2.facade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Classe de configuration globale de l'application.
 */
@SpringBootApplication(scanBasePackages = {"rm.tabou2.facade", "rm.tabou2.service", "rm.tabou2.storage"})
@EntityScan(basePackages = "rm.tabou2.storage.entity")
@EnableJpaRepositories(basePackages = "rm.tabou2.storage.dao")
@PropertySources({@PropertySource(value = {"classpath:tabou2.properties"}), @PropertySource(value = {"file:${tabou2.properties}"}, ignoreResourceNotFound = true)})
public class AppFacadeApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {

        // Renomage du fichier de properties pour éviter les conflits avec d'autres applications sur le tomcat
        System.setProperty("spring.config.name", "tabou2");
        SpringApplication.run(AppFacadeApplication.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(AppFacadeApplication.class);
    }

}

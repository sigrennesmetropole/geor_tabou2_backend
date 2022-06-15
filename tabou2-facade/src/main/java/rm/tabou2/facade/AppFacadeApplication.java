package rm.tabou2.facade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

/**
 * Classe de configuration globale de l'application.
 */
@SpringBootApplication(scanBasePackages = {"rm.tabou2.facade", "rm.tabou2.service", "rm.tabou2.storage.tabou", "rm.tabou2.storage.ddc", "rm.tabou2.storage.sig"})
@PropertySource(value = { "classpath:tabou2-common.properties" }, ignoreResourceNotFound = false)
@PropertySource(value = { "file:${georchestra.datadir}/default.properties" }, ignoreResourceNotFound = false)
@PropertySource(value = { "file:${georchestra.datadir}/tabou2/tabou2.properties" }, ignoreResourceNotFound = false)
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

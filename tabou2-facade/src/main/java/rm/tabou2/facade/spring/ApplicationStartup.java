package rm.tabou2.facade.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {


    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);


    /**
     * Methode appelée à l'initilisation de l'application SpringBoot
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.debug("Application Ready Event");
    }

}

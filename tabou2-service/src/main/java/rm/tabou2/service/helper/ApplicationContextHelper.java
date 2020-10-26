package rm.tabou2.service.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationC) throws BeansException {
        applicationContext = applicationC;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}

package rm.tabou2.service.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan(basePackages = {"rm.tabou2.service.aop"})
public class ServiceBeanConfiguration {


}

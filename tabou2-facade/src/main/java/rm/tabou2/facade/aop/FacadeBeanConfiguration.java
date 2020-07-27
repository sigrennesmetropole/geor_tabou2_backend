package rm.tabou2.facade.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy()
@ComponentScan(basePackages = {"rm.tabou2.facade.aop"})
public class FacadeBeanConfiguration {


}

package rm.tabou2.facade.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;


@Aspect
@Component
public class FacadeAspect {

    private static final Logger LOG = LoggerFactory.getLogger(FacadeAspect.class);

    @Autowired
    private AuthentificationHelper authentificationHelper;

    //Pour chaque entrée dans un controller
    @Pointcut("execution(* rm.tabou2.facade.controller.*.*(..))")
    public void businessMethods() {
        //Definition du pointcut
    }


    @Around("businessMethods()")
    public Object profile(final ProceedingJoinPoint pjp) throws Throwable {
        final Object output = pjp.proceed();

        final String accountname = authentificationHelper.getConnectedUsername();
        if (LOG.isInfoEnabled()) {
            LOG.info(String.format("[Account] = %s - %s : %s", accountname, pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName()));
        }
        return output;
    }

}

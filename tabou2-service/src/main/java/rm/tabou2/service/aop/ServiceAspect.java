package rm.tabou2.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ServiceAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceAspect.class);

    @Pointcut("execution(* rm.tabou2.service.impl.*.*(..))")
    public void businessMethods() {
    }

    @Around("businessMethods()")
    public Object profile(final ProceedingJoinPoint pjp) throws Throwable {
        final long start = System.currentTimeMillis();
        final Object output = pjp.proceed();
        final long elapsedTime = System.currentTimeMillis() - start;
        LOG.info(elapsedTime + " - " + pjp.getSignature().getDeclaringTypeName() + " " + pjp.getSignature().getName());
        return output;
    }

}

package rm.tabou2.storage.aop;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class StorageAspect {

    private static final Logger LOG = LoggerFactory.getLogger(StorageAspect.class);

    //TODO : à décommenter dès qu'un DAO sera implémenté


    /*@Pointcut("execution(* rm.tabou2.storage.dao.impl.*.*(..))")
    public void businessMethods() {
    }

    @Around("businessMethods()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object output = pjp.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        LOG.info(elapsedTime + " - " + pjp.getSignature().getDeclaringTypeName() + " " + pjp.getSignature().getName());
        return output;
    }*/

}

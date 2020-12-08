package rm.tabou2.storage.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class StorageAspect {

    private static final Logger LOG = LoggerFactory.getLogger(StorageAspect.class);




    @Pointcut("execution(* rm.tabou2.storage.tabou.dao.impl.*.*(..))")
    public void businessMethods() {
        //Definition du pointcunt
    }

    @Around("businessMethods()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object output = pjp.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        if (LOG.isInfoEnabled()) {
            LOG.info(String.format("%s - %s - %s ", elapsedTime, pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName()));
        }
        return output;
    }

}

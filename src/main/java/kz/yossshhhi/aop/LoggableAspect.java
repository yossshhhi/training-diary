package kz.yossshhhi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class LoggableAspect {

    @Pointcut("within(@kz.yossshhhi.aop.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() { }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Calling method " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        log.info("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. It took: " + end + " ms.");
        return result;
    }
}

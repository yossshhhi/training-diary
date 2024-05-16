package kz.yossshhhi.starter.logging.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method execution.
 * This aspect provides around advice that logs the beginning and completion of any method
 * annotated with {@link kz.yossshhhi.starter.logging.aop.annotation.Loggable}. It logs the method signature and the execution time.
 * This can be particularly useful for performance monitoring and debugging.
 */
@Aspect
@Slf4j
@Component
public class LoggableAspect {

    /**
     * Pointcut that matches all methods annotated with {@link kz.yossshhhi.starter.logging.aop.annotation.Loggable}.
     * This pointcut is used to identify methods that should be intercepted by the logging advice.
     */
    @Pointcut("within(@kz.yossshhhi.starter.logging.aop.annotation.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() { }

    /**
     * Around advice that logs method entry, execution time, and exit.
     *
     * @param proceedingJoinPoint the join point providing reflective access to both static
     *                            and dynamic parts of the method being advised.
     * @return the result of the advised method call.
     * @throws Throwable if any error occurs during the method execution or advice processing.
     */
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


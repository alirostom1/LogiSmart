package io.github.alirostom1.logismart.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class ServiceLoggingAspect {
    private static final Logger serviceLogger = LoggerFactory.getLogger("ServiceAspectLogger");

    @Pointcut("within(io.github.alirostom1.logismart.service..*)")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        serviceLogger.info("Executing {}.{}() with arguments: {}", className, methodName, args);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();

            serviceLogger.info(" {}.{}() completed successfully in {} ms",
                    className, methodName, stopWatch.getTotalTimeMillis());

            return result;

        } catch (Exception e) {
            stopWatch.stop();
            serviceLogger.error("{}.{}() failed after {} ms with error: {}",
                    className, methodName, stopWatch.getTotalTimeMillis(), e.getMessage());
            throw e;
        }
    }
}
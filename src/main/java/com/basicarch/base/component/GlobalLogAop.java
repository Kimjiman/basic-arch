package com.basicarch.base.component;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 컨트롤러 로그 검증을 위한 AOP
 */
@Component
@Aspect
@Slf4j
public class GlobalLogAop {

    @Pointcut("execution(* com.basicarch..controller..*(..))")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object logInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String params = Arrays.toString(joinPoint.getArgs());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            log.info("{}.{}({}) - {}ms", className, methodName, params, stopWatch.getTotalTimeMillis());
            return result;
        } catch (Throwable e) {
            stopWatch.stop();
            log.error("{}.{}({}) - {}ms [ERROR: {}]", className, methodName, params, stopWatch.getTotalTimeMillis(), e.getMessage());
            throw e;
        }
    }
}

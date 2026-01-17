package com.doubles.selfstudy.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class UserAccountFetchTimeLogAOP {

    @Pointcut("execution(* com.doubles.selfstudy.utils.ServiceUtils.getUserAccountOrException(..))")
    private void userAccountFetch() {}

    @Around("userAccountFetch()")
    public Object stopWatch(ProceedingJoinPoint joinPoint) throws Throwable {
        // 측정 시작
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            // 종료 및 로그 출력
            stopWatch.stop();
            Object userId = joinPoint.getArgs()[0];
            log.info("User ID: {} | 소요 시간: {}ms", userId, stopWatch.getTotalTimeMillis());
        }
    }
}

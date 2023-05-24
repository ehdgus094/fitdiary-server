package im.fitdiary.fitdiaryserver.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class BaseMethodLoggingAspect {

    @Pointcut("@annotation(im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging)")
    private void methodPointcut() {}

    @Pointcut("@within(im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging)")
    private void classPointcut() {}

    @Around("methodPointcut() || classPointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("{}.{} call with param: {}",
                joinPoint.getSignature().toShortString().split("\\.")[0],
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
        );
        Object result = joinPoint.proceed();
        log.debug("{}.{} ends with return value: {}",
                joinPoint.getSignature().toShortString().split("\\.")[0],
                joinPoint.getSignature().getName(),
                result
        );
        return result;
    }
}

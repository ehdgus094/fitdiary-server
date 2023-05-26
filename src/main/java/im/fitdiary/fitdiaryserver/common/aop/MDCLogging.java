package im.fitdiary.fitdiaryserver.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@Order(-1)
public class MDCLogging {

    @Pointcut("@annotation(im.fitdiary.fitdiaryserver.common.aop.annotation.MDCLogging)")
    private void methodPointcut() {}

    @Pointcut("@within(im.fitdiary.fitdiaryserver.common.aop.annotation.MDCLogging)")
    private void classPointcut() {}

    @Around("methodPointcut() || classPointcut()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            String uuid = UUID.randomUUID().toString().split("-")[0];
            MDC.put("traceId", uuid);
            return joinPoint.proceed();
        } finally {
            MDC.clear();
        }
    }
}

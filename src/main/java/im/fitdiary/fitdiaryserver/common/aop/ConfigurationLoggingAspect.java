package im.fitdiary.fitdiaryserver.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ConfigurationLoggingAspect {

    @Before("@within(im.fitdiary.fitdiaryserver.common.aop.annotation.ConfigurationLogging)")
    public void execute(JoinPoint joinPoint) {
        log.debug("{}.{} configured",
                joinPoint.getSignature().toShortString().split("\\.")[0],
                joinPoint.getSignature().getName()
        );
    }
}

package im.fitdiary.fitdiaryserver.common.kafka.annotation;

import org.springframework.retry.annotation.Backoff;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KafkaFailStrategyListener {

    String topic();

    String groupId();

    String errorHandler() default "kafkaErrorHandler";

    String attempts() default "3";

    Backoff backoff() default @Backoff;
}

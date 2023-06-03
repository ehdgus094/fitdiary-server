package im.fitdiary.server.auth.event;

import im.fitdiary.server.auth.service.AuthUserService;
import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.server.common.aop.annotation.MDCLogging;
import im.fitdiary.server.common.kafka.annotation.KafkaConsumer;
import im.fitdiary.server.common.kafka.annotation.KafkaFailStrategyListener;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@MDCLogging
@RequiredArgsConstructor
@KafkaConsumer
public class AuthConsumer {

    private final AuthUserService authUserService;

    private static final String TOPIC_USER_DELETED = "userDeleted";

    private static final String GROUP_ID = "auth";

    @Transactional
    @KafkaFailStrategyListener(
            topic = TOPIC_USER_DELETED,
            groupId = GROUP_ID,
            errorHandler = "kafkaErrorHandler",
            attempts = "5",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void userDeleted(Long userId, Acknowledgment acknowledgment) {
        authUserService.deleteByUserId(userId);
        acknowledgment.acknowledge();
    }
}
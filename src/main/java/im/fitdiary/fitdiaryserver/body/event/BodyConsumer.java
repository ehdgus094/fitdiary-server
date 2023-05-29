package im.fitdiary.fitdiaryserver.body.event;

import im.fitdiary.fitdiaryserver.body.service.BodyLogService;
import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.common.aop.annotation.MDCLogging;
import im.fitdiary.fitdiaryserver.common.kafka.annotation.KafkaConsumer;
import im.fitdiary.fitdiaryserver.common.kafka.annotation.KafkaFailStrategyListener;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@MDCLogging
@RequiredArgsConstructor
@KafkaConsumer
public class BodyConsumer {

    private final BodyLogService bodyLogService;

    private static final String TOPIC_USER_DELETED = "userDeleted";

    private static final String GROUP_ID = "body";

    @Transactional
    @KafkaFailStrategyListener(
            topic = TOPIC_USER_DELETED,
            groupId = GROUP_ID,
            errorHandler = "kafkaErrorHandler",
            attempts = "5",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void userDeleted(Long userId, Acknowledgment acknowledgment) {
        bodyLogService.deleteByUserId(userId);
        acknowledgment.acknowledge();
    }
}
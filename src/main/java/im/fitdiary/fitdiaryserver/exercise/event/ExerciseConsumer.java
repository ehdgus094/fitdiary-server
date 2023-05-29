package im.fitdiary.fitdiaryserver.exercise.event;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.common.aop.annotation.MDCLogging;
import im.fitdiary.fitdiaryserver.common.kafka.annotation.KafkaConsumer;
import im.fitdiary.fitdiaryserver.common.kafka.annotation.KafkaFailStrategyListener;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseLogService;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@MDCLogging
@RequiredArgsConstructor
@KafkaConsumer
public class ExerciseConsumer {

    private final ExerciseService exerciseService;

    private final ExerciseLogService exerciseLogService;

    private static final String TOPIC_USER_DELETED = "userDeleted";

    private static final String GROUP_ID = "exercise";

    @Transactional
    @KafkaFailStrategyListener(
            topic = TOPIC_USER_DELETED,
            groupId = GROUP_ID,
            errorHandler = "kafkaErrorHandler",
            attempts = "5",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void userDeleted(Long userId, Acknowledgment acknowledgment) {
        exerciseLogService.deleteByUserId(userId);
        exerciseService.deleteByUserId(userId);
        acknowledgment.acknowledge();
    }
}
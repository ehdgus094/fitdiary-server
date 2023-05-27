package im.fitdiary.fitdiaryserver.exercise.event;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import im.fitdiary.fitdiaryserver.common.aop.annotation.MDCLogging;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseLogService;
import im.fitdiary.fitdiaryserver.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@BaseMethodLogging
@MDCLogging
@RequiredArgsConstructor
@Service
public class ExerciseConsumer {

    /**
     * 에러 처리:
     * consumer retry는 병렬로 처리되기때문에
     * 지속적인 장애 발생 시 consumer에 스레드가 몰릴 수 있어
     * consumer용 서버를 별도로 구성했다고 가정했습니다.
     * 1. 최초 1회 이벤트 처리 실패
     * 2. groupId를 포함한 별도의 topic에 실패 이벤트 저장
     * 3. 실패 이벤트를 최대 5회까지 재시도
     * 4. 최종 실패 시 서버에 일시적인 문제가 아닌 큰 장애가 생겼다고 가정하고 문제 해결 후 수동으로 처리
     */

    private final ExerciseService exerciseService;

    private final ExerciseLogService exerciseLogService;

    private static final String TOPIC_USER_DELETED = "userDeleted";

    private static final String GROUP_ID = "exercise";

    @Transactional
    @KafkaListener(topics = TOPIC_USER_DELETED, groupId = GROUP_ID, errorHandler = "kafkaErrorHandler")
    public void userDeleted(Long userId, Acknowledgment acknowledgment) {
        exerciseLogService.deleteByUserId(userId);
        exerciseService.deleteByUserId(userId);
        acknowledgment.acknowledge();
    }

    @Transactional
    @KafkaListener(topics = TOPIC_USER_DELETED + "-" + GROUP_ID + "-fail", groupId = GROUP_ID)
    @RetryableTopic(
            backoff = @Backoff(delay = 2000, multiplier = 2),
            attempts = "5"
    )
    public void userDeletedFail(Long userId, Acknowledgment acknowledgment) {
        userDeleted(userId, acknowledgment);
    }

    @Transactional
    @KafkaListener(topics = TOPIC_USER_DELETED + "-" + GROUP_ID + "-fail-dlt", groupId = GROUP_ID)
    public void userDeletedFailDlt(Long userId, Acknowledgment acknowledgment) {
        // 여기까지 왔다면 서버에 일시적인 문제가 아닌 큰 장애가 생겼다고 가정하고 문제 해결 후 수동으로 처리
        userDeleted(userId, acknowledgment);
    }
}

package im.fitdiary.fitdiaryserver.user.event;

import im.fitdiary.fitdiaryserver.common.aop.annotation.BaseMethodLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@BaseMethodLogging
@RequiredArgsConstructor
@Service
public class UserProducerImpl implements UserProducer {

    private final KafkaTemplate<String, Object> template;

    private static final String TOPIC_USER_DELETED = "userDeleted";

    public void userDeleted(Long userId) {
        template.send(TOPIC_USER_DELETED, userId);
    }
}

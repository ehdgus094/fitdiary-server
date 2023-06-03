package im.fitdiary.server.common.kafka;

import im.fitdiary.server.common.aop.annotation.BaseMethodLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@BaseMethodLogging
@RequiredArgsConstructor
@Component
public class KafkaErrorHandler implements KafkaListenerErrorHandler {

    private final KafkaTemplate<String, Object> template;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        return null;
    }

    @Override
    public Object handleError(
            Message<?> message,
            ListenerExecutionFailedException exception,
            Consumer<?, ?> consumer
    ) {
        String topic = (String) message.getHeaders().get("kafka_receivedTopic");
        String groupId = (String) message.getHeaders().get("kafka_groupId");
        log.warn("[topic={}, groupId={}] consumer failed reason: {}", topic, groupId, exception.getMessage());
        template.send(topic + "-" + groupId + "-fail", message.getPayload());
        consumer.commitSync();
        return null;
    }
}

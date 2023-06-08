package im.fitdiary.server.common.kafka.builder;

import java.lang.reflect.Method;

public interface KafkaListenerBuilder {

    void addListener(Method method);

    Class<?> load();

    Class<?> getConsumerClass();

    boolean isGenerated();
}

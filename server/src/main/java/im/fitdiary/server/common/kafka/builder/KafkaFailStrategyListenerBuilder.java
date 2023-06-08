package im.fitdiary.server.common.kafka.builder;

import im.fitdiary.server.common.kafka.annotation.KafkaFailStrategyListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;

import java.lang.reflect.Method;

@Slf4j
public class KafkaFailStrategyListenerBuilder implements KafkaListenerBuilder {

    /**
     * 에러 처리:
     * consumer retry는 병렬로 처리되기때문에
     * 지속적인 장애 발생 시 consumer에 스레드가 몰릴 수 있어
     * consumer용 서버를 별도로 구성했다고 가정했습니다.
     * 1. 최초 1회 이벤트 처리 실패 (InitialListener)
     * 2. groupId를 포함한 별도의 topic에 실패 이벤트 저장
     * 3. 실패 이벤트를 최대 5회까지 재시도 (FailListener)
     * 4. 최종 실패 시 서버에 일시적인 문제가 아닌 큰 장애가 생겼다고 가정하고 문제 해결 후 수동으로 처리 (FinalListener)
     */

    private Builder<?> builder;

    @Getter
    private final Class<?> consumerClass;

    private static final Class<KafkaFailStrategyListener> targetAnnotation = KafkaFailStrategyListener.class;

    private static final String FIELD_NAME = "consumer";

    @Getter
    private boolean generated = false;

    public KafkaFailStrategyListenerBuilder(Class<?> consumerClass) {
        try {
            builder = new ByteBuddy()
                    .subclass(Object.class, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                    .name(consumerClass.getName() + "$FailStrategyListener")
                    .defineField(FIELD_NAME, consumerClass, Visibility.PRIVATE, FieldManifestation.FINAL)
                    .defineConstructor(Visibility.PUBLIC)
                    .withParameters(consumerClass)
                    .intercept(MethodCall
                            .invoke(Object.class.getConstructor())
                            .andThen(FieldAccessor
                                    .ofField(FIELD_NAME)
                                    .setsArgumentAt(0)
                            )
                    );
        } catch (NoSuchMethodException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        this.consumerClass = consumerClass;
    }

    public void addListener(Method method) {
        KafkaFailStrategyListener annotation = method.getAnnotation(targetAnnotation);
        if (annotation != null) {
            addInitialListener(method, annotation);
            addFailListener(method, annotation);
            addFinalListener(method, annotation);
            generated = true;
        }
    }

    private void addInitialListener(Method method, KafkaFailStrategyListener failStrategyListener) {
        String[] topic = {failStrategyListener.topic()};
        AnnotationDescription listener = AnnotationDescription.Builder
                .ofType(KafkaListener.class)
                .defineArray("topics", topic)
                .define("groupId", failStrategyListener.groupId())
                .define("errorHandler", failStrategyListener.errorHandler())
                .build();

        builder = builder.defineMethod(
                method.getName() + "Initial",
                        method.getReturnType(),
                        method.getModifiers()
                )
                .withParameters(method.getParameterTypes())
                .intercept(MethodCall
                        .invoke(method)
                        .onField(FIELD_NAME)
                        .withAllArguments()
                )
                .annotateMethod(listener);
    }

    private void addFailListener(Method method, KafkaFailStrategyListener failStrategyListener) {
        String[] topic = {failStrategyListener.topic() + "-" + failStrategyListener.groupId() + "-fail"};
        AnnotationDescription listener = AnnotationDescription.Builder
                .ofType(KafkaListener.class)
                .defineArray("topics", topic)
                .define("groupId", failStrategyListener.groupId())
                .build();
        AnnotationDescription retryableTopic = AnnotationDescription.Builder
                .ofType(RetryableTopic.class)
                .define("attempts", failStrategyListener.attempts())
                .define("backoff", failStrategyListener.backoff())
                .build();

        builder = builder.defineMethod(
                method.getName() + "Fail",
                        method.getReturnType(),
                        method.getModifiers()
                )
                .withParameters(method.getParameterTypes())
                .intercept(MethodCall
                        .invoke(method)
                        .onField(FIELD_NAME)
                        .withAllArguments()
                )
                .annotateMethod(listener)
                .annotateMethod(retryableTopic);
    }

    private void addFinalListener(Method method, KafkaFailStrategyListener failStrategyListener) {
        String[] topic = {failStrategyListener.topic() + "-" + failStrategyListener.groupId() + "-fail-dlt"};
        AnnotationDescription listener = AnnotationDescription.Builder
                .ofType(KafkaListener.class)
                .defineArray("topics", topic)
                .define("groupId", failStrategyListener.groupId())
                .build();

        builder = builder
                .defineMethod(
                        method.getName() + "Final",
                        method.getReturnType(),
                        method.getModifiers()
                )
                .withParameters(method.getParameterTypes())
                .intercept(MethodCall
                        .invoke(method)
                        .onField(FIELD_NAME)
                        .withAllArguments()
                )
                .annotateMethod(listener);
    }

    // 바이트코드 확인용
//    public void saveFile() throws IOException {
//        builder.make().saveIn(new File("src/main/bytebuddy"));
//        log.debug("[{}] file saved", bean.getName());
//    }

    public Class<?> load() {
        Class<?> loaded = builder.make().load(getClass().getClassLoader()).getLoaded();
        log.debug("[{}] ListenerClass for @{} generated",
                consumerClass.getSimpleName(),
                targetAnnotation.getSimpleName()
        );
        return loaded;
    }
}

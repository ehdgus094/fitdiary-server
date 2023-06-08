package im.fitdiary.server.common.kafka;

import im.fitdiary.server.common.kafka.annotation.KafkaConsumer;
import im.fitdiary.server.common.kafka.builder.KafkaFailStrategyListenerBuilder;
import im.fitdiary.server.common.kafka.builder.KafkaListenerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class KafkaProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Arrays.stream(beanFactory.getBeanNamesForAnnotation(KafkaConsumer.class))
                .forEach(beanName -> {
                    Class<?> consumerClass = getClass(beanFactory.getBeanDefinition(beanName));

                    generateListener(new KafkaFailStrategyListenerBuilder(consumerClass))
                            .ifPresent(listenerClass ->
                                    registerBeanDefinition(beanFactory, listenerClass)
                            );
                });
    }

    private Class<?> getClass(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    private Optional<Class<?>> generateListener(KafkaListenerBuilder builder) {
        Arrays.stream(builder.getConsumerClass().getDeclaredMethods())
                .forEach(builder::addListener);
        return builder.isGenerated() ? Optional.of(builder.load()) : Optional.empty();
    }

    private void registerBeanDefinition(
            ConfigurableListableBeanFactory beanFactory,
            Class<?> registerClass
    ) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(registerClass);
        ((DefaultListableBeanFactory) beanFactory)
                .registerBeanDefinition(registerClass.getName(), beanDefinition);
    }
}

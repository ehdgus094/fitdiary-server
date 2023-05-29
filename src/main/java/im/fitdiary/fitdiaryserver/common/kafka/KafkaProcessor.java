package im.fitdiary.fitdiaryserver.common.kafka;

import im.fitdiary.fitdiaryserver.common.kafka.annotation.KafkaConsumer;
import im.fitdiary.fitdiaryserver.common.kafka.annotation.KafkaFailStrategyListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class KafkaProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Arrays.stream(beanFactory.getBeanNamesForAnnotation(KafkaConsumer.class))
                .forEach(beanName -> {
                    Class<?> consumerClass = getClass(beanFactory.getBeanDefinition(beanName));
                    generateFailStrategyListener(consumerClass)
                            .ifPresent(failStrategyListenerClass ->
                                registerBeanDefinition(beanFactory, failStrategyListenerClass)
                            );
                });
    }

    private Class<?> getClass(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Class<?>> generateFailStrategyListener(Class<?> consumerClass) {
        KafkaFailStrategyListenerBuilder builder =
                new KafkaFailStrategyListenerBuilder(consumerClass);

        Arrays.stream(consumerClass.getDeclaredMethods())
                .filter(method -> method.getAnnotation(KafkaFailStrategyListener.class) != null)
                .forEach(method -> builder.addListener(
                        method,
                        method.getAnnotation(KafkaFailStrategyListener.class)
                ));
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

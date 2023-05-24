package im.fitdiary.fitdiaryserver.config;

import im.fitdiary.fitdiaryserver.common.aop.annotation.ConfigurationLogging;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationLogging
@Configuration
public class MicrometerConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}

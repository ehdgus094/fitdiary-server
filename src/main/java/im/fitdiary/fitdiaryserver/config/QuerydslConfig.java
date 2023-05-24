package im.fitdiary.fitdiaryserver.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import im.fitdiary.fitdiaryserver.common.aop.annotation.ConfigurationLogging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@ConfigurationLogging
@Configuration
public class QuerydslConfig {

    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}

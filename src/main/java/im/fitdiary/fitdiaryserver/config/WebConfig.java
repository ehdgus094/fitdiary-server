package im.fitdiary.fitdiaryserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import im.fitdiary.fitdiaryserver.common.filter.MDCLoggingFilter;
import im.fitdiary.fitdiaryserver.common.interceptor.RequestInfoLoggingInterceptor;
import im.fitdiary.fitdiaryserver.exception.filter.HideErrorMessageFilter;
import im.fitdiary.fitdiaryserver.security.argumentresolver.PrincipalArgumentResolver;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInfoLoggingInterceptor())
                .order(1)
                .addPathPatterns("/**");
    }

    @PostConstruct
    public void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @Bean
    @Profile("prod")
    public FilterRegistrationBean<HideErrorMessageFilter> hideErrorMessageFilter(ObjectMapper mapper) {
        FilterRegistrationBean<HideErrorMessageFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new HideErrorMessageFilter(mapper));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<MDCLoggingFilter> mdcLoggingFilter() {
        FilterRegistrationBean<MDCLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new MDCLoggingFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.modules(new JsonNullableModule(), new JavaTimeModule());
    }
}

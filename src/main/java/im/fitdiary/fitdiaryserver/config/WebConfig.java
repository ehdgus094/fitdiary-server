package im.fitdiary.fitdiaryserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import im.fitdiary.fitdiaryserver.exception.filter.ErrorMessageFilter;
import im.fitdiary.fitdiaryserver.security.argumentresolver.PrincipalArgumentResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PrincipalArgumentResolver());
    }

    @Bean
    public FilterRegistrationBean<Filter> errorMessageFilter(ConfigProperties properties, ObjectMapper mapper) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ErrorMessageFilter(properties, mapper));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}

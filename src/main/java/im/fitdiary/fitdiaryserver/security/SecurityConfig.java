package im.fitdiary.fitdiaryserver.security;

import im.fitdiary.fitdiaryserver.security.exception.CustomAccessDeniedHandler;
import im.fitdiary.fitdiaryserver.security.exception.CustomAuthenticationEntryPoint;
import im.fitdiary.fitdiaryserver.security.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter filter;
    @Value("${spring.mvc.servlet.path}")
    private String baseUri;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST,
                            baseUri + "/user/login/**",
                            baseUri + "/user/email",
                            baseUri + "/user/kakao"
                    )
                        .permitAll()
                    .antMatchers(HttpMethod.POST, baseUri + "/user/refresh-token")
                        .hasRole(convert(RoleType.ROLE_USER_REFRESH))
                    .antMatchers(baseUri + "/user/**")
                        .hasRole(convert(RoleType.ROLE_USER_ACCESS))
                    .anyRequest()
                        .permitAll()
                .and()
                    // role mismatch
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    // not authenticated
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private String convert(RoleType roleType) {
        return roleType.toString().replace("ROLE_", "");
    }
}
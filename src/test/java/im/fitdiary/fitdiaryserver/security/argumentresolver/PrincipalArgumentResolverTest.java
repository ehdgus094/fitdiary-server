package im.fitdiary.fitdiaryserver.security.argumentresolver;

import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import im.fitdiary.fitdiaryserver.security.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PrincipalArgumentResolverTest {

    @Mock
    MethodParameter parameter;

    @Mock
    ModelAndViewContainer mavContainer;

    @Mock
    NativeWebRequest webRequest;

    @Mock
    WebDataBinderFactory binderFactory;

    PrincipalArgumentResolver resolver;

    @BeforeEach
    void init() {
        resolver = new PrincipalArgumentResolver();
    }

    @Test
    @DisplayName("supportsParameter")
    void supportsParameter() {
        // given
        given(parameter.hasParameterAnnotation(Auth.class))
                .willReturn(true);
        doReturn(AuthToken.class)
                .when(parameter).getParameterType();

        // when
        boolean result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("resolveArgument")
    void resolveArgument() throws Exception {
        // given
        Long id = 1L;
        RoleType roleType = RoleType.ROLE_USER_ACCESS;

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(roleType.toString()));
        CustomUserDetails userDetails = new CustomUserDetails(id.toString(), authorities);
        CustomAuthenticationToken authenticationToken =
                new CustomAuthenticationToken(userDetails.getAuthorities(), userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when
        AuthToken result = (AuthToken) resolver
                .resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.has(roleType)).isTrue();
    }
}
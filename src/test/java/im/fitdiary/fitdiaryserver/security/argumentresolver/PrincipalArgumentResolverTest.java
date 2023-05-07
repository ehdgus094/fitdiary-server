package im.fitdiary.fitdiaryserver.security.argumentresolver;

import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

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
        given(parameter.hasParameterAnnotation(UserId.class))
                .willReturn(true);
        doReturn(Long.class)
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
        String id = "1";
        CustomUserDetails userDetails = new CustomUserDetails(id, null);
        CustomAuthenticationToken authenticationToken =
                new CustomAuthenticationToken(userDetails.getAuthorities(), userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // when
        Object result =
                resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        // then
        assertThat(result)
                .isNotNull()
                .isEqualTo(Long.parseLong(id));
        assertThat(result.getClass()).isEqualTo(Long.class);
    }
}
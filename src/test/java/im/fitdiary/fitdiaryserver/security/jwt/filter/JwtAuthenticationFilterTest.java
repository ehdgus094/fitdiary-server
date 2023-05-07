package im.fitdiary.fitdiaryserver.security.jwt.filter;

import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.CustomAuthenticationToken;
import im.fitdiary.fitdiaryserver.security.CustomUserDetails;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.security.jwt.handler.JwtHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    JwtHandler jwtHandler;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain chain;
    @InjectMocks
    JwtAuthenticationFilter filter;

    @Test
    @DisplayName("doFilterInternal success")
    void doFilterInternalSuccess() throws Exception {
        // given
        String token = "token";
        String subject = "1";
        RoleType roleType = RoleType.ROLE_USER_ACCESS;
        given(request.getHeader("Authorization"))
                .willReturn(token);
        given(jwtHandler.getSubject(token))
                .willReturn(subject);
        given(jwtHandler.getRoleType(token))
                .willReturn(roleType);

        // when
        filter.doFilterInternal(request, response, chain);
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // then
        assertThat(authentication).isInstanceOf(CustomAuthenticationToken.class);
        assertThat(((CustomUserDetails) authentication.getPrincipal()).getUsername())
                .isEqualTo(subject);
    }

    @Test
    @DisplayName("doFilterInternal fail")
    void doFilterInternalFail() throws Exception {
        // given
        String wrongToken = "wrongToken";
        given(request.getHeader("Authorization"))
                .willReturn(wrongToken);
        given(jwtHandler.getSubject(wrongToken))
                .willThrow(UnauthorizedException.class);

        // when
        filter.doFilterInternal(request, response, chain);
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // then
        assertThat(authentication).isNull();
    }
}
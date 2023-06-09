package im.fitdiary.server.security.jwt.filter;

import im.fitdiary.server.exception.e401.UnauthorizedException;
import im.fitdiary.server.security.CustomAuthenticationToken;
import im.fitdiary.server.security.CustomUserDetails;
import im.fitdiary.server.security.RoleType;
import im.fitdiary.server.security.jwt.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("doFilterInternal")
    class DoFilterInternal {

        String token;

        String subject;

        RoleType roleType;

        @BeforeEach
        void init() {
            SecurityContextHolder.getContext().setAuthentication(null);
            token = "token";
            subject = "subject";
            roleType = RoleType.ROLE_USER_ACCESS;
        }

        @Test
        @DisplayName("fail")
        void fail() throws Exception {
            // given
            given(request.getHeader("Authorization"))
                    .willReturn(token);
            given(jwtHandler.getSubject(token))
                    .willThrow(UnauthorizedException.class);

            // when
            filter.doFilterInternal(request, response, chain);
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            // then
            assertThat(authentication).isNull();
        }

        @Test
        @DisplayName("success")
        void success() throws Exception {
            // given
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
    }
}
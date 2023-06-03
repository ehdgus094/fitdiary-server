package im.fitdiary.server.security.jwt.handler;

import im.fitdiary.server.config.ConfigProperties;
import im.fitdiary.server.exception.e401.UnauthorizedException;
import im.fitdiary.server.security.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JwtHandlerImplTest {

    @InjectMocks
    JwtHandlerImpl jwtHandler;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ConfigProperties properties;

    @Nested
    @DisplayName("createToken")
    class CreateToken {
        String subject;

        @BeforeEach
        void init() {
            subject = "subject";
        }

        @Test
        @DisplayName("userAccessToken")
        void userAccessToken() {
            // given
            given(properties.getJwt().getUser().getAccess().getSecret())
                    .willReturn("secret");
            given(properties.getJwt().getUser().getAccess().getMaxAge())
                    .willReturn(100L);

            // when
            String token = jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, subject);

            // then
            assertThat(token)
                    .isNotNull()
                    .isNotEqualTo("")
                    .contains(jwtHandler.getType());
        }

        @Test
        @DisplayName("userRefreshToken")
        void userRefreshToken() {
            given(properties.getJwt().getUser().getRefresh().getSecret())
                    .willReturn("secret");
            given(properties.getJwt().getUser().getRefresh().getMaxAge())
                    .willReturn(100L);

            // when
            String token = jwtHandler.createToken(RoleType.ROLE_USER_REFRESH, subject);

            // then
            assertThat(token)
                    .isNotNull()
                    .isNotEqualTo("")
                    .contains(jwtHandler.getType());
        }
    }

    @Nested
    @DisplayName("getSubject")
    class GetSubject {

        String subject;

        String token;

        @BeforeEach
        void init() {
            given(properties.getJwt().getUser().getAccess().getSecret())
                    .willReturn("secret");
            given(properties.getJwt().getUser().getAccess().getMaxAge())
                    .willReturn(100L);
            subject = "subject";
            token = jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, subject);
        }

        @Test
        @DisplayName("success")
        void success() {
            // when
            String foundSubject = jwtHandler.getSubject(token);

            // then
            assertThat(foundSubject).isEqualTo(subject);
        }

        @Test
        @DisplayName("fail_wrongToken")
        void fail_wrongToken() {
            // given
            String wrongToken = "wrongToken";

            // when - then
            assertThatThrownBy(() ->
                    jwtHandler.getSubject(wrongToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_null")
        void fail_null() {
            // when - then
            assertThatThrownBy(() ->
                    jwtHandler.getSubject(null)
            ).isInstanceOf(UnauthorizedException.class);
        }
    }

    @Nested
    @DisplayName("getRoleType")
    class GetRoleType {

        RoleType roleType;

        String token;

        @BeforeEach
        void init() {
            given(properties.getJwt().getUser().getAccess().getSecret())
                    .willReturn("secret");
            given(properties.getJwt().getUser().getAccess().getMaxAge())
                    .willReturn(100L);
            roleType = RoleType.ROLE_USER_ACCESS;
            String subject = "subject";
            token = jwtHandler.createToken(roleType, subject);
        }

        @Test
        @DisplayName("success")
        void success() {
            // when
            RoleType foundRoleType = jwtHandler.getRoleType(token);

            // then
            assertThat(foundRoleType).isEqualTo(roleType);
        }

        @Test
        @DisplayName("fail_wrongToken")
        void fail_wrongToken() {
            // given
            String wrongToken = "wrongToken";

            // when - then
            assertThatThrownBy(() ->
                    jwtHandler.getRoleType(wrongToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_null")
        void fail_null() {
            // when - then
            assertThatThrownBy(() ->
                    jwtHandler.getRoleType(null)
            ).isInstanceOf(UnauthorizedException.class);
        }
    }

    @Nested
    @DisplayName("getExpiration")
    class GetExpiration {

        String create(long maxAge) {
            given(properties.getJwt().getUser().getAccess().getSecret())
                    .willReturn("secret");
            given(properties.getJwt().getUser().getAccess().getMaxAge())
                    .willReturn(maxAge);
            return jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, "subject");
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
            String token = create(1000L);

            // when
            LocalDateTime expiration = jwtHandler.getExpiration(token);

            // then
            assertThat(expiration).isAfter(LocalDateTime.now());
        }

        @Test
        @DisplayName("fail_wrongToken")
        void fail_wrongToken() {
            // given
            create(1000L);
            String wrongToken = "wrongToken";

            // when - then
            assertThatThrownBy(() ->
                    jwtHandler.getExpiration(wrongToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_null")
        void fail_null() {
            // given
            create(1000L);

            // when - then
            assertThatThrownBy(() ->
                    jwtHandler.getExpiration(null)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_expired")
        void fail_expired() {
            // given
            String token = create(0L);

            // when - then
            assertThatThrownBy(() ->
                    jwtHandler.getExpiration(token)
            ).isInstanceOf(UnauthorizedException.class);
        }
    }
}
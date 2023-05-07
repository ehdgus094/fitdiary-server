package im.fitdiary.fitdiaryserver.security.jwt.handler;

import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.util.factory.configproperties.ConfigPropertiesFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class JwtHandlerImplTest {

    JwtHandlerImpl jwtHandler;
    ConfigProperties properties;

    @BeforeEach
    void init() {
        properties = ConfigPropertiesFactory.create();
        jwtHandler = new JwtHandlerImpl(properties);
    }

    @Nested
    @DisplayName("createToken")
    class CreateToken {
        private final String SUBJECT = "subject";

        @Test
        @DisplayName("userAccessToken")
        void userAccessToken() {
            // when
            String token = jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, SUBJECT);

            // then
            assertThat(token)
                    .isNotNull()
                    .isNotEqualTo("")
                    .contains(jwtHandler.getType());
        }

        @Test
        @DisplayName("userRefreshToken")
        void userRefreshToken() {
            // when
            String token = jwtHandler.createToken(RoleType.ROLE_USER_REFRESH, SUBJECT);

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

        private final String SUBJECT = "subject";
        private String token;

        @BeforeEach
        void create() {
            token = jwtHandler.createToken(RoleType.ROLE_USER_ACCESS, SUBJECT);
        }

        @Test
        @DisplayName("success")
        void success() {
            // when
            String foundSubject = jwtHandler.getSubject(token);

            // then
            assertThat(foundSubject).isEqualTo(SUBJECT);
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

        private final RoleType roleType = RoleType.ROLE_USER_ACCESS;
        private String token;

        @BeforeEach
        void create() {
            token = jwtHandler.createToken(roleType, "subject");
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

        private String create(Long maxAge) {
            properties.getJwt().getUser().getAccess().setMaxAge(maxAge);
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
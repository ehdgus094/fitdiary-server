package im.fitdiary.fitdiaryserver.security.jwt.service;

import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.jwt.model.RoleType;
import im.fitdiary.fitdiaryserver.util.factory.configproperties.ConfigPropertiesFactory;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    JwtServiceImpl jwtService;
    ConfigProperties properties;

    @BeforeEach
    void init() {
        properties = ConfigPropertiesFactory.create();
        jwtService = new JwtServiceImpl(properties);
    }

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        // given
        String subject = "subject";

        // when
        String userAccessToken = jwtService.createToken(RoleType.ROLE_USER_ACCESS, subject);
        String userRefreshToken = jwtService.createToken(RoleType.ROLE_USER_REFRESH, subject);

        // then
        assertThat(userAccessToken)
                .isNotNull()
                .isNotEqualTo("")
                .contains(jwtService.getType());

        assertThat(userRefreshToken)
                .isNotNull()
                .isNotEqualTo("")
                .contains(jwtService.getType());
    }

    @Test
    @DisplayName("토큰 추출")
    void extract() {
        // given
        String subject = "subject";
        String wrongToken = "wrongToken";

        String userAccessToken = jwtService.createToken(RoleType.ROLE_USER_ACCESS, subject);

        properties.getJwt().getUser().getAccess().setMaxAge(0L);
        String userAccessTokenExpired =
                jwtService.createToken(RoleType.ROLE_USER_ACCESS, subject);

        String userRefreshToken = jwtService.createToken(RoleType.ROLE_USER_REFRESH, subject);

        // when
        Optional<Claims> userAccessClaims = jwtService.extract(userAccessToken);
        Optional<Claims> userRefreshClaims = jwtService.extract(userRefreshToken);

        // then
        assertThat(userAccessClaims).isPresent();
        assertThat(userAccessClaims.get().getSubject()).isEqualTo(subject);
        assertThat(userAccessClaims.get().getAudience()).isEqualTo(RoleType.ROLE_USER_ACCESS.toString());
        assertThat(jwtService.extract(null)).isNotPresent();
        assertThat(jwtService.extract(userAccessTokenExpired)).isNotPresent();
        assertThat(jwtService.extract(wrongToken)).isNotPresent();

        properties.getJwt().getUser().getAccess().setSecret("wrongSecret");
        assertThat(jwtService.extract(userAccessToken)).isNotPresent();

        assertThat(userRefreshClaims).isPresent();
        assertThat(userRefreshClaims.get().getSubject()).isEqualTo(subject);
        assertThat(userRefreshClaims.get().getAudience()).isEqualTo(RoleType.ROLE_USER_REFRESH.toString());
    }

    @Test
    @DisplayName("만료일 추출")
    void getExpiration() {
        // given
        String subject = "subject";
        String wrongToken = "wrongToken";

        String refreshToken = jwtService.createToken(RoleType.ROLE_USER_REFRESH, subject);

        properties.getJwt().getUser().getRefresh().setMaxAge(0L);
        String refreshTokenExpired =
                jwtService.createToken(RoleType.ROLE_USER_REFRESH, subject);

        // when
        LocalDateTime expiration = jwtService.getExpiration(refreshToken);

        // then
        assertThat(expiration).isAfter(LocalDateTime.now());
        assertThatThrownBy(() ->
                jwtService.getExpiration(wrongToken)
        ).isInstanceOf(UnauthorizedException.class);
        assertThatThrownBy(() ->
                jwtService.getExpiration(refreshTokenExpired)
        ).isInstanceOf(UnauthorizedException.class);
    }
}
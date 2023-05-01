package im.fitdiary.fitdiaryserver.security.jwt.service;

import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.exception.UnauthorizedException;
import im.fitdiary.fitdiaryserver.security.jwt.model.RoleType;
import im.fitdiary.fitdiaryserver.util.factory.configproperties.ConfigPropertiesFactory;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService")
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
        final String subject = "subject";

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
        final String subject = "subject";
        final String wrongToken = "wrongToken";

        String userAccessToken = jwtService.createToken(RoleType.ROLE_USER_ACCESS, subject);

        properties.getJwt().getUser().getAccess().setMaxAge(0L);
        String userAccessTokenExpired =
                jwtService.createToken(RoleType.ROLE_USER_ACCESS, subject);

        String userRefreshToken = jwtService.createToken(RoleType.ROLE_USER_REFRESH, subject);

        // when
        Claims userAccessClaims = jwtService.extract(userAccessToken)
                .orElseThrow(UnauthorizedException::new);
        Claims userRefreshClaims = jwtService.extract(userRefreshToken)
                .orElseThrow(UnauthorizedException::new);

        // then
        assertThat(userAccessClaims.getSubject()).isEqualTo(subject);
        assertThat(userAccessClaims.getAudience()).isEqualTo(RoleType.ROLE_USER_ACCESS.toString());
        assertThat(jwtService.extract(userAccessTokenExpired)).isNotPresent();
        assertThat(jwtService.extract(wrongToken)).isNotPresent();

        properties.getJwt().getUser().getAccess().setSecret("wrongSecret");
        assertThat(jwtService.extract(userAccessToken)).isNotPresent();

        assertThat(userRefreshClaims.getSubject()).isEqualTo(subject);
        assertThat(userRefreshClaims.getAudience()).isEqualTo(RoleType.ROLE_USER_REFRESH.toString());
    }
}
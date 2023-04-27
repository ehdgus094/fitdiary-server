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

import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(userAccessToken);
        assertNotEquals("", userAccessToken);
        assertTrue(userAccessToken.contains(jwtService.getType()));

        assertNotNull(userRefreshToken);
        assertNotEquals("", userRefreshToken);
        assertTrue(userRefreshToken.contains(jwtService.getType()));
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
        assertEquals(subject, userAccessClaims.getSubject());
        assertEquals(RoleType.ROLE_USER_ACCESS.toString(), userAccessClaims.getAudience());
        assertThrows(UnauthorizedException.class, () -> {
            jwtService.extract(userAccessTokenExpired)
                    .orElseThrow(UnauthorizedException::new);
        });
        assertThrows(UnauthorizedException.class, () -> {
            jwtService.extract(wrongToken)
                    .orElseThrow(UnauthorizedException::new);
        });
        assertThrows(UnauthorizedException.class, () -> {
            properties.getJwt().getUser().getAccess().setSecret("wrongSecret");
            jwtService.extract(userAccessToken)
                    .orElseThrow(UnauthorizedException::new);
        });

        assertEquals(subject, userRefreshClaims.getSubject());
        assertEquals(RoleType.ROLE_USER_REFRESH.toString(), userRefreshClaims.getAudience());
    }
}
package im.fitdiary.fitdiaryserver.auth.service;

import im.fitdiary.fitdiaryserver.auth.data.AuthUserRepository;
import im.fitdiary.fitdiaryserver.auth.data.entity.AuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.JwtToken;
import im.fitdiary.fitdiaryserver.auth.service.dto.CreateAuthUser;
import im.fitdiary.fitdiaryserver.auth.service.dto.LoginUser;
import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.AuthUserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.security.jwt.handler.JwtHandler;
import im.fitdiary.fitdiaryserver.util.factory.auth.AuthFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceImplTest {

    @Mock
    AuthUserRepository authUserRepository;
    @Mock
    JwtHandler jwtHandler;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    AuthUserServiceImpl authUserService;

    @Nested
    @DisplayName("createUser")
    class CreateUser {

        @Nested
        @DisplayName("email")
        class Email {

            private CreateAuthUser createAuthUser;
            private AuthUser authUser;
            private String encodedPassword;

            @BeforeEach
            void init() {
                createAuthUser = AuthFactory.createEmailAuthUser();
                authUser = AuthFactory.emailUser();
                encodedPassword = "encodedPassword";
            }

            @Test
            @DisplayName("fail_duplicated")
            void fail_duplicated() {
                // given
                given(authUserRepository.findByLoginIdAndLoginTypeOrUserId(
                        createAuthUser.getLoginId(),
                        createAuthUser.getLoginType(),
                        createAuthUser.getUserId()
                )).willReturn(Optional.of(authUser));

                // when - then
                assertThatThrownBy(() ->
                        authUserService.create(createAuthUser)
                ).isInstanceOf(AuthUserDuplicatedException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);

                // when
                AuthUser createdAuthUser = authUserService.create(createAuthUser);

                // then
                assertThat(createdAuthUser.getLoginId()).isEqualTo(createAuthUser.getLoginId());
                assertThat(createdAuthUser.getLoginType()).isEqualTo(createAuthUser.getLoginType());
                assertThat(createdAuthUser.getPassword()).isEqualTo(encodedPassword);
                assertThat(createdAuthUser.getRefreshToken()).isNull();
            }
        }

        @Nested
        @DisplayName("kakao")
        class Kakao {

            private CreateAuthUser createAuthUser;
            private AuthUser authUser;

            @BeforeEach
            void init() {
                createAuthUser = AuthFactory.createKakaoAuthUser();
                authUser = AuthFactory.kakaoUser();
            }

            @Test
            @DisplayName("fail_duplicated")
            void fail_duplicated() {
                // given
                given(authUserRepository.findByLoginIdAndLoginTypeOrUserId(
                        createAuthUser.getLoginId(),
                        createAuthUser.getLoginType(),
                        createAuthUser.getUserId()
                )).willReturn(Optional.of(authUser));

                // when - then
                assertThatThrownBy(() ->
                        authUserService.create(createAuthUser)
                ).isInstanceOf(AuthUserDuplicatedException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // when
                AuthUser createdAuthUser = authUserService.create(createAuthUser);

                // then
                verify(passwordEncoder, never()).encode(anyString());
                assertThat(createdAuthUser.getLoginId()).isEqualTo(createAuthUser.getLoginId());
                assertThat(createdAuthUser.getLoginType()).isEqualTo(createAuthUser.getLoginType());
                assertThat(createdAuthUser.getPassword()).isNull();
                assertThat(createdAuthUser.getRefreshToken()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("loginUser")
    class LoginAuthUser {

        @Nested
        @DisplayName("email")
        class Email {

            private LoginUser loginUser;
            private String password;
            private AuthUser authUser;
            private String token;

            @BeforeEach
            void init() {
                loginUser = AuthFactory.loginEmailUser();
                password = (String) getField(loginUser, "password");
                authUser = AuthFactory.emailUser();
                setField(authUser, "id", 1L);
                token = "token";
            }

            @Test
            @DisplayName("fail_wrongId")
            void fail_wrongId() {
                // when - then
                assertThatThrownBy(() ->
                        authUserService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("fail_wrongPassword")
            void fail_wrongPassword() {
                // given
                given(authUserRepository.findByLoginIdAndLoginType(
                        loginUser.getLoginId(),
                        loginUser.getLoginType()
                )).willReturn(Optional.of(authUser));
                given(passwordEncoder.matches(password, authUser.getPassword()))
                        .willReturn(false);

                // when - then
                assertThatThrownBy(() ->
                        authUserService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                given(authUserRepository.findByLoginIdAndLoginType(
                        loginUser.getLoginId(),
                        loginUser.getLoginType()
                )).willReturn(Optional.of(authUser));
                given(passwordEncoder.matches(password, authUser.getPassword()))
                        .willReturn(true);
                given(jwtHandler.createToken(any(), anyString())).willReturn(token);

                // when
                JwtToken jwtToken = authUserService.login(loginUser);

                // then
                assertThat(jwtToken.getAccessToken()).isEqualTo(token);
                assertThat(jwtToken.getRefreshToken()).isEqualTo(token);
                assertThat(authUser.getRefreshToken()).isEqualTo(token);
            }
        }

        @Nested
        @DisplayName("kakao")
        class Kakao {

            private LoginUser loginUser;
            private AuthUser authUser;
            private String token;

            @BeforeEach
            void init() {
                loginUser = AuthFactory.loginKakaoUser();
                authUser = AuthFactory.kakaoUser();
                setField(authUser, "id", 1L);
                token = "token";
            }

            @Test
            @DisplayName("fail_wrongId")
            void fail_wrongId() {
                // when - then
                assertThatThrownBy(() ->
                        authUserService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                given(authUserRepository.findByLoginIdAndLoginType(
                        loginUser.getLoginId(),
                        loginUser.getLoginType()
                )).willReturn(Optional.of(authUser));
                given(jwtHandler.createToken(any(), anyString())).willReturn(token);

                // when
                JwtToken jwtToken = authUserService.login(loginUser);

                // then
                verify(passwordEncoder, never()).matches(anyString(), anyString());
                assertThat(jwtToken.getAccessToken()).isEqualTo(token);
                assertThat(jwtToken.getRefreshToken()).isEqualTo(token);
                assertThat(authUser.getRefreshToken()).isEqualTo(token);
            }
        }
    }

    @Nested
    @DisplayName("logoutUser")
    class LogoutUser {

        private Long userId;
        private AuthUser authUser;

        @BeforeEach
        void init() {
            userId = 1L;
            authUser = AuthFactory.emailUser();
            String oldRefreshToken = "refreshToken";
            setField(authUser, "id", userId);
            setField(authUser, "refreshToken", oldRefreshToken);
        }

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // when - then
            assertThatThrownBy(() ->
                    authUserService.logout(userId)
            ).isInstanceOf(AuthUserNotFoundException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
            given(authUserRepository.findByUserId(userId))
                    .willReturn(Optional.of(authUser));

            // when
            authUserService.logout(userId);

            // then
            assertThat(authUser.getRefreshToken()).isNull();
        }
    }

    @Nested
    @DisplayName("refreshToken")
    class RefreshToken {

        private Long userId;
        private AuthUser authUser;
        private String refreshToken;
        private String newRefreshToken;
        private String newAccessToken;

        @BeforeEach
        void init() {
            userId = 1L;
            authUser = AuthFactory.emailUser();
            refreshToken = "refreshToken";
            setField(authUser, "refreshToken", refreshToken);
            setField(authUser, "id", userId);
            newRefreshToken = "newRefreshToken";
            newAccessToken = "newAccessToken";
        }

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // when - then
            assertThatThrownBy(() ->
                    authUserService.refreshToken(userId, refreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_wrongRefreshToken")
        void fail_wrongRefreshToken() {
            // given
            given(authUserRepository.findByUserId(userId))
                    .willReturn(Optional.of(authUser));
            String wrongRefreshToken = "wrongRefreshToken";

            // when - then
            assertThatThrownBy(() ->
                    authUserService.refreshToken(userId, wrongRefreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_refreshTokenExpired")
        void fail_refreshTokenExpired() {
            // given
            given(authUserRepository.findByUserId(userId))
                    .willReturn(Optional.of(authUser));
            given(jwtHandler.getExpiration(refreshToken))
                    .willThrow(UnauthorizedException.class);

            // when - then
            assertThatThrownBy(() ->
                    authUserService.refreshToken(userId, refreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("success_refreshTokenAboutToExpire")
        void success_refreshTokenAboutToExpire() {
            // given
            given(authUserRepository.findByUserId(userId))
                    .willReturn(Optional.of(authUser));
            given(jwtHandler.getExpiration(refreshToken))
                    .willReturn(LocalDateTime.now().plusDays(29));
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_REFRESH), any()))
                    .willReturn(newRefreshToken);
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_ACCESS), any()))
                    .willReturn(newAccessToken);

            // when
            JwtToken jwtToken = authUserService.refreshToken(userId, refreshToken);

            // then
            assertThat(jwtToken.getAccessToken()).isEqualTo(newAccessToken);
            assertThat(jwtToken.getRefreshToken()).isEqualTo(newRefreshToken);
        }

        @Test
        @DisplayName("success_refreshTokenNotModified")
        void success_refreshTokenNotModified() {
            // given
            given(authUserRepository.findByUserId(userId))
                    .willReturn(Optional.of(authUser));
            given(jwtHandler.getExpiration(refreshToken))
                    .willReturn(LocalDateTime.now().plusMonths(2));
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_ACCESS), any()))
                    .willReturn(newAccessToken);

            // when
            JwtToken jwtToken = authUserService.refreshToken(userId, refreshToken);

            // then
            assertThat(jwtToken.getAccessToken()).isEqualTo(newAccessToken);
            assertThat(jwtToken.getRefreshToken()).isNotEqualTo(newRefreshToken);
        }
    }
}
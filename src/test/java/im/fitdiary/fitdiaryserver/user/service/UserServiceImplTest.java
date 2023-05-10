package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.UserDuplicatedException;
import im.fitdiary.fitdiaryserver.security.RoleType;
import im.fitdiary.fitdiaryserver.security.jwt.handler.JwtHandler;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.UserRepository;
import im.fitdiary.fitdiaryserver.user.service.dto.AuthToken;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateUser;
import im.fitdiary.fitdiaryserver.user.service.dto.LoginUser;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
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
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    JwtHandler jwtHandler;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    @Nested
    @DisplayName("create")
    class Create {

        @Nested
        @DisplayName("email")
        class Email {

            private CreateUser createUser;
            private User user;
            private String encodedPassword;

            @BeforeEach
            void init() {
                createUser = UserFactory.createEmailUser();
                user = UserFactory.emailUser();
                encodedPassword = "encodedPassword";
            }

            @Test
            @DisplayName("fail_duplicated")
            void fail_duplicated() {
                // given
                given(userRepository.findByLoginIdAndLoginType(
                        createUser.getLoginId(),
                        createUser.getLoginType()
                )).willReturn(Optional.of(user));

                // when - then
                assertThatThrownBy(() ->
                        userService.create(createUser)
                ).isInstanceOf(UserDuplicatedException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);

                // when
                User createdUser = userService.create(createUser);

                // then
                assertThat(createdUser.getAuth().getLoginId()).isEqualTo(createUser.getLoginId());
                assertThat(createdUser.getAuth().getLoginType()).isEqualTo(createUser.getLoginType());
                assertThat(createdUser.getAuth().getPassword()).isEqualTo(encodedPassword);
                assertThat(createdUser.getAuth().getRefreshToken()).isNull();
                assertThat(createdUser.getName()).isEqualTo(createUser.getName());
                assertThat(createdUser.getBirthYmd()).isEqualTo(createUser.getBirthYmd());
                assertThat(createdUser.getGender()).isEqualTo(createUser.getGender());
                assertThat(createdUser.getEmail()).isEqualTo(createdUser.getAuth().getLoginId());
            }
        }

        @Nested
        @DisplayName("kakao")
        class Kakao {

            private CreateUser createUser;
            private User user;

            @BeforeEach
            void init() {
                createUser = UserFactory.createKakaoUser();
                user = UserFactory.kakaoUser();
            }

            @Test
            @DisplayName("fail_duplicated")
            void fail_duplicated() {
                // given
                given(userRepository.findByLoginIdAndLoginType(
                        createUser.getLoginId(),
                        createUser.getLoginType()
                )).willReturn(Optional.of(user));

                // when - then
                assertThatThrownBy(() ->
                        userService.create(createUser)
                ).isInstanceOf(UserDuplicatedException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // when
                User createdUser = userService.create(createUser);
                String createUserEmail = (String) getField(createUser, "email");

                // then
                verify(passwordEncoder, never()).encode(anyString());
                assertThat(createdUser.getAuth().getLoginId()).isEqualTo(createUser.getLoginId());
                assertThat(createdUser.getAuth().getLoginType()).isEqualTo(createUser.getLoginType());
                assertThat(createdUser.getAuth().getPassword()).isNull();
                assertThat(createdUser.getAuth().getRefreshToken()).isNull();
                assertThat(createdUser.getName()).isEqualTo(createUser.getName());
                assertThat(createdUser.getBirthYmd()).isEqualTo(createUser.getBirthYmd());
                assertThat(createdUser.getGender()).isEqualTo(createUser.getGender());
                assertThat(createdUser.getEmail()).isEqualTo(createUserEmail);
            }
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Nested
        @DisplayName("email")
        class Email {

            private LoginUser loginUser;
            private String password;
            private User user;
            private String token;

            @BeforeEach
            void init() {
                loginUser = UserFactory.loginEmailUser();
                password = (String) getField(loginUser, "password");
                user = UserFactory.emailUser();
                setField(user, "id", 1L);
                token = "token";
            }

            @Test
            @DisplayName("fail_wrongId")
            void fail_wrongId() {
                // when - then
                assertThatThrownBy(() ->
                        userService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("fail_wrongPassword")
            void fail_wrongPassword() {
                // given
                given(userRepository.findByLoginIdAndLoginType(
                        loginUser.getLoginId(),
                        loginUser.getLoginType()
                )).willReturn(Optional.of(user));
                given(passwordEncoder.matches(password, user.getAuth().getPassword()))
                        .willReturn(false);

                // when - then
                assertThatThrownBy(() ->
                        userService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                given(userRepository.findByLoginIdAndLoginType(
                        loginUser.getLoginId(),
                        loginUser.getLoginType()
                )).willReturn(Optional.of(user));
                given(passwordEncoder.matches(password, user.getAuth().getPassword()))
                        .willReturn(true);
                given(jwtHandler.createToken(any(), anyString())).willReturn(token);

                // when
                AuthToken authToken = userService.login(loginUser);

                // then
                assertThat(authToken.getAccessToken()).isEqualTo(token);
                assertThat(authToken.getRefreshToken()).isEqualTo(token);
                assertThat(user.getAuth().getRefreshToken()).isEqualTo(token);
            }
        }

        @Nested
        @DisplayName("kakao")
        class Kakao {

            private LoginUser loginUser;
            private User user;
            private String token;

            @BeforeEach
            void init() {
                loginUser = UserFactory.loginKakaoUser();
                user = UserFactory.kakaoUser();
                setField(user, "id", 1L);
                token = "token";
            }

            @Test
            @DisplayName("fail_wrongId")
            void fail_wrongId() {
                // when - then
                assertThatThrownBy(() ->
                        userService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                given(userRepository.findByLoginIdAndLoginType(
                        loginUser.getLoginId(),
                        loginUser.getLoginType()
                )).willReturn(Optional.of(user));
                given(jwtHandler.createToken(any(), anyString())).willReturn(token);

                // when
                AuthToken authToken = userService.login(loginUser);

                // then
                assertThat(authToken.getAccessToken()).isEqualTo(token);
                assertThat(authToken.getRefreshToken()).isEqualTo(token);
                assertThat(user.getAuth().getRefreshToken()).isEqualTo(token);
                verify(passwordEncoder, never()).matches(anyString(), anyString());
            }
        }
    }

    @Nested
    @DisplayName("logout")
    class Logout {

        private Long userId;
        private User user;

        @BeforeEach
        void init() {
            userId = 1L;
            user = UserFactory.emailUser();
            String oldRefreshToken = "refreshToken";
            setField(user, "id", userId);
            setField(user.getAuth(), "refreshToken", oldRefreshToken);
        }

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // when - then
            assertThatThrownBy(() ->
                    userService.logout(userId)
            ).isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
            given(userRepository.findAuthByUserId(userId))
                    .willReturn(Optional.of(user));

            // when
            userService.logout(userId);

            // then
            assertThat(user.getAuth().getRefreshToken()).isNull();
        }
    }

    @Nested
    @DisplayName("refreshToken")
    class RefreshToken {

        private Long userId;
        private User user;
        private String refreshToken;
        private String newRefreshToken;
        private String newAccessToken;

        @BeforeEach
        void init() {
            userId = 1L;
            user = UserFactory.emailUser();
            refreshToken = "refreshToken";
            setField(user.getAuth(), "refreshToken", refreshToken);
            setField(user, "id", userId);
            newRefreshToken = "newRefreshToken";
            newAccessToken = "newAccessToken";
        }

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // when - then
            assertThatThrownBy(() ->
                    userService.refreshToken(userId, refreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_wrongRefreshToken")
        void fail_wrongRefreshToken() {
            // given
            given(userRepository.findAuthByUserId(userId))
                    .willReturn(Optional.of(user));
            String wrongRefreshToken = "wrongRefreshToken";

            // when - then
            assertThatThrownBy(() ->
                    userService.refreshToken(userId, wrongRefreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_refreshTokenExpired")
        void fail_refreshTokenExpired() {
            // given
            given(userRepository.findAuthByUserId(userId))
                    .willReturn(Optional.of(user));
            given(jwtHandler.getExpiration(refreshToken))
                    .willThrow(UnauthorizedException.class);

            // when - then
            assertThatThrownBy(() ->
                    userService.refreshToken(userId, refreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("success_refreshTokenAboutToExpire")
        void success_refreshTokenAboutToExpire() {
            // given
            given(userRepository.findAuthByUserId(userId))
                    .willReturn(Optional.of(user));
            given(jwtHandler.getExpiration(refreshToken))
                    .willReturn(LocalDateTime.now().plusDays(29));
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_REFRESH), any()))
                    .willReturn(newRefreshToken);
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_ACCESS), any()))
                    .willReturn(newAccessToken);

            // when
            AuthToken authToken = userService.refreshToken(userId, refreshToken);

            // then
            assertThat(authToken.getAccessToken()).isEqualTo(newAccessToken);
            assertThat(authToken.getRefreshToken()).isEqualTo(newRefreshToken);
        }

        @Test
        @DisplayName("success_refreshTokenNotModified")
        void success_refreshTokenNotModified() {
            // given
            given(userRepository.findAuthByUserId(userId))
                    .willReturn(Optional.of(user));
            given(jwtHandler.getExpiration(refreshToken))
                    .willReturn(LocalDateTime.now().plusMonths(2));
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_ACCESS), any()))
                    .willReturn(newAccessToken);

            // when
            AuthToken authToken = userService.refreshToken(userId, refreshToken);

            // then
            assertThat(authToken.getAccessToken()).isEqualTo(newAccessToken);
            assertThat(authToken.getRefreshToken()).isNotEqualTo(newRefreshToken);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        private Long userId;
        private User user;

        @BeforeEach
        void init() {
            userId = 1L;
            user = UserFactory.emailUser();
        }

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // when - then
            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
            given(userRepository.findById(userId))
                    .willReturn(Optional.of(user));

            // when
            User foundUser = userService.findById(userId);

            // then
            assertThat(foundUser.getId()).isEqualTo(user.getId());
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        private Long userId;
        private User user;

        @BeforeEach
        void init() {
            userId = 1L;
            user = UserFactory.emailUser();
        }

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // when - then
            assertThatThrownBy(() -> userService.deleteById(userId))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
           given(userRepository.findById(userId))
                    .willReturn(Optional.of(user));

            // when - then
            userService.deleteById(userId);
        }
    }
}
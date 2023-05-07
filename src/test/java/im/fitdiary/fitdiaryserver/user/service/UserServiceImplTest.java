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

            @Test
            @DisplayName("fail_duplicated")
            void fail_duplicated() {
                // given
                CreateUser duplicatedUser = UserFactory.createEmailUser();
                User user = UserFactory.emailUser();
                given(userRepository.findByLoginIdAndLoginType(
                        duplicatedUser.getLoginId(),
                        duplicatedUser.getLoginType()
                )).willReturn(Optional.of(user));

                // when - then
                assertThatThrownBy(() ->
                        userService.create(duplicatedUser)
                ).isInstanceOf(UserDuplicatedException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                CreateUser createUser = UserFactory.createEmailUser();
                String encodedPassword = "encodedPassword";
                given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);

                // when
                User createdUser = userService.create(createUser);

                // then
                assertThat(createdUser.getAuth().getLoginId()).isEqualTo(createUser.getLoginId());
                assertThat(createdUser.getEmail()).isEqualTo(createdUser.getAuth().getLoginId());
                assertThat(createdUser.getAuth().getPassword()).isEqualTo(encodedPassword);
            }
        }

        @Nested
        @DisplayName("kakao")
        class Kakao {

            @Test
            @DisplayName("fail_duplicated")
            void fail_duplicated() {
                // given
                CreateUser duplicatedUser = UserFactory.createKakaoUser();
                User user = UserFactory.kakaoUser();
                given(userRepository.findByLoginIdAndLoginType(
                        duplicatedUser.getLoginId(),
                        duplicatedUser.getLoginType()
                )).willReturn(Optional.of(user));

                // when - then
                assertThatThrownBy(() ->
                        userService.create(duplicatedUser)
                ).isInstanceOf(UserDuplicatedException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                CreateUser createUser = UserFactory.createKakaoUser();

                // when
                User createdUser = userService.create(createUser);

                // then
                assertThat(createdUser.getAuth().getLoginId()).isEqualTo(createUser.getLoginId());
                verify(passwordEncoder, never()).encode(anyString());
            }
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Nested
        @DisplayName("email")
        class Email {

            @Test
            @DisplayName("fail_wrongId")
            void fail_wrongId() {
                // given
                LoginUser loginUser = UserFactory.loginEmailUser();

                // when - then
                assertThatThrownBy(() ->
                        userService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("fail_wrongPassword")
            void fail_wrongPassword() {
                // given
                LoginUser loginUser = UserFactory.loginEmailUser();
                String password = (String) getField(loginUser, "password");
                User user = UserFactory.emailUser();

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
                LoginUser loginUser = UserFactory.loginEmailUser();
                String password = (String) getField(loginUser, "password");
                User user = UserFactory.emailUser();
                setField(user, "id", 1L);
                String token = "token";

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

            @Test
            @DisplayName("fail_wrongId")
            void fail_wrongId() {
                // given
                LoginUser loginUser = UserFactory.loginKakaoUser();

                // when - then
                assertThatThrownBy(() ->
                        userService.login(loginUser)
                ).isInstanceOf(InvalidLoginInfoException.class);
            }

            @Test
            @DisplayName("success")
            void success() {
                // given
                LoginUser loginUser = UserFactory.loginKakaoUser();
                User user = UserFactory.kakaoUser();
                setField(user, "id", 1L);
                String token = "token";

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

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // given
            Long wrongId = 1L;

            // when - then
            assertThatThrownBy(() ->
                    userService.logout(wrongId)
            ).isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
            User user = UserFactory.emailUser();
            Long userId = 1L;
            String oldRefreshToken = "refreshToken";
            setField(user, "id", userId);
            setField(user.getAuth(), "refreshToken", oldRefreshToken);

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

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // given
            Long wrongId = 1L;
            String refreshToken = "refreshToken";

            // when - then
            assertThatThrownBy(() ->
                    userService.refreshToken(wrongId, refreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_wrongRefreshToken")
        void fail_wrongRefreshToken() {
            // given
            User user = UserFactory.emailUser();
            Long userId = 1L;
            String refreshToken = "refreshToken";
            String wrongRefreshToken = "wrongRefreshToken";
            setField(user.getAuth(), "refreshToken", refreshToken);

            given(userRepository.findAuthByUserId(userId))
                    .willReturn(Optional.of(user));

            // when - then
            assertThatThrownBy(() ->
                    userService.refreshToken(userId, wrongRefreshToken)
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("fail_refreshTokenExpired")
        void fail_refreshTokenExpired() {
            // given
            User user = UserFactory.emailUser();
            Long userId = 1L;
            String refreshToken = "refreshToken";
            setField(user.getAuth(), "refreshToken", refreshToken);

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
            User user = UserFactory.emailUser();
            Long userId = 1L;
            String refreshToken = "refreshToken";
            setField(user, "id", userId);
            setField(user.getAuth(), "refreshToken", refreshToken);
            String newAccessToken = "newAccessToken";
            String newRefreshToken = "newRefreshToken";

            given(userRepository.findAuthByUserId(userId))
                    .willReturn(Optional.of(user));
            given(jwtHandler.getExpiration(refreshToken))
                    .willReturn(LocalDateTime.now().plusDays(29));
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_ACCESS), any()))
                    .willReturn(newAccessToken);
            given(jwtHandler.createToken(eq(RoleType.ROLE_USER_REFRESH), any()))
                    .willReturn(newRefreshToken);

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
            User user = UserFactory.emailUser();
            Long userId = 1L;
            String refreshToken = "refreshToken";
            setField(user, "id", userId);
            setField(user.getAuth(), "refreshToken", refreshToken);
            String newAccessToken = "newAccessToken";
            String newRefreshToken = "newRefreshToken";

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

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // given
            Long wrongId = 1L;

            // when - then
            assertThatThrownBy(() -> userService.findById(wrongId))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
            Long userId = 1L;
            User user = UserFactory.emailUser();
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

        @Test
        @DisplayName("fail_wrongId")
        void fail_wrongId() {
            // given
            Long wrongId = 1L;

            // when - then
            assertThatThrownBy(() -> userService.deleteById(wrongId))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // given
            Long userId = 1L;
            User user = UserFactory.emailUser();
            given(userRepository.findById(userId))
                    .willReturn(Optional.of(user));

            // when - then
            userService.deleteById(userId);
        }
    }
}
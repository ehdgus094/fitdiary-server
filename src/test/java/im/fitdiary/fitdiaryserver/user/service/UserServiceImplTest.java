package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.e401.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.e401.UnauthorizedException;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.security.jwt.model.RoleType;
import im.fitdiary.fitdiaryserver.security.jwt.service.JwtService;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
import im.fitdiary.fitdiaryserver.user.dto.RefreshTokenRes;
import im.fitdiary.fitdiaryserver.user.dto.UserRes;
import im.fitdiary.fitdiaryserver.user.entity.User;
import im.fitdiary.fitdiaryserver.user.repository.UserRepository;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.DisplayName;
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
    JwtService jwtService;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("login")
    void login() {
        // given
        User user = UserFactory.emailUser();
        setField(user, "id", 1L);
        String password = "password";
        String incorrectLoginId = "incorrect";
        String incorrectPassword = "incorrect";
        String token = "token";

        given(passwordEncoder.matches(password, user.getAuth().getPassword()))
                .willReturn(true);
        given(passwordEncoder.matches(incorrectPassword, user.getAuth().getPassword()))
                .willReturn(false);
        given(userRepository.findByLoginId(user.getAuth().getLoginId()))
                .willReturn(Optional.of(user));
        given(userRepository.findByLoginId(incorrectLoginId))
                .willReturn(Optional.empty());
        given(jwtService.createToken(any(), anyString()))
                .willReturn(token);

        // when
        LoginUserRes res = userService.login(user.getAuth().getLoginId(), password);

        // then
        assertThat(res.getAccessToken()).isEqualTo(token);
        assertThat(res.getRefreshToken()).isEqualTo(token);
        assertThat(res.getUser().getName()).isEqualTo(user.getName());
        assertThat(user.getAuth().getRefreshToken()).isEqualTo(token);
        assertThatThrownBy(() ->
                userService.login(incorrectLoginId, user.getAuth().getPassword())
        ).isInstanceOf(InvalidLoginInfoException.class);
        assertThatThrownBy(() ->
                userService.login(user.getAuth().getLoginId(), incorrectPassword)
        ).isInstanceOf(InvalidLoginInfoException.class);
    }

    @Test
    @DisplayName("logout")
    void logout() {
        // given
        User user = UserFactory.emailUser();
        Long userId = 1L;
        Long wrongUserId = 2L;
        String oldRefreshToken = "refreshToken";
        setField(user, "id", userId);
        setField(user.getAuth(), "refreshToken", oldRefreshToken);

        given(userRepository.findAuthByUserId(userId))
                .willReturn(Optional.of(user));
        given(userRepository.findAuthByUserId(wrongUserId))
                .willReturn(Optional.empty());

        // when
        userService.logout(userId);

        // then
        assertThat(user.getAuth().getRefreshToken()).isNull();
        assertThatThrownBy(() ->
                userService.logout(wrongUserId)
        ).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("refreshToken")
    void refreshToken() {
        // given
        User user = UserFactory.emailUser();
        Long userId = 1L;
        Long wrongUserId = 2L;
        String refreshToken = "refreshToken";
        String wrongRefreshToken = "wrongRefreshToken";
        String refreshTokenAboutToExpire = "refreshTokenAboutToExpire";
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";
        setField(user, "id", userId);
        setField(user.getAuth(), "refreshToken", refreshToken);

        given(userRepository.findAuthByUserId(userId))
                .willReturn(Optional.of(user));
        given(userRepository.findAuthByUserId(wrongUserId))
                .willReturn(Optional.empty());
        given(jwtService.getExpiration(refreshToken))
                .willReturn(LocalDateTime.now().plusMonths(2));
        given(jwtService.getExpiration(refreshTokenAboutToExpire))
                .willReturn(LocalDateTime.now().plusDays(29));
        given(jwtService.createToken(eq(RoleType.ROLE_USER_ACCESS), any()))
                .willReturn(newAccessToken);
        given(jwtService.createToken(eq(RoleType.ROLE_USER_REFRESH), any()))
                .willReturn(newRefreshToken);

        // when
        RefreshTokenRes res = userService.refreshToken(userId, refreshToken);
        setField(user.getAuth(), "refreshToken", refreshTokenAboutToExpire);
        RefreshTokenRes resWithNewRefreshToken = userService.refreshToken(userId, refreshTokenAboutToExpire);

        // then
        assertThat(res.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(res.getRefreshToken()).isNotEqualTo(newRefreshToken);
        assertThat(resWithNewRefreshToken.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(resWithNewRefreshToken.getRefreshToken()).isEqualTo(newRefreshToken);
        assertThatThrownBy(() ->
                userService.refreshToken(wrongUserId, refreshToken)
        ).isInstanceOf(UnauthorizedException.class);
        assertThatThrownBy(() ->
                userService.refreshToken(userId, wrongRefreshToken)
        ).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("findById")
    void findById() {
        // given
        User user = UserFactory.emailUser();
        Long userId = 1L;
        Long wrongUserId = 2L;
        setField(user, "id", userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(userRepository.findById(wrongUserId))
                .willReturn(Optional.empty());

        // when
        UserRes res = userService.findById(userId);

        // then
        assertThat(user.getName()).isEqualTo(res.getName());
        assertThatThrownBy(() -> userService.findById(wrongUserId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("deleteById")
    void deleteById() {
        // given
        User user = UserFactory.emailUser();
        Long userId = 1L;
        Long wrongUserId = 2L;
        setField(user, "id", userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(userRepository.findById(wrongUserId))
                .willReturn(Optional.empty());

        // when
        userService.deleteById(userId);

        // then
        assertThatThrownBy(() -> userService.deleteById(wrongUserId))
                .isInstanceOf(UserNotFoundException.class);
    }
}
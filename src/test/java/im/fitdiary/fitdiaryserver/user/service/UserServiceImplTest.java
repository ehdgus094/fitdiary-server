package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.exception.InvalidLoginInfoException;
import im.fitdiary.fitdiaryserver.exception.NotFoundException;
import im.fitdiary.fitdiaryserver.security.jwt.service.JwtService;
import im.fitdiary.fitdiaryserver.user.dto.LoginUserRes;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
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
        ReflectionTestUtils.setField(user, "id", 1L);
        final String password = "password";
        final String incorrectLoginId = "incorrect";
        final String incorrectPassword = "incorrect";
        final String token = "token";

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
        LoginUserRes loginUser =
                userService.login(user.getAuth().getLoginId(), password);

        // then
        assertEquals(token, loginUser.getAccessToken());
        assertEquals(token, loginUser.getRefreshToken());
        assertEquals(user.getName(), loginUser.getUser().getName());
        assertEquals(token, user.getAuth().getRefreshToken());
        assertThrows(InvalidLoginInfoException.class, () -> {
            userService.login(incorrectLoginId, user.getAuth().getPassword());
        });
        assertThrows(InvalidLoginInfoException.class, () -> {
            userService.login(user.getAuth().getLoginId(), incorrectPassword);
        });
    }

    @Test
    @DisplayName("findById")
    void findById() {
        // given
        User user = UserFactory.emailUser();
        final Long userId = 1L;
        final Long wrongUserId = 2L;
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(userRepository.findById(wrongUserId))
                .willReturn(Optional.empty());

        // when
        UserRes findUser = userService.findById(userId);

        // then
        assertEquals(findUser.getName(), user.getName());
        assertThrows(NotFoundException.class, () -> {
           userService.findById(wrongUserId);
        });
    }
}
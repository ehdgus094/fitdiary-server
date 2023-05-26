package im.fitdiary.fitdiaryserver.user.service;

import im.fitdiary.fitdiaryserver.auth.service.AuthUserService;
import im.fitdiary.fitdiaryserver.exception.e404.UserNotFoundException;
import im.fitdiary.fitdiaryserver.exception.e409.AuthUserDuplicatedException;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import im.fitdiary.fitdiaryserver.user.data.UserRepository;
import im.fitdiary.fitdiaryserver.user.event.UserProducer;
import im.fitdiary.fitdiaryserver.user.service.dto.CreateUser;
import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthUserService authUserService;

    @Mock
    UserProducer userProducer;

    @InjectMocks
    UserServiceImpl userService;

    @Nested
    @DisplayName("create")
    class Create {

        CreateUser createUser;

        @BeforeEach
        void init() {
            createUser = UserFactory.createEmailUser();
        }

        @Test
        @DisplayName("fail_duplicated")
        void fail_duplicated() {
            // given
            given(authUserService.create(any()))
                    .willThrow(AuthUserDuplicatedException.class);

            // when - then
            assertThatThrownBy(() ->
                    userService.create(createUser)
            ).isInstanceOf(AuthUserDuplicatedException.class);
        }

        @Test
        @DisplayName("success")
        void success() {
            // when
            User createdUser = userService.create(createUser);

            // then
            assertThat(createdUser.getName()).isEqualTo(createUser.getName());
            assertThat(createdUser.getBirthYmd()).isEqualTo(createUser.getBirthYmd());
            assertThat(createdUser.getGender()).isEqualTo(createUser.getGender());
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        Long userId;

        User user;

        @BeforeEach
        void init() {
            userId = 1L;
            user = UserFactory.user();
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
}
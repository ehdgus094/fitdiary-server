package im.fitdiary.fitdiaryserver.user.entity;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserAuth Entity")
class UserAuthTest {

    @Test
    @DisplayName("updateRefreshToken")
    void updateRefreshToken() {
        // given
        UserAuth auth = UserFactory.auth();
        final String newToken = "newToken";

        // when
        auth.updateRefreshToken(newToken);

        // then
        assertEquals(newToken, auth.getRefreshToken());
    }

    @Test
    @DisplayName("loginType 누락")
    void withoutLoginType() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserAuth.builder()
                    .password("1234")
                    .loginId("1234")
                    .build();
        });
    }

    @Test
    @DisplayName("loginId 누락")
    void withoutLoginId() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .password("1234")
                    .build();
        });
    }

    @Test
    @DisplayName("loginId 공백")
    void emptyLoginId() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .password("1234")
                    .loginId("")
                    .build();
        });
    }

    @Test
    @DisplayName("password 누락(email)")
    void withoutPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .loginId("1234")
                    .build();
        });
    }

    @Test
    @DisplayName("password 공백(email)")
    void emptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .loginId("1234")
                    .password("")
                    .build();
        });
    }
}
package im.fitdiary.fitdiaryserver.user.entity;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(auth.getRefreshToken()).isEqualTo(newToken);
    }

    @Test
    @DisplayName("loginType 누락")
    void withoutLoginType() {
        assertThatThrownBy(() ->
            UserAuth.builder()
                    .password("1234")
                    .loginId("1234")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("loginId 누락")
    void withoutLoginId() {
        assertThatThrownBy(() ->
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .password("1234")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("loginId 공백")
    void emptyLoginId() {
        assertThatThrownBy(() ->
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .password("1234")
                    .loginId("")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("password 누락(email)")
    void withoutPassword() {
        assertThatThrownBy(() ->
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .loginId("1234")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("password 공백(email)")
    void emptyPassword() {
        assertThatThrownBy(() ->
            UserAuth.builder()
                    .loginType(LoginType.EMAIL)
                    .loginId("1234")
                    .password("")
                    .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
package im.fitdiary.server.auth.presentation.dto;

import im.fitdiary.server.util.factory.auth.AuthFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginEmailUserReqTest {

    ValidationTemplate<LoginEmailUserReq> template =
            new ValidationTemplate<>(AuthFactory.loginEmailUserReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 LoginId")
    void validateLoginId() {
        template.failure("loginId", null);
        template.failure("loginId", "");
        template.failure("loginId", "fdasfds");
    }

    @Test
    @DisplayName("유효성 검사 Password")
    void validatePassword() {
        template.failure("password", null);
        template.failure("password", "");
    }
}
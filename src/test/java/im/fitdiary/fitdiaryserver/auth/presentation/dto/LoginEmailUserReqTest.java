package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.auth.AuthFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginEmailUserReqTest {

    private final ValidationTemplate<LoginEmailUserReq> template =
            new ValidationTemplate<>(AuthFactory.loginEmailUserReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 LoginId")
    void validateLoginId() {
        template.fail("loginId", null);
        template.fail("loginId", "");
        template.fail("loginId", "fdasfds");
    }

    @Test
    @DisplayName("유효성 검사 Password")
    void validatePassword() {
        template.fail("password", null);
        template.fail("password", "");
    }
}
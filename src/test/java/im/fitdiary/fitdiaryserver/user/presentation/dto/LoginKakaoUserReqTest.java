package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginKakaoUserReqTest {

    private final ValidationTemplate<LoginEmailUserReq> template =
            new ValidationTemplate<>(UserFactory.loginEmailUserReq());

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
}
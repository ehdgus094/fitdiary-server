package im.fitdiary.fitdiaryserver.auth.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.auth.AuthFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginKakaoUserReqTest {

    private final ValidationTemplate<LoginKakaoUserReq> template =
            new ValidationTemplate<>(AuthFactory.loginKakaoUserReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.succeed();
    }

    @Test
    @DisplayName("유효성 검사 LoginId")
    void validateLoginId() {
        template.fail("loginId", null);
        template.fail("loginId", "");
    }
}
package im.fitdiary.server.user.presentation.dto;

import im.fitdiary.server.util.factory.user.UserFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateKakaoUserReqTest {

    ValidationTemplate<CreateKakaoUserReq> template =
            new ValidationTemplate<>(UserFactory.createKakaoUserReq());

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
    }

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.failure("name", null);
        template.failure("name", "");
    }

    @Test
    @DisplayName("유효성 검사 BirthYmd")
    void validateBirthYmd() {
        template.failure("birthYmd", null);
        template.failure("birthYmd", "");
        template.failure("birthYmd", "18990101");
        template.failure("birthYmd", "20000001");
        template.failure("birthYmd", "20000100");
    }

    @Test
    @DisplayName("유효성 검사 Gender")
    void validateGender() {
        template.failure("gender", null);
    }

    @Test
    @DisplayName("유효성 검사 Email")
    void validateEmail() {
        template.failure("email", null);
        template.failure("email", "");
        template.failure("email", "fdafds");
    }
}
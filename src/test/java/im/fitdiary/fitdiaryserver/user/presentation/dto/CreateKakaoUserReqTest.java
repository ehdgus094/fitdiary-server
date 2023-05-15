package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateKakaoUserReqTest {

    ValidationTemplate<CreateKakaoUserReq> template =
            new ValidationTemplate<>(UserFactory.createKakaoUserReq());

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

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.fail("name", null);
        template.fail("name", "");
    }

    @Test
    @DisplayName("유효성 검사 BirthYmd")
    void validateBirthYmd() {
        template.fail("birthYmd", null);
        template.fail("birthYmd", "");
        template.fail("birthYmd", "18990101");
        template.fail("birthYmd", "20000001");
        template.fail("birthYmd", "20000100");
    }

    @Test
    @DisplayName("유효성 검사 Gender")
    void validateGender() {
        template.fail("gender", null);
    }

    @Test
    @DisplayName("유효성 검사 Email")
    void validateEmail() {
        template.fail("email", null);
        template.fail("email", "");
        template.fail("email", "fdafds");
    }
}
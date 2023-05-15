package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateEmailUserReqTest {

    ValidationTemplate<CreateEmailUserReq> template =
            new ValidationTemplate<>(UserFactory.createEmailUserReq());

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
        template.failure("loginId", "fdafds");
    }

    @Test
    @DisplayName("유효성 검사 Password")
    void validatePassword() {
        template.failure("password", null);
        template.failure("password", "");
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
}
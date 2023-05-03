package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import im.fitdiary.fitdiaryserver.util.validation.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.ReflectionTestUtils.*;

class LoginEmailUserReqTest {

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
        template.fail(null, req ->
                setField(req, "loginId", null)
        );
        template.fail("", req ->
                setField(req, "loginId", "")
        );
    }

    @Test
    @DisplayName("유효성 검사 Password")
    void validatePassword() {
        template.fail(null, req ->
                setField(req, "password", null)
        );
        template.fail("", req ->
                setField(req, "password", "")
        );
    }
}
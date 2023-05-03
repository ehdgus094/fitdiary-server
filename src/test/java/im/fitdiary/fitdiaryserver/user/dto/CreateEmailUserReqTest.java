package im.fitdiary.fitdiaryserver.user.dto;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import im.fitdiary.fitdiaryserver.util.validation.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.ReflectionTestUtils.*;

class CreateEmailUserReqTest {

    private final ValidationTemplate<CreateEmailUserReq> template =
            new ValidationTemplate<>(UserFactory.createEmailUserReq());

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

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.fail(null, req ->
                setField(req, "name", null)
        );
        template.fail("", req ->
                setField(req, "name", "")
        );
    }

    @Test
    @DisplayName("유효성 검사 BirthYmd")
    void validateBirthYmd() {
        template.fail(null, req ->
                setField(req, "birthYmd", null)
        );
        template.fail("18990101", req ->
                setField(req, "birthYmd", "18990101")
        );
        template.fail("20000001", req ->
                setField(req, "birthYmd", "20000001")
        );
        template.fail("20000100", req ->
                setField(req, "birthYmd", "20000100")
        );
    }

    @Test
    @DisplayName("유효성 검사 Gender")
    void validateGender() {
        template.fail(null, req ->
                setField(req, "gender", null)
        );
    }

    @Test
    @DisplayName("유효성 검사 Height")
    void validateHeight() {
        template.fail(99, req ->
                setField(req, "height", 99)
        );
        template.fail(251, req ->
                setField(req, "height", 251)
        );
        template.fail(null, req ->
                setField(req, "height", null)
        );
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.fail(19, req ->
                setField(req, "weight", 19)
        );
        template.fail(601, req ->
                setField(req, "weight", 601)
        );
        template.fail(null, req ->
                setField(req, "weight", null)
        );
    }
}
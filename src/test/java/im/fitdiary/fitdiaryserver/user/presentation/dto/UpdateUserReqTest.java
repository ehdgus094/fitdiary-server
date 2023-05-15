package im.fitdiary.fitdiaryserver.user.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.user.UserFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

class UpdateUserReqTest {

    ValidationTemplate<UpdateUserReq> template =
            new ValidationTemplate<>(UserFactory.updateUserReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.success("name", JsonNullable.undefined());
        template.failure("name", JsonNullable.of(null));
        template.failure("name", JsonNullable.of(""));
    }
}
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
        template.succeed();
    }

    @Test
    @DisplayName("유효성 검사 Name")
    void validateName() {
        template.succeed("name", JsonNullable.undefined());
        template.fail("name", JsonNullable.of(null));
        template.fail("name", JsonNullable.of(""));
    }
}
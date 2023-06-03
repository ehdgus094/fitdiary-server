package im.fitdiary.server.body.presentation.dto;

import im.fitdiary.server.util.factory.body.BodyFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;

class UpdateBodyLogReqTest {

    ValidationTemplate<UpdateBodyLogReq> template =
            new ValidationTemplate<>(BodyFactory.updateBodyLogReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 Height")
    void validateHeight() {
        template.success("height", JsonNullable.undefined());
        template.failure("height", JsonNullable.of(null));
        template.failure("height", JsonNullable.of(new BigDecimal("0")));
        template.failure("height", JsonNullable.of(new BigDecimal("251")));
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.success("weight", JsonNullable.undefined());
        template.failure("weight", JsonNullable.of(null));
        template.failure("weight", JsonNullable.of(new BigDecimal("0")));
        template.failure("weight", JsonNullable.of(new BigDecimal("1001")));
    }

    @Test
    @DisplayName("유효성 검사 MuscleMass")
    void validateMuscleMass() {
        template.success("muscleMass", JsonNullable.undefined());
        template.success("muscleMass", JsonNullable.of(null));
        template.failure("muscleMass", JsonNullable.of(new BigDecimal("0")));
        template.failure("muscleMass", JsonNullable.of(new BigDecimal("201")));
    }

    @Test
    @DisplayName("유효성 검사 BodyFat")
    void validateBodyFat() {
        template.success("bodyFat", JsonNullable.undefined());
        template.success("bodyFat", JsonNullable.of(null));
        template.failure("bodyFat", JsonNullable.of(new BigDecimal("0")));
        template.failure("bodyFat", JsonNullable.of(new BigDecimal("101")));
    }

    @Test
    @DisplayName("유효성 검사 MeasuredAt")
    void validateMeasuredAt() {
        template.success("measuredAt", JsonNullable.undefined());
        template.failure("measuredAt", JsonNullable.of(null));
        template.failure("measuredAt", JsonNullable.of(10000000000L));
    }
}
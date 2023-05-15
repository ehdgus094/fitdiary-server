package im.fitdiary.fitdiaryserver.body.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.body.BodyFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;

class UpdateBodyLogReqTest {

    private final ValidationTemplate<UpdateBodyLogReq> template =
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
        template.fail("height", JsonNullable.of(null));
        template.fail("height", JsonNullable.of(new BigDecimal("0")));
        template.fail("height", JsonNullable.of(new BigDecimal("251")));
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.success("weight", JsonNullable.undefined());
        template.fail("weight", JsonNullable.of(null));
        template.fail("weight", JsonNullable.of(new BigDecimal("0")));
        template.fail("weight", JsonNullable.of(new BigDecimal("1001")));
    }

    @Test
    @DisplayName("유효성 검사 MuscleMass")
    void validateMuscleMass() {
        template.success("muscleMass", JsonNullable.undefined());
        template.success("muscleMass", JsonNullable.of(null));
        template.fail("muscleMass", JsonNullable.of(new BigDecimal("0")));
        template.fail("muscleMass", JsonNullable.of(new BigDecimal("201")));
    }

    @Test
    @DisplayName("유효성 검사 BodyFat")
    void validateBodyFat() {
        template.success("bodyFat", JsonNullable.undefined());
        template.success("bodyFat", JsonNullable.of(null));
        template.fail("bodyFat", JsonNullable.of(new BigDecimal("0")));
        template.fail("bodyFat", JsonNullable.of(new BigDecimal("101")));
    }

    @Test
    @DisplayName("유효성 검사 MeasuredAt")
    void validateMeasuredAt() {
        template.success("measuredAt", JsonNullable.undefined());
        template.fail("measuredAt", JsonNullable.of(null));
        template.fail("measuredAt", JsonNullable.of(10000000000L));
    }
}
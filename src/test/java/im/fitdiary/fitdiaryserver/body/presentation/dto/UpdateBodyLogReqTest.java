package im.fitdiary.fitdiaryserver.body.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.body.BodyFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
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
        template.succeed();
    }

    @Test
    @DisplayName("유효성 검사 Height")
    void validateHeight() {
        template.succeed("height", JsonNullable.undefined());
        template.fail("height", JsonNullable.of(null));
        template.fail("height", JsonNullable.of(new BigDecimal("0")));
        template.fail("height", JsonNullable.of(new BigDecimal("251")));
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.succeed("weight", JsonNullable.undefined());
        template.fail("weight", JsonNullable.of(null));
        template.fail("weight", JsonNullable.of(new BigDecimal("0")));
        template.fail("weight", JsonNullable.of(new BigDecimal("1001")));
    }

    @Test
    @DisplayName("유효성 검사 MuscleMass")
    void validateMuscleMass() {
        template.succeed("muscleMass", JsonNullable.undefined());
        template.succeed("muscleMass", JsonNullable.of(null));
        template.fail("muscleMass", JsonNullable.of(new BigDecimal("0")));
        template.fail("muscleMass", JsonNullable.of(new BigDecimal("201")));
    }

    @Test
    @DisplayName("유효성 검사 BodyFat")
    void validateBodyFat() {
        template.succeed("bodyFat", JsonNullable.undefined());
        template.succeed("bodyFat", JsonNullable.of(null));
        template.fail("bodyFat", JsonNullable.of(new BigDecimal("0")));
        template.fail("bodyFat", JsonNullable.of(new BigDecimal("101")));
    }

    @Test
    @DisplayName("유효성 검사 MeasuredAt")
    void validateMeasuredAt() {
        template.succeed("measuredAt", JsonNullable.undefined());
        template.fail("measuredAt", JsonNullable.of(null));
        template.fail("measuredAt", JsonNullable.of(10000000000L));
    }
}
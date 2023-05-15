package im.fitdiary.fitdiaryserver.body.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.body.BodyFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CreateBodyLogReqTest {

    private final ValidationTemplate<CreateBodyLogReq> template =
            new ValidationTemplate<>(BodyFactory.createBodyLogReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.succeed();
    }

    @Test
    @DisplayName("유효성 검사 Height")
    void validateHeight() {
        template.succeed("height", null);
        template.fail("height", new BigDecimal("0"));
        template.fail("height", new BigDecimal("251"));
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.fail("weight", null);
        template.fail("weight", new BigDecimal("0"));
        template.fail("weight", new BigDecimal("1001"));
    }

    @Test
    @DisplayName("유효성 검사 MuscleMass")
    void validateMuscleMass() {
        template.succeed("muscleMass", null);
        template.fail("muscleMass", new BigDecimal("0"));
        template.fail("muscleMass", new BigDecimal("201"));
    }

    @Test
    @DisplayName("유효성 검사 BodyFat")
    void validateBodyFat() {
        template.succeed("bodyFat", null);
        template.fail("bodyFat", new BigDecimal("0"));
        template.fail("bodyFat", new BigDecimal("101"));
    }

    @Test
    @DisplayName("유효성 검사 MeasuredAt")
    void validateMeasuredAt() {
        template.succeed("measuredAt", null);
        template.fail("measuredAt", 10000000000L);
    }
}
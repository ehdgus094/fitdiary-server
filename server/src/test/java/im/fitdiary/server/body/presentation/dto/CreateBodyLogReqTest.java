package im.fitdiary.server.body.presentation.dto;

import im.fitdiary.server.util.factory.body.BodyFactory;
import im.fitdiary.server.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CreateBodyLogReqTest {

    ValidationTemplate<CreateBodyLogReq> template =
            new ValidationTemplate<>(BodyFactory.createBodyLogReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 Height")
    void validateHeight() {
        template.success("height", null);
        template.failure("height", new BigDecimal("0"));
        template.failure("height", new BigDecimal("251"));
    }

    @Test
    @DisplayName("유효성 검사 Weight")
    void validateWeight() {
        template.failure("weight", null);
        template.failure("weight", new BigDecimal("0"));
        template.failure("weight", new BigDecimal("1001"));
    }

    @Test
    @DisplayName("유효성 검사 MuscleMass")
    void validateMuscleMass() {
        template.success("muscleMass", null);
        template.failure("muscleMass", new BigDecimal("0"));
        template.failure("muscleMass", new BigDecimal("201"));
    }

    @Test
    @DisplayName("유효성 검사 BodyFat")
    void validateBodyFat() {
        template.success("bodyFat", null);
        template.failure("bodyFat", new BigDecimal("0"));
        template.failure("bodyFat", new BigDecimal("101"));
    }

    @Test
    @DisplayName("유효성 검사 MeasuredAt")
    void validateMeasuredAt() {
        template.success("measuredAt", null);
        template.failure("measuredAt", 10000000000L);
    }
}
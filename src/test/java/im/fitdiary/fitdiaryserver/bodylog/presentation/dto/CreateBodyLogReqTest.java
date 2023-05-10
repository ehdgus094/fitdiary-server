package im.fitdiary.fitdiaryserver.bodylog.presentation.dto;

import im.fitdiary.fitdiaryserver.util.factory.bodylog.BodyLogFactory;
import im.fitdiary.fitdiaryserver.util.template.ValidationTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CreateBodyLogReqTest {

    private final ValidationTemplate<CreateBodyLogReq> template =
            new ValidationTemplate<>(BodyLogFactory.createBodyLogReq());

    @Test
    @DisplayName("유효성 검사")
    void validate() {
        template.success();
    }

    @Test
    @DisplayName("유효성 검사 Height")
    void validateHeight() {
        template.success("height", null);
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
        template.success("muscleMass", null);
        template.fail("muscleMass", new BigDecimal("0"));
        template.fail("muscleMass", new BigDecimal("201"));
    }

    @Test
    @DisplayName("유효성 검사 BodyFat")
    void validateBodyFat() {
        template.success("bodyFat", null);
        template.fail("bodyFat", new BigDecimal("0"));
        template.fail("bodyFat", new BigDecimal("101"));
    }
}
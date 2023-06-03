package im.fitdiary.server.body.presentation.dto;

import im.fitdiary.server.body.data.dto.BodyLogEditor;
import im.fitdiary.server.common.converter.TimeConverter;
import im.fitdiary.server.common.validation.annotation.PastOrPresentTimestamp;
import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@ToString
public class UpdateBodyLogReq {

    @NotNull
    @Max(250)
    @Positive
    private JsonNullable<BigDecimal> height = JsonNullable.undefined();

    @NotNull
    @Max(1000)
    @Positive
    private JsonNullable<BigDecimal> weight = JsonNullable.undefined();

    @Max(200)
    @Positive
    private JsonNullable<BigDecimal> muscleMass = JsonNullable.undefined();

    @Max(100)
    @Positive
    private JsonNullable<BigDecimal> bodyFat = JsonNullable.undefined();

    @NotNull
    @PastOrPresentTimestamp
    private JsonNullable<Long> measuredAt = JsonNullable.undefined();

    public BodyLogEditor toEditor() {
        return new BodyLogEditor(
                height,
                weight,
                muscleMass,
                bodyFat,
                measuredAt.isPresent()
                        ? JsonNullable.of(TimeConverter.toLocalDateTime(measuredAt.get()))
                        : JsonNullable.undefined()
        );
    }
}

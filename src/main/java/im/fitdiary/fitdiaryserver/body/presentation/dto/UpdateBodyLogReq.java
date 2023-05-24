package im.fitdiary.fitdiaryserver.body.presentation.dto;

import im.fitdiary.fitdiaryserver.body.data.dto.BodyLogEditor;
import im.fitdiary.fitdiaryserver.common.converter.TimeConverter;
import im.fitdiary.fitdiaryserver.common.validation.PastOrPresentTimestamp;
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

    @NotNull(message = "height should not be null")
    @Max(value = 250, message = "height should not be over than 250")
    @Positive(message = "height should be positive")
    private JsonNullable<BigDecimal> height = JsonNullable.undefined();

    @NotNull(message = "weight should not be null")
    @Max(value = 1000, message = "weight should not be over than 1000")
    @Positive(message = "weight should be positive")
    private JsonNullable<BigDecimal> weight = JsonNullable.undefined();

    @Max(value = 200, message = "muscleMass should not be over than 200")
    @Positive(message = "muscleMass should be positive")
    private JsonNullable<BigDecimal> muscleMass = JsonNullable.undefined();

    @Max(value = 100, message = "bodyFat should not be over than 100")
    @Positive(message = "bodyFat should be positive")
    private JsonNullable<BigDecimal> bodyFat = JsonNullable.undefined();

    @NotNull(message = "measuredAt should not be null")
    @PastOrPresentTimestamp(message = "measuredAt must be past or present")
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

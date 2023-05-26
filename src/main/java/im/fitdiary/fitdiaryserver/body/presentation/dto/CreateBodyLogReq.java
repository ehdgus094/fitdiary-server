package im.fitdiary.fitdiaryserver.body.presentation.dto;

import im.fitdiary.fitdiaryserver.body.service.dto.CreateBodyLog;
import im.fitdiary.fitdiaryserver.common.converter.TimeConverter;
import im.fitdiary.fitdiaryserver.common.validation.annotation.PastOrPresentTimestamp;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@ToString
public class CreateBodyLogReq {

    @Max(value = 250, message = "height should not be over than 250")
    @Positive(message = "height should be positive")
    private BigDecimal height;

    @NotNull(message = "weight should not be null")
    @Max(value = 1000, message = "weight should not be over than 1000")
    @Positive(message = "weight should be positive")
    private BigDecimal weight;

    @Max(value = 200, message = "muscleMass should not be over than 200")
    @Positive(message = "muscleMass should be positive")
    private BigDecimal muscleMass;

    @Max(value = 100, message = "bodyFat should not be over than 100")
    @Positive(message = "bodyFat should be positive")
    private BigDecimal bodyFat;

    @PastOrPresentTimestamp(message = "measuredAt must be past or present")
    private Long measuredAt;

    public CreateBodyLog toDto(Long userId) {
        return new CreateBodyLog(
                userId,
                height,
                weight,
                muscleMass,
                bodyFat,
                TimeConverter.toLocalDateTime(measuredAt)
        );
    }
}

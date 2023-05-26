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

    @Max(250)
    @Positive
    private BigDecimal height;

    @NotNull
    @Max(1000)
    @Positive
    private BigDecimal weight;

    @Max(200)
    @Positive
    private BigDecimal muscleMass;

    @Max(100)
    @Positive
    private BigDecimal bodyFat;

    @PastOrPresentTimestamp
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

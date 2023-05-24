package im.fitdiary.fitdiaryserver.body.data.dto;

import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
public class BodyLogEditor {

    private final JsonNullable<BigDecimal> height;

    private final JsonNullable<BigDecimal> weight;

    private final JsonNullable<BigDecimal> muscleMass;

    private final JsonNullable<BigDecimal> bodyFat;

    private final JsonNullable<LocalDateTime> measuredAt;

    public BodyLogEditor(
            JsonNullable<BigDecimal> height,
            JsonNullable<BigDecimal> weight,
            JsonNullable<BigDecimal> muscleMass,
            JsonNullable<BigDecimal> bodyFat,
            JsonNullable<LocalDateTime> measuredAt
    ) {
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.measuredAt = measuredAt;
    }
}

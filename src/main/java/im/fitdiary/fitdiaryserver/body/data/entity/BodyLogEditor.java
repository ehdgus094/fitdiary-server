package im.fitdiary.fitdiaryserver.body.data.entity;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
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

    public void edit(BodyLog bodyLog) {
        if (height.isPresent()) bodyLog.setHeight(height.get());
        if (weight.isPresent()) bodyLog.setWeight(weight.get());
        if (muscleMass.isPresent()) bodyLog.setMuscleMass(muscleMass.get());
        if (bodyFat.isPresent()) bodyLog.setBodyFat(bodyFat.get());
        if (measuredAt.isPresent()) bodyLog.setMeasuredAt(measuredAt.get());
    }
}

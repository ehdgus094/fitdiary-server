package im.fitdiary.fitdiaryserver.body.presentation.dto;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.common.converter.TimeConverter;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BodyLogRes {

    private final Long id;
    private final BigDecimal height;
    private final BigDecimal weight;
    private final BigDecimal muscleMass;
    private final BigDecimal bodyFat;
    private final Long measuredAt;

    public BodyLogRes(BodyLog bodyLog) {
        this.id = bodyLog.getId();
        this.height = bodyLog.getHeight();
        this.weight = bodyLog.getWeight();
        this.muscleMass = bodyLog.getMuscleMass();
        this.bodyFat = bodyLog.getBodyFat();
        this.measuredAt = TimeConverter.toTimestamp(bodyLog.getMeasuredAt());
    }
}

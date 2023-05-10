package im.fitdiary.fitdiaryserver.bodylog.service.dto;

import im.fitdiary.fitdiaryserver.bodylog.data.entity.BodyLog;
import im.fitdiary.fitdiaryserver.common.converter.TimeConverter;
import im.fitdiary.fitdiaryserver.user.data.entity.User;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Getter
public class CreateBodyLog {

    @Nullable
    private BigDecimal height;
    private final BigDecimal weight;
    @Nullable
    private final BigDecimal muscleMass;
    @Nullable
    private final BigDecimal bodyFat;
    @Nullable
    private final Long measuredAt;

    public CreateBodyLog(
            @Nullable BigDecimal height,
            BigDecimal weight,
            @Nullable BigDecimal muscleMass,
            @Nullable BigDecimal bodyFat,
            @Nullable Long measuredAt
    ) {
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.measuredAt = measuredAt;
    }

    public void updateHeight(BigDecimal height) {
        this.height = height;
    }

    public BodyLog toEntity(User user) {
        return BodyLog.create(
                user.getId(),
                height,
                weight,
                muscleMass,
                bodyFat,
                TimeConverter.toLocalDateTime(measuredAt)
        );
    }
}

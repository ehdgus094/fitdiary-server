package im.fitdiary.fitdiaryserver.body.service.dto;

import im.fitdiary.fitdiaryserver.body.data.entity.BodyLog;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
public class CreateBodyLog {

    private final Long userId;

    @Nullable
    private BigDecimal height;

    private final BigDecimal weight;

    @Nullable
    private final BigDecimal muscleMass;

    @Nullable
    private final BigDecimal bodyFat;

    @Nullable
    private final LocalDateTime measuredAt;

    public CreateBodyLog(
            Long userId,
            @Nullable BigDecimal height,
            BigDecimal weight,
            @Nullable BigDecimal muscleMass,
            @Nullable BigDecimal bodyFat,
            @Nullable LocalDateTime measuredAt
    ) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
        this.measuredAt = measuredAt;
    }

    public void updateHeight(BigDecimal height) {
        this.height = height;
    }

    public BodyLog toEntity() {
        return BodyLog.create(
                userId,
                height,
                weight,
                muscleMass,
                bodyFat,
                measuredAt == null ? LocalDateTime.now() : measuredAt
        );
    }
}

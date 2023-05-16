package im.fitdiary.fitdiaryserver.exercise.data.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateExerciseLogDetail {

    private final Long exerciseId;

    private final boolean warmUp;

    private final int intervals;

    private final BigDecimal weight;

    private final int count;

    private final int supportCount;

    public CreateExerciseLogDetail(
            Long exerciseId,
            boolean warmUp,
            int intervals,
            BigDecimal weight,
            int count,
            int supportCount
    ) {
        this.exerciseId = exerciseId;
        this.warmUp = warmUp;
        this.intervals = intervals;
        this.weight = weight;
        this.count = count;
        this.supportCount = supportCount;
    }
}

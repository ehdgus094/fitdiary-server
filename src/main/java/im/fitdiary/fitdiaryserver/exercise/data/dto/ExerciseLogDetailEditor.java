package im.fitdiary.fitdiaryserver.exercise.data.dto;

import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;

@Getter
public class ExerciseLogDetailEditor {

    private final JsonNullable<Integer> sequence;

    private final JsonNullable<Boolean> warmUp;

    private final JsonNullable<BigDecimal> weight;

    private final JsonNullable<Integer> count;

    private final JsonNullable<Integer> supportCount;

    public ExerciseLogDetailEditor(
            JsonNullable<Integer> sequence,
            JsonNullable<Boolean> warmUp,
            JsonNullable<BigDecimal> weight,
            JsonNullable<Integer> count,
            JsonNullable<Integer> supportCount
    ) {
        this.sequence = sequence;
        this.warmUp = warmUp;
        this.weight = weight;
        this.count = count;
        this.supportCount = supportCount;
    }
}

package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.exercise.data.dto.ExerciseLogDetailEditor;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
public class UpdateExerciseLogDetailReq {

    @NotNull(message = "exerciseLogDetailId should not be null")
    @Positive(message = "exerciseLogDetailId should be positive")
    private Long exerciseLogDetailId;

    @NotNull(message = "sequence should not be null")
    @Min(value = 0, message = "sequence should not be less than 0")
    private JsonNullable<Integer> sequence = JsonNullable.undefined();

    @NotNull(message = "warmUp should not be null")
    private JsonNullable<Boolean> warmUp = JsonNullable.undefined();

    @NotNull(message = "weight should not be null")
    @Positive(message = "weight should be positive")
    @Max(value = 999, message = "weight should not be over than 999")
    private JsonNullable<BigDecimal> weight = JsonNullable.undefined();

    @NotNull(message = "count should not be null")
    @Positive(message = "count should be positive")
    @Max(value = 10000, message = "count should not be over than 10000")
    private JsonNullable<Integer> count = JsonNullable.undefined();

    @NotNull(message = "supportCount should not be null")
    @Min(value = 0, message = "supportCount should not be less than 0")
    @Max(value = 10000, message = "supportCount should not be over than 10000")
    private JsonNullable<Integer> supportCount = JsonNullable.undefined();

    public ExerciseLogDetailEditor toEditor() {
        return new ExerciseLogDetailEditor(
                sequence,
                warmUp,
                weight,
                count,
                supportCount
        );
    }
}

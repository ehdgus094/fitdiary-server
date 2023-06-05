package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.exercise.data.dto.ExerciseLogDetailEditor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@ToString
@Schema
public class UpdateExerciseLogDetailReq {

    @NotNull
    @Positive
    private Long exerciseLogDetailId;

    @NotNull
    @Min(0)
    private JsonNullable<Integer> sequence = JsonNullable.undefined();

    @NotNull
    private JsonNullable<Boolean> warmUp = JsonNullable.undefined();

    @NotNull
    @Positive
    @Max(999)
    private JsonNullable<BigDecimal> weight = JsonNullable.undefined();

    @NotNull
    @Positive
    @Max(10000)
    private JsonNullable<Integer> count = JsonNullable.undefined();

    @NotNull
    @Min(0)
    @Max(10000)
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

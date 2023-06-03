package im.fitdiary.server.exercise.presentation.dto;

import im.fitdiary.server.exercise.data.dto.CreateExerciseLogDetail;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@ToString
public class CreateExerciseLogDetailReq {

    @NotNull
    @Positive
    private Long exerciseId;

    @NotNull
    private Boolean warmUp;

    @NotNull
    @Positive
    @Max(999)
    private BigDecimal weight;

    @NotNull
    @Positive
    @Max(10000)
    private Integer count;

    @NotNull
    @Min(0)
    @Max(10000)
    private Integer supportCount;

    public CreateExerciseLogDetail toDto() {
        return new CreateExerciseLogDetail(exerciseId, warmUp, weight, count, supportCount);
    }
}

package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import im.fitdiary.fitdiaryserver.exercise.data.dto.CreateExerciseLogDetail;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
public class CreateExerciseLogDetailReq {

    @NotNull(message = "exerciseId should not be null")
    @Positive(message = "exerciseId should be positive")
    private Long exerciseId;

    @NotNull(message = "warmUp should not be null")
    private Boolean warmUp;

    @NotNull(message = "intervals should not be null")
    @Positive(message = "intervals should be positive")
    @Max(value = 86400, message = "intervals should not be over than 86400") // 1일
    private Integer intervals;

    @NotNull(message = "weight should not be null")
    @Positive(message = "weight should be positive")
    @Max(value = 999, message = "weight should not be over than 999")
    private BigDecimal weight;

    @NotNull(message = "count should not be null")
    @Positive(message = "count should be positive")
    @Max(value = 10000, message = "count should not be over than 10000")
    private Integer count;

    @NotNull(message = "supportCount should not be null")
    @Positive(message = "supportCount should be positive")
    @Max(value = 10000, message = "supportCount should not be over than 10000")
    private Integer supportCount;

    public CreateExerciseLogDetail toDto() {
        return new CreateExerciseLogDetail(exerciseId, warmUp, intervals, weight, count, supportCount);
    }
}

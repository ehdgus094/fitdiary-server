package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class UpdateExerciseLogDetailListReq {

    @NotNull(message = "exerciseLogId should not be null")
    @Positive(message = "exerciseLogId should be positive")
    private Long exerciseLogId;

    @NotNull(message = "data should not be null")
    @Size(min = 1, max = 100, message = "data size should be between 1 and 100")
    @Valid
    private List<UpdateExerciseLogDetailReq> data;
}

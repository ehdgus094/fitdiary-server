package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
public class CreateExerciseLogDetailListReq {

    @NotNull(message = "exerciseLogId should not be null")
    @Positive(message = "exerciseLogId should be positive")
    private Long exerciseLogId;

    @NotEmpty(message = "data should not be empty")
    @Valid
    private List<CreateExerciseLogDetailReq> data;
}

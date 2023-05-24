package im.fitdiary.fitdiaryserver.exercise.presentation.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@ToString
public class CreateExerciseLogDetailListReq {

    @NotNull(message = "exerciseLogId should not be null")
    @Positive(message = "exerciseLogId should be positive")
    private Long exerciseLogId;

    @NotNull(message = "data should not be null")
    @Size(min = 1, max = 100, message = "data size should be between 1 and 100")
    @Valid
    private List<CreateExerciseLogDetailReq> data;
}
